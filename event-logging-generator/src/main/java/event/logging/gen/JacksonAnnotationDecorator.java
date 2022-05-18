package event.logging.gen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JacksonAnnotationDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonAnnotationDecorator.class);

    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package .*;\n\n");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("\nimport [A-Za-z0-9.]+;\n");
    private static final Pattern CLASS_PATTERN = Pattern.compile("public (abstract )?class ([a-zA-z0-9]+)[\n <][^{]*\\{\n?");
    private static final Pattern INTERFACE_PATTERN = Pattern.compile("public interface ([a-zA-z0-9]+)[\n <][^{]*\\{\n?");
    private static final Pattern FIELD_PATTERN = Pattern.compile("(public|protected|private) ([A-Za-z0-9<>]+) ([A-Za-z0-9]+);\n?");
    private static final String CLASS = " class ";

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

        addPropertyAnnotations(inputDir, outputDir);
        addTypeAnnotations(outputDir, outputDir);
    }

    private void addPropertyAnnotations(final Path inputDir, final Path outputDir) throws IOException {
        System.out.println("Adding Jackson Annotations to all files in: " + inputDir);
        if (Files.isDirectory(inputDir)) {
            // Modify Java files.
            try (final Stream<Path> stream = Files.list(inputDir)) {
                stream.forEach(path -> {
                    if (path.toFile().getName().endsWith(".java")) {
                        System.out.println("Adding Jackson Annotations to: " + path);
                        addJacksonAnnotations(path, outputDir.resolve(path.getFileName().toString()));
                    }
                });
            }
        }
    }

    private void addTypeAnnotations(final Path inputDir, final Path outputDir) throws IOException {
        if (Files.isDirectory(inputDir)) {
            // Determine implementation and extensions.
            Map<String, Set<String>> implementations = new HashMap<>();
            try (final Stream<Path> stream = Files.list(outputDir)) {
                stream.forEach(path -> {
                    if (path.toFile().getName().endsWith(".java")) {
                        final String java = read(path);
                        final Pattern pattern = Pattern.compile("(class|interface)\\s+([A-Za-z0-9.]+)\\s+(extends|implements)\\s+([^{]+)");
                        final Matcher matcher = pattern.matcher(java);
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
                                    implementations.computeIfAbsent(superClass, k -> new HashSet<>()).add(extensionClass);
                                }
                            });
                        }
                    }
                });
            }

            implementations.forEach((superClass, subTypes) -> {
                final Path file = inputDir.resolve(superClass + ".java");
                if (!Files.isRegularFile(file)) {
                    LOGGER.error("File not found: " + file);

                } else {
                    // Add type into to superclass.
                    final StringBuilder sb = new StringBuilder();
                    sb.append("@JsonTypeInfo(\n");
                    sb.append("        use = JsonTypeInfo.Id.NAME,\n");
                    sb.append("        property = \"type\"\n");
                    sb.append(")\n");
                    sb.append("@JsonSubTypes({\n");

                    final List<String> sorted = subTypes.stream().sorted().collect(Collectors.toList());
                    for (int i = 0; i < sorted.size(); i++) {
                        final String subType = sorted.get(i);
                        sb.append("        @JsonSubTypes.Type(value = ");
                        sb.append(subType);
                        sb.append(".class, name = \"");

                        // See if all caps
                        if (subType.toUpperCase(Locale.ROOT).equals(subType)) {
                            // Lowercase
                            sb.append(subType.toLowerCase(Locale.ROOT));
                        } else {
                            // Lowercase first letter
                            sb.append(subType.substring(0, 1).toLowerCase(Locale.ROOT) + subType.substring(1));
                        }

                        sb.append("\")");
                        if (i < sorted.size() - 1) {
                            sb.append(",");
                        }
                        sb.append("\n");
                    }
                    sb.append("})\n");

                    String java = read(file);
                    java = addImport(java, "com.fasterxml.jackson.annotation.JsonTypeInfo");
                    java = addImport(java, "com.fasterxml.jackson.annotation.JsonSubTypes");

                    int start = start(java, CLASS_PATTERN);
                    if (start == -1) {
                        start = start(java, INTERFACE_PATTERN);
                    }
                    if (start == -1) {
                        LOGGER.error("Unable to find start of class or interface");
                    } else {
                        java = insert(java, sb.toString(), start);
                    }

                    write(outputDir.resolve(file.getFileName().toString()), java);
                }
            });
        }
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

    private void addJacksonAnnotations(final Path inputFile, final Path outputFile) {
        String java = read(inputFile);
        java = addJacksonAnnotations(java);

        if (relocate) {
            java = java.replaceAll("package event.logging", "package event.logging.temp");
        }

        write(outputFile, java);
    }

    public String addJacksonAnnotations(String java) {
        final int firstClassPos = end(java, CLASS_PATTERN);
        if (firstClassPos == -1) {
            return java;
        }

        final List<String> members = new ArrayList<>();
        final Map<String, String> typeMap = new HashMap<>();
        int afterMembers = -1;
        int nextClassPos = java.indexOf(CLASS, firstClassPos);
        if (nextClassPos == -1) {
            nextClassPos = Integer.MAX_VALUE;
        }

        final Matcher fieldMatcher = FIELD_PATTERN.matcher(java);
        while (fieldMatcher.find()) {
            final int start = fieldMatcher.start();
            final int end = fieldMatcher.end();
            if (start > nextClassPos) {
                break;
            }

            final String typeName = fieldMatcher.group(2);
            final String memberName = fieldMatcher.group(3);
            typeMap.put(memberName, typeName);
            if (members.contains(memberName)) {
                throw new RuntimeException("Duplicate field: " + memberName);
            }
            members.add(memberName);
            afterMembers = end;
        }

        if (members.size() > 0) {
            // Add creator
            if (afterMembers != -1) {
                String className = null;
                final Matcher classNameMatcher = CLASS_PATTERN.matcher(java);
                if (classNameMatcher.find()) {
                    className = classNameMatcher.group(2);
                }

                if (className != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(java.substring(0, afterMembers));
                    sb.append("\n    @JsonCreator\n");
                    sb.append("    public ");
                    sb.append(className);
                    sb.append("(\n");

                    // Add params
                    for (int i = 0; i < members.size(); i++) {
                        final String member = members.get(i);
                        sb.append("        @JsonProperty(");
                        sb.append("\"");
                        sb.append(member);
                        sb.append("\"");
                        sb.append(") final ");
                        sb.append(typeMap.get(member));
                        sb.append(" ");
                        sb.append(member);

                        if (i < members.size() - 1) {
                            sb.append(",\n");
                        }
                    }
                    sb.append(") {\n");

                    // Add assignments
                    for (final String member : members) {
                        sb.append("        this.");
                        sb.append(member);
                        sb.append(" = ");
                        sb.append(member);
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

                    sb.append(java.substring(afterMembers));
                    java = sb.toString();


                }
            }

            // Add member annotations.
            for (final String member : members) {
                final String accessor = member.substring(0, 1).toUpperCase(Locale.ROOT) + member.substring(1);
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
                            comment = comment.replaceAll("\n", "\" +\n            \""); // Add indentation and quotes to each line.
                            comment = comment.replaceAll("^\"", ""); // Strip start quote.
                            comment = comment.replaceAll("$\"", ""); // Strip end quote.
                            comment = comment.trim(); // Trim ends.
                            comment = comment.replaceAll("$\\+", ""); // Trim unused concatenation.
                            comment = comment.trim(); // Trim ends.
                            comment = "\"" + comment + "\""; // Add outer quotes.
                        }
                    }
                }

                final String type = typeMap.get(member);
                int start = findMemberPos(java, type, member);
                if (start != -1) {
                    if (comment != null) {
                        java = insert(java, "@JsonPropertyDescription(" + comment + "\n    )\n    ", start);
                    }

                    start = findMemberPos(java, type, member);
                    if (start != -1) {
                        java = insert(java, "@JsonProperty\n    ", start);
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

            final StringBuilder classAnnotations = new StringBuilder();
            classAnnotations.append("@JsonPropertyOrder({\n");
            for (int i = 0; i < members.size(); i++) {
                final String memberName = members.get(i);
                classAnnotations.append("    \"");
                classAnnotations.append(memberName);
                classAnnotations.append("\"");
                if (i != members.size() - 1) {
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

    private int findMemberPos(final String java, final String type, final String member) {
        return start(java, Pattern.compile("(public|protected|private)? " + type + " " + member + ";"));
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
}
