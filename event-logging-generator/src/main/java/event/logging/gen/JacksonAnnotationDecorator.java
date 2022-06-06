package event.logging.gen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JacksonAnnotationDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonAnnotationDecorator.class);

    private static final Pattern PACKAGE_PATTERN =
            Pattern.compile("package .*;\n\n");
    private static final Pattern IMPORT_PATTERN =
            Pattern.compile("\nimport [A-Za-z0-9.]+;\n");
    private static final Pattern CLASS_PATTERN =
            Pattern.compile("public (abstract )?(class|interface|enum|record) ([a-zA-z0-9]+)[\n <][^{]*\\{\n?");
    //    private static final Pattern INTERFACE_PATTERN =
//            Pattern.compile("public interface ([a-zA-z0-9]+)[\n <][^{]*\\{\n?");
    private static final Pattern FIELD_PATTERN =
            Pattern.compile("(public|protected|private) ([A-Za-z0-9<>]+) ([A-Za-z0-9]+);\n?");
    private static final Pattern EXTENDS_PATTERN =
            Pattern.compile("(class|interface|enum|record)\\s+([A-Za-z0-9.]+)\\s+(extends|implements)\\s+([^{]+)");
    private static final String CLASS = " class ";
    private static final String REQUIRED = "required = true";

    private final boolean overwrite;
    private final boolean relocate;

    public static void main(String[] args) throws IOException {
        final Path mainJavaDir = Paths.get("/home/stroomdev66/work/event-logging/event-logging-api/src/main/java/event/logging");
        new JacksonAnnotationDecorator(false, true).addAnnotations(mainJavaDir);
    }

    public JacksonAnnotationDecorator(final boolean overwrite, final boolean relocate) {
        this.overwrite = overwrite;
        this.relocate = relocate;
    }

    public void addAnnotations(final Path inputDir) throws IOException {
        Path outputDir = inputDir;

        if (!overwrite) {
            outputDir = inputDir.resolve("temp");
            Files.createDirectories(outputDir);
        }

        // Inspect implementations and superclasses.
        final Classes classes = getClasses(inputDir);
        addPropertyAnnotations(classes, inputDir, outputDir);
        addTypeAnnotations(classes, outputDir, outputDir);
    }

    private void addPropertyAnnotations(final Classes classes,
                                        final Path inputDir,
                                        final Path outputDir) {
        LOGGER.info("Adding Jackson Annotations to all files in: " + inputDir);
        // Modify Java files.
        classes.allClasses.forEach((className, classInfo) -> {
            final Path path = inputDir.resolve(className + ".java");
            LOGGER.info("Adding Jackson Annotations to: " + path);
            addJacksonAnnotations(classes, classInfo, path, outputDir.resolve(path.getFileName().toString()));
        });
    }

    private void addTypeAnnotations(final Classes classes,
                                    final Path inputDir,
                                    final Path outputDir) {
        // Calculate implementations.
        final Map<String, Set<String>> implementations = new HashMap<>();
        classes.allClasses.values().forEach(classInfo -> {
            for (final String superClass : classInfo.superClassSet) {
                implementations.computeIfAbsent(superClass, k -> new HashSet<>()).add(classInfo.name);
            }
        });

        implementations.keySet().forEach(superClass -> {
            final Path inputFile = inputDir.resolve(superClass + ".java");
            if (!Files.isRegularFile(inputFile)) {
                LOGGER.error("File not found: " + inputFile);

            } else {
                final Path outputFile = outputDir.resolve(inputFile.getFileName().toString());
                addTypeAnnotations(
                        implementations,
                        superClass,
                        inputFile,
                        outputFile);
            }
        });
    }

    private void addTypeAnnotations(final Map<String, Set<String>> implementations,
                                    final String superClass,
                                    final Path inputFile,
                                    final Path outputFile) {
        final Set<String> subTypes = getDescendantImplementations(superClass, implementations);

        final List<String> sorted = subTypes.stream().sorted().collect(Collectors.toList());
        final Map<String, String> typeNames = new HashMap<>();

        // TODO : Mine the implementations so we end up with only final descendants.

        // See if we can do all first names.
        final Set<String> uniqueNames = new HashSet<>();
        for (final String subType : sorted) {
            String typeName = subType;
            if (typeName.endsWith("EventAction")) {
                typeName = getFirstName(typeName);
            }
            typeName = createTypeName(typeName);
            typeNames.put(subType, typeName);
            if (!uniqueNames.add(typeName)) {
                throw new RuntimeException("Type name not unique");
            }
        }

        // Add type into to superclass.
        final StringBuilder sb = new StringBuilder();
        sb.append("@JsonTypeInfo(\n");
        sb.append("        use = JsonTypeInfo.Id.NAME,\n");
        sb.append("        property = \"@type\"\n");
        sb.append(")\n");
        sb.append("@JsonSubTypes({\n");

        for (int i = 0; i < sorted.size(); i++) {
            final String subType = sorted.get(i);
            sb.append("        @JsonSubTypes.Type(value = ");
            sb.append(subType);
            sb.append(".class, name = \"");
            sb.append(typeNames.get(subType));
            sb.append("\")");
            if (i < sorted.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("})\n");

        String java = read(inputFile);
        java = addImport(java, "com.fasterxml.jackson.annotation.JsonTypeInfo");
        java = addImport(java, "com.fasterxml.jackson.annotation.JsonSubTypes");

        final int start = start(java, CLASS_PATTERN);
        if (start == -1) {
            LOGGER.error("Unable to find start of class or interface");
        } else {
            java = insert(java, sb.toString(), start);
        }

        write(outputFile, java);
    }

    private Set<String> getDescendantImplementations(final String superClass, final Map<String, Set<String>> implementations) {
        final Set<String> result = new HashSet<>();
        final Set<String> subClasses = implementations.get(superClass);
        if (subClasses != null) {
            for (final String subclass : subClasses) {
                final Set<String> descendants = getDescendantImplementations(subclass, implementations);
                if (descendants.size() > 0) {
                    result.addAll(descendants);
                } else {
                    result.add(subclass);
                }
            }
        }
        return result;
    }

    private String getFirstName(final String string) {
        if (string.toUpperCase(Locale.ROOT).equals(string)) {
            return string;
        }

        int pos = -1;
        for (int i = 1; i < string.length() && pos == -1; i++) {
            if (Character.isUpperCase(string.charAt(i))) {
                pos = i;
            }
        }
        if (pos != -1) {
            return string.substring(0, pos);
        }
        return string;
    }

    private String createTypeName(final String string) {
        if (string.length() == 0) {
            return string;
        }

        if (string.toUpperCase(Locale.ROOT).equals(string)) {
            return string.toLowerCase(Locale.ROOT);
        }

        return string.substring(0, 1).toLowerCase(Locale.ROOT) + string.substring(1);
    }

    private String read(final Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void write(final Path path, final String java) {
        try {
            Files.write(path, java.getBytes(StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void addJacksonAnnotations(final Classes classes,
                                       final ClassInfo classInfo,
                                       final Path inputFile,
                                       final Path outputFile) {
        String java = read(inputFile);
        java = addJacksonAnnotations(classes, classInfo, java);

        if (relocate) {
            java = java.replaceAll("package event.logging", "package event.logging.temp");
        }

        write(outputFile, java);
    }

    private List<FieldInfo> getSuperFields(final Classes classes,
                                           final ClassInfo classInfo) {
        final List<FieldInfo> fields = new ArrayList<>();
        for (final String superClass : classInfo.superClassSet) {
            final ClassInfo superClassInfo = classes.allClasses.get(superClass);
            if (superClassInfo != null) {
                fields.addAll(getSuperFields(classes, superClassInfo));
                fields.addAll(superClassInfo.fields);
            } else {
                LOGGER.info("Unknown super class: " + superClass);
            }
        }
        return fields;
    }

    public String addJacksonAnnotations(final Classes classes,
                                        final ClassInfo classInfo,
                                        String java) {
        final int firstClassPos = classInfo.firstClassPos;
        if (firstClassPos == -1) {
            return java;
        }

        final List<FieldInfo> superFields = getSuperFields(classes, classInfo);
        final List<FieldInfo> combinedFields = new ArrayList<>();
        combinedFields.addAll(superFields);
        combinedFields.addAll(classInfo.fields);

        if (combinedFields.size() > 0) {
            // Add creator
            if (classInfo.afterMembers != -1) {
                String className = classInfo.name;
                final StringBuilder sb = new StringBuilder();
                sb.append(java, 0, classInfo.afterMembers);
                sb.append("\n    @JsonCreator\n");
                sb.append("    public ");
                sb.append(className);
                sb.append("(\n");

                // Add params
                for (int i = 0; i < combinedFields.size(); i++) {
                    final FieldInfo fieldInfo = combinedFields.get(i);
                    sb.append("        @JsonProperty(");
                    sb.append("\"");
                    sb.append(fieldInfo.name);
                    sb.append("\"");
                    sb.append(") final ");
                    sb.append(fieldInfo.type);
                    sb.append(" ");
                    sb.append(fieldInfo.name);

                    if (i < combinedFields.size() - 1) {
                        sb.append(",\n");
                    }
                }
                sb.append(") {\n");

                // Add super call.
                if (superFields.size() > 0) {
                    sb.append("        super(");
                    for (int i = 0; i < superFields.size(); i++) {
                        final FieldInfo fieldInfo = superFields.get(i);
                        sb.append(fieldInfo.name);
                        if (i < superFields.size() - 1) {
                            sb.append(",\n              ");
                        }
                    }
                    sb.append(");\n");
                }

                // Add assignments
                for (final FieldInfo fieldInfo : classInfo.fields) {
                    sb.append("        this.");
                    sb.append(fieldInfo.name);
                    sb.append(" = ");
                    sb.append(fieldInfo.name);
                    sb.append(";\n");
                }

                sb.append("    }\n");

                // Ensure there is a no-args constructor.
                if (!java.contains("public " + className + "()")) {
                    sb.append("\n    public ");
                    sb.append(className);
                    sb.append("() {\n");
                    sb.append("    }\n");
                }

                sb.append(java.substring(classInfo.afterMembers));
                java = sb.toString();
            }

            // Add member annotations.
            for (final FieldInfo fieldInfo : classInfo.fields) {
                final String accessor =
                        fieldInfo.name.substring(0, 1).toUpperCase(Locale.ROOT) +
                                fieldInfo.name.substring(1);
                final String getter = "get" + accessor + "(";
                int index = java.indexOf(getter);
                if (index == -1) {
                    final String is = "is" + accessor + "(";
                    index = java.indexOf(is);
                }

                // Find description.
                String comment = null;
                if (index != -1) {
                    int pos = 0;
                    int start = -1;
                    while (pos != -1 && pos < index) {
                        pos = java.indexOf("/**", pos + 3);
                        if (pos < index) {
                            start = pos;
                        }
                    }

                    if (start != -1) {
                        final int end = java.indexOf("*/", start);
                        if (end != -1) {
                            comment = java.substring(start, end);
                            comment = comment.replaceAll("\n[ ]*\\*[ ]*", "\n"); // Remove comment line escapes.
                            comment = comment.replaceAll("/\\*\\*", ""); // Remove start comment escape.
                            comment = comment.replaceAll("\\*/", ""); // Remove trailing comment escape.
                            comment = comment.replaceAll("[ ]*\n", "\n"); // Remove spaces from end of lines.
                            comment = comment.trim(); // Trim ends.
                            comment = comment.replaceAll("\"", "\\\\\""); // Escape embedded quotes.
                            comment = comment.replaceAll("\n", " \" +\n            \""); // Add indentation and quotes to each line.
                            comment = comment.replaceAll("^\"", ""); // Strip start quote.
                            comment = comment.replaceAll("$\"", ""); // Strip end quote.
                            comment = comment.trim(); // Trim ends.
                            comment = comment.replaceAll("$\\+", ""); // Trim unused concatenation.
                            comment = comment.trim(); // Trim ends.
                            comment = "\"" + comment + "\""; // Add outer quotes.
                        }
                    }
                }

                int start = findMemberPos(java, fieldInfo);
                if (start != -1) {
                    if (comment != null) {
                        java = insert(java, "@JsonPropertyDescription(" + comment + "\n    )\n    ", start);
                    }

                    start = findMemberPos(java, fieldInfo);
                    if (start != -1) {
                        if (fieldInfo.required) {
                            java = insert(java, "@JsonProperty(required = true)\n    ", start);
                        } else {
                            java = insert(java, "@JsonProperty\n    ", start);
                        }
                    }
                }
            }

            // Add imports.
            java = addImport(java, "com.fasterxml.jackson.annotation.JsonCreator");
            java = addImport(java, "com.fasterxml.jackson.annotation.JsonInclude");
            java = addImport(java, "com.fasterxml.jackson.annotation.JsonInclude.Include");
            java = addImport(java, "com.fasterxml.jackson.annotation.JsonProperty");
            java = addImport(java, "com.fasterxml.jackson.annotation.JsonPropertyDescription");
            java = addImport(java, "com.fasterxml.jackson.annotation.JsonPropertyOrder");
            java = addImport(java, "java.util.List");

            final StringBuilder classAnnotations = new StringBuilder();
            classAnnotations.append("@JsonPropertyOrder({\n");
            for (int i = 0; i < combinedFields.size(); i++) {
                final FieldInfo fieldInfo = combinedFields.get(i);
                classAnnotations.append("    \"");
                classAnnotations.append(fieldInfo.name);
                classAnnotations.append("\"");
                if (i != combinedFields.size() - 1) {
                    classAnnotations.append(",");
                }
                classAnnotations.append("\n");
            }
            classAnnotations.append("})\n");
            classAnnotations.append("@JsonInclude(Include.NON_NULL)\n");

            // Add class level annotations.
            java = insertBefore(
                    java,
                    classAnnotations.toString(),
                    CLASS_PATTERN);
        }

        return java;
    }

    private int findMemberPos(final String java, final FieldInfo fieldInfo) {
        return start(java, Pattern.compile(
                "(public|protected|private)? " +
                        fieldInfo.type +
                        " " +
                        fieldInfo.name +
                        ";"));
    }

    private String insertBefore(final String java, final String insert, final Pattern pattern) {
        final int pos = start(java, pattern);
        if (pos != -1) {
            return insert(java, insert, pos);
        }
        return java;
    }

    private String insertAfter(final String java, final String insert, final Pattern pattern) {
        final int pos = end(java, pattern);
        if (pos != -1) {
            return insert(java, insert, pos);
        }
        return java;
    }

    private int start(final String java, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(java);
        if (matcher.find()) {
            return matcher.start();
        }
        return -1;
    }

    private int end(final String java, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(java);
        if (matcher.find()) {
            return matcher.end();
        }
        return -1;
    }

    private String insert(final String java, final String insert, final int pos) {
        return java.substring(0, pos) + insert + java.substring(pos);
    }

    private String addImport(final String java, final String importClass) {
        final String string = "" +
                "import " +
                importClass +
                ";\n";

        // Find the last import.
        final Matcher matcher = IMPORT_PATTERN.matcher(java);
        int end = -1;
        while (matcher.find()) {
            end = matcher.end();
        }
        if (end != -1) {
            // If we had an import then insert after.
            return insert(java, string, end);
        } else {
            // Insert after the package declaration.
            return insertAfter(java, string, PACKAGE_PATTERN);
        }
    }

    private Classes getClasses(final Path dir) {
//        final Map<String, Set<String>> implementations = new HashMap<>();
        final Map<String, ClassInfo> classes = new HashMap<>();

        try {
            if (Files.isDirectory(dir)) {
                // Determine implementation and extensions.
                try (final Stream<Path> stream = Files.list(dir)) {
                    stream.forEach(path -> {
                        final String fileName = path.getFileName().toString();
                        if (fileName.endsWith(".java") && !fileName.contains("package-info")) {
                            final String java = read(path);
                            final ClassInfo classInfo = getClassInfo(java);
                            classes.put(classInfo.name, classInfo);
                        }
                    });
                }
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        return new Classes(classes);
    }

    private ClassInfo getClassInfo(final String java) {
        final int firstClassPos = end(java, CLASS_PATTERN);
        int afterMembers = firstClassPos;
        final List<FieldInfo> fields = new ArrayList<>();
//        final Map<String, String> typeMap = new HashMap<>();
        if (firstClassPos != -1) {
            int nextClassPos = java.indexOf(CLASS, firstClassPos);
            if (nextClassPos == -1) {
                nextClassPos = Integer.MAX_VALUE;
            }

            int nextRequired = java.indexOf(REQUIRED);
            final Matcher fieldMatcher = FIELD_PATTERN.matcher(java);
            while (fieldMatcher.find()) {
                final int start = fieldMatcher.start();
                final int end = fieldMatcher.end();
                if (start > nextClassPos) {
                    break;
                }

                boolean required = false;
                if (nextRequired != -1) {
                    if (nextRequired < start) {
                        required = true;
                        nextRequired = java.indexOf(REQUIRED, nextRequired + REQUIRED.length());
                    }
                }

                final String fieldType = fieldMatcher.group(2);
                final String fieldName = fieldMatcher.group(3);
                final FieldInfo fieldInfo = new FieldInfo(fieldName, fieldType, required);
                if (fields.contains(fieldInfo)) {
                    throw new RuntimeException("Duplicate field: " + fieldName);
                }
                fields.add(fieldInfo);
                afterMembers = end;
            }
        }

        final Matcher classNameMatcher = CLASS_PATTERN.matcher(java);
        String name = null;
        if (classNameMatcher.find()) {
            name = classNameMatcher.group(3);
        }

        if (name == null || name.trim().length() == 0) {
            throw new RuntimeException("Unable to find class name");
        }

        // Get superclasses.
        final Set<String> superClassSet = new HashSet<>();
        final Matcher matcher = EXTENDS_PATTERN.matcher(java);
        while (matcher.find()) {
            final String extensionClass = matcher.group(2);

            String superClasses = matcher.group(4);
            superClasses = superClasses.replaceAll("<[^>]*>", ""); // Remove generics
            superClasses = superClasses.replaceAll("extends\\s+", ",");
            superClasses = superClasses.replaceAll("implements\\s+", ",");
            superClasses = superClasses.replaceAll(",", " ");
            superClasses = superClasses.replaceAll("\\s+", " ");

            Arrays.stream(superClasses.split(" ")).forEach(superClass -> {
                if (!superClass.contains(".Selector")) {
                    superClassSet.add(superClass);
                }
            });

//            if (matcher.find()) {
//                throw new RuntimeException("Unexpected repeat");
//            }
        }

        return new ClassInfo(name, firstClassPos, afterMembers, fields, superClassSet);
    }

    private static class Classes {
        private final Map<String, ClassInfo> allClasses;

        public Classes(final Map<String, ClassInfo> allClasses) {
            this.allClasses = allClasses;
        }
    }

    private static class ClassInfo {
        private final String name;
        private final int firstClassPos;
        private final int afterMembers;
        private final List<FieldInfo> fields;
        private final Set<String> superClassSet;

        public ClassInfo(final String name,
                         final int firstClassPos,
                         final int afterMembers,
                         final List<FieldInfo> fields,
                         final Set<String> superClassSet) {
            this.name = name;
            this.firstClassPos = firstClassPos;
            this.afterMembers = afterMembers;
            this.fields = fields;
            this.superClassSet = superClassSet;
        }
    }

    private static class FieldInfo {
        private final String name;
        private final String type;
        private final boolean required;

        public FieldInfo(final String name,
                         final String type,
                         final boolean required) {
            this.name = name;
            this.type = type;
            this.required = required;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FieldInfo fieldInfo = (FieldInfo) o;
            return Objects.equals(name, fieldInfo.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
