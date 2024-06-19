/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package event.logging.gen;

import com.sun.tools.xjc.Driver;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for generating the JAXB classes from a source schema. The schema is first
 * altered to improve the resulting java classes. The generated classes plus those
 * from event-logging-base are copied into event-logging-api and the jar is built from
 * event-logging-api
 */
public class GenClasses {
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    private static final String SOURCE_SCHEMA_REGEX = "event-logging-v.*\\.xsd";
    private static final Pattern SOURCE_SCHEMA_PATTERN = Pattern.compile(SOURCE_SCHEMA_REGEX);
    private static final String GENERATOR_PROJECT_NAME = "event-logging-generator";
    private static final String API_PROJECT_NAME = "event-logging-api";
    private static final String BASE_PROJECT_NAME = "event-logging-base";
    private static final String SCHEMA_DIR_NAME = "schema";
    private static final String PACKAGE_NAME = "event.logging";
    private static final Pattern EVENT_LOGGING_BASE_PATTERN = Pattern.compile("event\\.logging\\.base");
    private static final Pattern JAVAX_XML_PATTERN = Pattern.compile("javax\\.xml\\.");
    private static final Pattern COMPLEX_TYPE_PATTERN = Pattern.compile("ComplexType");
    private static final Pattern SIMPLE_TYPE_PATTERN = Pattern.compile("SimpleType");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("/\\*\\*");
    private static final Pattern BASE_PATTERN = Pattern.compile("public Base[^ .]+ [^\n]+\n[^\n]+\n[^\n]+\n");

    /**
     * Expected to be called from gradle
     */
    public static void main(final String[] args) throws Exception {
        new GenClasses().run();

        System.out.println("JAXB class generation complete");
    }

    private void run() throws Exception {

        Path rootDir = Paths.get(".").normalize().toAbsolutePath();
        if (rootDir.endsWith(GENERATOR_PROJECT_NAME)) {
            //running from within the generator module so go up one
            rootDir = Paths.get("..").normalize().toAbsolutePath();
        }
        System.out.println("Using root directory " + rootDir.toString());
        processXSDFile(rootDir);
    }

    private void processXSDFile(final Path rootDir) throws Exception {
        // Modify XSD file.
        final Path generatorProjectDir = rootDir.resolve(GENERATOR_PROJECT_NAME);
        final Path schemaDir = generatorProjectDir.resolve(SCHEMA_DIR_NAME);

        final Path modXsd = schemaDir.resolve("schema.mod.xsd");
        final Path bindingFile = generatorProjectDir.resolve("simple-binding.xjb");

        List<Path> sourceSchemas;
        try (final Stream<Path> pathStream = Files.find(schemaDir, 1, (path, atts) ->
                SOURCE_SCHEMA_PATTERN.matcher(path.getFileName().toString()).matches()
        )) {
            sourceSchemas = pathStream.collect(Collectors.toList());
        }

        Path xsdFile = null;
        if (sourceSchemas.isEmpty()) {
            System.out.printf("ERROR - No source schema found in %s matching '%s'%n",
                    schemaDir.toAbsolutePath(),
                    SOURCE_SCHEMA_REGEX);
            System.exit(1);
        } else if (sourceSchemas.size() > 1) {
            System.out.printf("ERROR - Too many source schemas found in %s matching '%s'%n",
                    schemaDir.toAbsolutePath(),
                    SOURCE_SCHEMA_REGEX);
            System.exit(1);
        } else {
            xsdFile = schemaDir.resolve(sourceSchemas.get(0).getFileName().toString());
        }

        String xsd = Files.readString(xsdFile, UTF8);
        //Remove 'ComplexType' from the type names to make the class names cleaner
        xsd = COMPLEX_TYPE_PATTERN.matcher(xsd).replaceAll("");
        //Remove 'SimpleType' from the type names to make the class names cleaner
        xsd = SIMPLE_TYPE_PATTERN.matcher(xsd).replaceAll("");
        Files.write(modXsd, xsd.getBytes(UTF8));

        // Delete existing output.
        final Path apiProjectDir = rootDir.resolve(API_PROJECT_NAME);
        final Path srcDir = apiProjectDir.resolve("src");
        final Path mainDir = srcDir.resolve("main");
        final Path mainJavaDir = mainDir.resolve("java");
        final Path mainResourcesDir = mainDir.resolve("resources");

        //src dir in the api project is transient so delete everything ready to re-generate it
        clean(srcDir.resolve("main/java/event/logging"));
        clean(srcDir.resolve("main/resources/event/logging"));
        clean(srcDir.resolve("test/java/event/logging"));

        // -Xfluent-builder comes from the jaxb-rich-contract-plugin lib and give us the nice
        //   fluent builder API.
        // -Xinheritance comes from the jaxb-plugins lib and lets us make jaxb classes
        //   implement an interface.
        final String[] xjcOptions = new String[]{
                "-xmlschema",
                "-extension",
                "-p", PACKAGE_NAME,
                "-d", mainJavaDir.toAbsolutePath().toString(),
                "-b", bindingFile.toAbsolutePath().toString(),
                "-quiet",
                modXsd.toAbsolutePath().toString(), // the source schema to gen classes from
                "-Xfluent-builder", // make builder classes/methods
                "-generateJavadocFromAnnotations=true",
                "-Xinheritance",
        };

        System.out.println("Running XJC with arguments:");
        Arrays.stream(xjcOptions)
                .map(str -> "  " + str)
                .forEach(System.out::println);

        // Run XJC to generate the classes
        final int exitStatus = Driver.run(xjcOptions, System.out, System.out);

        if (exitStatus != 0) {
            System.out.print("Executing xjc failed");
            System.exit(1);
        }

        // Now modify the generated classes.
        if (Files.isDirectory(mainJavaDir)) {
            // Modify Java files.
            Files.walkFileTree(mainJavaDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    if (path.toFile().getName().endsWith(".java")) {
                        modifyFile(path);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        // Copy other classes that make up the API.
        final Path baseProjectDir = rootDir.resolve(BASE_PROJECT_NAME);
        copyAll(baseProjectDir.resolve("src/main/java/event/logging/base"),
                apiProjectDir.resolve("src/main/java/event/logging"));

        copyAll(baseProjectDir.resolve("src/main/resources"),
                apiProjectDir.resolve("src/main/resources"));

        copyAll(baseProjectDir.resolve("src/test/java/event/logging/base"),
                apiProjectDir.resolve("src/test/java/event/logging"));

        copyAll(baseProjectDir.resolve("src/test/resources"),
                apiProjectDir.resolve("src/test/resources"));

        // Add Jackson annotations
        // This was added to see if we could create a json schema from the java classes but
        // another method was used. Leaving this here in case it is needed again.
//        System.out.println("Adding Jackson Annotations");
//        new JacksonAnnotationDecorator(true, false)
//                .addAnnotations(apiProjectDir.resolve("src/main/java/event/logging"));

        // The jaxb2-rich-contract-plugin creates some classes in com.kscs.util.jaxb so move them into
        // event.logging.fluent
        relocatePackage(apiProjectDir, "com.kscs.util.jaxb", "event.logging.jaxb.fluent");

        // Copy the schema for validation purposes.
        Path schemaPath = mainResourcesDir.resolve("event/logging/impl");
        Files.createDirectories(schemaPath);
        Files.copy(modXsd, schemaPath.resolve("schema.xsd"));
    }

    private void clean(Path path) throws IOException {
        deleteAll(path);
        Files.createDirectories(path);
    }

    private void deleteAll(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!file.getFileName().toString().contains(".gitkeep")) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void copyAll(Path from, Path to) throws IOException {
        if (Files.exists(from)) {
            Files.walkFileTree(from, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path rel = from.relativize(dir);
                    Path dest = to.resolve(rel);
                    Files.createDirectories(dest);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path rel = from.relativize(file);
                    Path dest = to.resolve(rel);
                    if (!Files.exists(dest)) {
                        Files.copy(file, dest);

                        // CHANGE OUTPUT PACKAGES.
                        byte[] data = Files.readAllBytes(dest);
                        String content = new String(data, UTF8);
                        content = EVENT_LOGGING_BASE_PATTERN.matcher(content).replaceAll("event.logging");
                        Files.write(dest, content.getBytes(UTF8));

                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void relocatePackage(final Path projectDir,
                                 final String sourcePackage,
                                 final String destPackage) throws IOException {

        final Path from = projectDir.resolve("src/main/java/" + sourcePackage.replace(".", "/"));
        final Path to = projectDir.resolve("src/main/java/" + destPackage.replace(".", "/"));
        final Path allJava = projectDir.resolve("src/main/java");

        System.out.printf("Relocating packages - projectDir: %s, from: %s, to: %s%n",
                projectDir.resolve("..").relativize(projectDir),
                projectDir.relativize(from),
                projectDir.relativize(to));

        if (Files.exists(from)) {
            Files.walkFileTree(from, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    final Path rel = from.relativize(dir);
                    final Path dest = to.resolve(rel);
                    Files.createDirectories(dest);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    final Path rel = from.relativize(file);
                    final Path dest = to.resolve(rel);
                    if (!Files.exists(dest)) {
                        Files.move(file, dest);
                        System.out.printf("  Moved file: %s to %s%n",
                                file.getFileName().toString(),
                                dest.toAbsolutePath().toString());

                        // Change output packages.
                        final byte[] data = Files.readAllBytes(dest);
                        final String content = new String(data, UTF8).replace(sourcePackage, destPackage);
                        Files.write(dest, content.getBytes(UTF8));
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        // Now fix all references to the source pkg in the whole project
        if (Files.exists(allJava)) {
            Files.walkFileTree(allJava, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (Files.exists(file)) {

                        // Change output packages.
                        final byte[] data = Files.readAllBytes(file);
                        final String originalContent = new String(data, UTF8);
                        final String newContent = originalContent.replace(sourcePackage, destPackage);
                        Files.write(file, newContent.getBytes(UTF8));

                        if (!newContent.equals(originalContent)) {
                            System.out.printf("  Replaced %s with %s in file: %s%n",
                                    sourcePackage,
                                    destPackage,
                                    file.getFileName().toString());
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void modifyFile(final Path javaFile) {
        try {
            String java = Files.readString(javaFile, UTF8);

            // Remove top JAXB comments.
            java = removeComments(java);

            // Sort annotations to ensure consistency
            java = sortLines(java, "@XmlElement(name = ", true);

            // Sort comments.
            java = sortLines(java, "* {@link", false);

            // Sort out object factory.
            if (javaFile.getFileName().toString().contains("ObjectFactory")) {
                java = fixObjectFactory(java);
            }
            // TODO remove this replaceAll once we uplift jaxb-rich-contract-plugin to
            //  >= 4.2.0.0 which is using jakarta.xml
            // Replace javax.xml. => jakarta.xml. so we can ship our lib with jaxb4 deps
            java = JAVAX_XML_PATTERN.matcher(java)
                    .replaceAll("jakarta.xml.");

            Files.write(javaFile, java.getBytes(UTF8));
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String removeComments(String java) {
        int index = 0;
        while (java.startsWith("//") && index != -1) {
            index = java.indexOf("\n");
            if (index != -1) {
                java = java.substring(index + 1);
            }
        }
        return java;
    }

    private String sortLines(final String java, final String pattern, final boolean addCommas) {
        final String[] lines = java.split("\n");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {

            final List<String> commentLines = new ArrayList<>();
            for (int j = i; j < lines.length; j++) {
                if (lines[j].contains(pattern)) {
                    commentLines.add(lines[j]);
                } else {
                    break;
                }
            }

            if (!commentLines.isEmpty()) {
                Collections.sort(commentLines);
                for (int j = 0; j < commentLines.size(); j++) {
                    String line = commentLines.get(j);
                    if (addCommas && line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }

                    sb.append(line);

                    if (addCommas && j != commentLines.size() - 1) {
                        sb.append(",");
                    }

                    sb.append("\n");
                }
                i += commentLines.size() - 1;
            } else {
                sb.append(lines[i]);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String fixObjectFactory(final String java) {
        final int index = java.indexOf("* Create an");
        String before = java.substring(0, index);
        before = before.substring(0, before.lastIndexOf("/")).trim() + "\n\n";

        final String after = java.substring(index + "* Create an".length(), java.length() - 3);
        final String[] parts = after.split("\\* Create an");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = "    /**\n     * Create an" + COMMENT_PATTERN.matcher(parts[i]).replaceAll("");
            parts[i] = "    " + parts[i].trim() + "\n\n";
        }

        final List<String> decls = new ArrayList<>();
        final List<String> nonDecls = new ArrayList<>();
        for (final String part : parts) {
            if (part.contains("@XmlElementDecl")) {
                decls.add(part);
            } else {
                nonDecls.add(part);
            }
        }
        Collections.sort(decls);
        Collections.sort(nonDecls);

        final StringBuilder sb = new StringBuilder();
        sb.append(before);
        nonDecls.forEach(sb::append);
        decls.forEach(sb::append);
        sb.append("}\n");

        String str = sb.toString();

        // Replace Base Object creation.
        str = BASE_PATTERN.matcher(str).replaceAll("");
        return str;
    }
}
