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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class for generating the JAXB classes from a source schema. The schema is first
 * altered to improve the resulting java classes. The generated classes plus those
 * from event-logging-base are copied into event-logging-api and the jar is built from
 * event-logging-api
 */
public class GenClasses {
    private static final String PUBLIC_ABSTRACT_CLASS = "public abstract class ";

    private static final String PUBLIC_CLASS = "public class ";

    private static final String XJC_PATH = "/usr/bin/xjc";
    private static final String SOURCE_SCHEMA_REGEX = "event-logging-v.*\\.xsd";
    private static final Pattern SOURCE_SCHEMA_PATTERN = Pattern.compile(SOURCE_SCHEMA_REGEX);

    private static final String GENERATOR_PROJECT_NAME = "event-logging-generator";
    private static final String API_PROJECT_NAME = "event-logging-api";
    private static final String BASE_PROJECT_NAME = "event-logging-base";

    private static final String SCHEMA_DIR_NAME = "schema";
    private static final String PACKAGE_NAME = "event.logging";

    private static final Pattern EVENT_LOGGING_BASE_PATTERN = Pattern.compile("event\\.logging\\.base");

    private static final Pattern COMPLEX_TYPE_PATTERN = Pattern.compile("ComplexType");
    private static final Pattern SIMPLE_TYPE_PATTERN = Pattern.compile("SimpleType");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("/\\*\\*");
    private static final Pattern BASE_PATTERN = Pattern.compile("public Base[^ .]+ [^\n]+\n[^\n]+\n[^\n]+\n");

    private static class IOSink extends Thread {
        private final InputStream inputStream;
        private final PrintStream printStream;

        IOSink(final InputStream inputStream, final PrintStream printStream) {
            this.inputStream = inputStream;
            this.printStream = printStream;
        }

        @Override
        public void run() {
            try {
                final byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) >= 0) {
                    printStream.println(new String(buffer, 0, len));
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(final String[] args) throws Exception {
        new GenClasses().run();

        System.out.println("JAXB class generation complete");
    }

    private void run() throws Exception {
        // Options:
        // -nv : do not perform strict validation of the input schema(s)
        // -extension : allow vendor extensions - do not strictly follow the
        // Compatibility Rules and App E.2 from the JAXB Spec
        // -b <file/dir> : specify external bindings files (each <file> must
        // have its own -b)
        // If a directory is given, **/*.xjb is searched
        // -d <dir> : generated files will go into this directory
        // -p <pkg> : specifies the target package
        // -httpproxy <proxy> : set HTTP/HTTPS proxy. Format is
        // [user[:password]@]proxyHost:proxyPort
        // -httpproxyfile <f> : Works like -httpproxy but takes the argument in
        // a file to protect password
        // -classpath <arg> : specify where to find user class files
        // -catalog <file> : specify catalog files to resolve external entity
        // references
        // support TR9401, XCatalog, and OASIS XML Catalog format.
        // -readOnly : generated files will be in read-only mode
        // -npa : suppress generation of package level annotations
        // (**/package-info.java)
        // -no-header : suppress generation of a file header with timestamp
        // -target 2.0 : behave like XJC 2.0 and generate code that doesnt use
        // any 2.1 features.
        // -xmlschema : treat input as W3C XML Schema (default)
        // -relaxng : treat input as RELAX NG (experimental,unsupported)
        // -relaxng-compact : treat input as RELAX NG compact syntax
        // (experimental,unsupported)
        // -dtd : treat input as XML DTD (experimental,unsupported)
        // -wsdl : treat input as WSDL and compile schemas inside it
        // (experimental,unsupported)
        // -verbose : be extra verbose
        // -quiet : suppress compiler output
        // -help : display this help message
        // -version : display version information
        //
        //
        // Extensions:
        // -Xlocator : enable source location support for generated code
        // -Xsync-methods : generate accessor methods with the 'synchronized'
        // keyword
        // -mark-generated : mark the generated code as
        // @javax.annotation.Generated
        // -episode <FILE> : generate the episode file for separate compilation

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

        List<Path> sourceSchemas = Files.find(schemaDir, 1, (path, atts) ->
                SOURCE_SCHEMA_PATTERN.matcher(path.getFileName().toString()).matches()
        ).collect(Collectors.toList());

        Path xsdFile = null;
        if (sourceSchemas.size() == 0) {
            System.out.println(String.format("ERROR - No source schema found in %s matching '%s'",
                    schemaDir.toAbsolutePath().toString(),
                    SOURCE_SCHEMA_REGEX));
            System.exit(1);
        } else if (sourceSchemas.size() > 1) {
            System.out.println(String.format("ERROR - Too many source schemas found in %s matching '%s'",
                    schemaDir.toAbsolutePath().toString(),
                    SOURCE_SCHEMA_REGEX));
            System.exit(1);
        } else {
            xsdFile = schemaDir.resolve(sourceSchemas.get(0).getFileName().toString());
        }

        String xsd = StreamUtil.fileToString(xsdFile.toFile());
        //Remove 'ComplexType' from the type names to make the class names cleaner
        xsd = COMPLEX_TYPE_PATTERN.matcher(xsd).replaceAll("");
        //Remove 'SimpleType' from the type names to make the class names cleaner
        xsd = SIMPLE_TYPE_PATTERN.matcher(xsd).replaceAll("");
        StreamUtil.stringToFile(xsd, modXsd.toFile());


        // Delete existing output.
        final Path apiProjectDir = rootDir.resolve(API_PROJECT_NAME);
        final Path srcDir = apiProjectDir.resolve("src");
        final Path mainDir = srcDir.resolve("main");
        final Path mainJavaDir = mainDir.resolve("java");
        final Path eventLoggingir = mainJavaDir.resolve("event/logging");
        final Path mainResourcesDir = mainDir.resolve("resources");

        final Path testDir = srcDir.resolve("test");
        final Path testJavaDir = testDir.resolve("java");
        final Path testResourcesDir = testJavaDir.resolve("resources");

        //src dir in the api project is transient so delete everything ready to re-generate it
        deleteAll(eventLoggingir);
        Files.createDirectories(eventLoggingir);
        deleteAll(mainResourcesDir);
        Files.createDirectories(mainResourcesDir);

        deleteAll(testJavaDir);
        Files.createDirectories(testJavaDir);
        deleteAll(testResourcesDir);
        Files.createDirectories(testResourcesDir);

        final String command = XJC_PATH +
                " -xmlschema" +
                " -extension" +
                " -p " + PACKAGE_NAME +
                " -d " + mainJavaDir.toAbsolutePath() +
                "    " + modXsd.toAbsolutePath() + //the source schema to gen classes from
                " -b " + bindingFile.toAbsolutePath() +
                " -quiet ";

        System.out.println("Executing: " + command);

        final Process process = Runtime.getRuntime().exec(command);
        final InputStream inputStream = process.getInputStream();
        final InputStream errorStream = process.getErrorStream();

        new IOSink(inputStream, System.out).start();
        new IOSink(errorStream, System.err).start();

        final int exitStatus = process.waitFor();

        if (exitStatus != 0) {
            System.out.print("Executing xjc failed");
            System.exit(1);
        }

        // Now modify the generated classes.
        if (Files.isDirectory(mainJavaDir)) {
            // Modify Java files.
            Files.walkFileTree(mainJavaDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    if (path.toFile().getName().endsWith(".java")) {
                        modifyFile(path.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        // Copy other classes that make up the API.
        final Path baseProjectDir = rootDir.resolve(BASE_PROJECT_NAME);
        copyAll(baseProjectDir.resolve("src/main/java/event/logging/base"), apiProjectDir.resolve("src/main/java/event/logging"));
        copyAll(baseProjectDir.resolve("src/main/resources"), apiProjectDir.resolve("src/main/resources"));
        copyAll(baseProjectDir.resolve("src/test/java/event/logging/base"), apiProjectDir.resolve("src/test/java/event/logging"));
        copyAll(baseProjectDir.resolve("src/test/resources"), apiProjectDir.resolve("src/test/resources"));

        // Copy the schema for validation purposes.
        Path schemaPath = mainResourcesDir.resolve("event/logging/impl");
        Files.createDirectories(schemaPath);
        Files.copy(modXsd, schemaPath.resolve("schema.xsd"));
    }

    private void deleteAll(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walkFileTree(dir, new SimpleFileVisitor<>() {
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
            Files.walkFileTree(from, new SimpleFileVisitor<>() {
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
                        String content = new String(data, "UTF-8");
                        content = EVENT_LOGGING_BASE_PATTERN.matcher(content).replaceAll("event.logging");
                        Files.write(dest, content.getBytes("UTF-8"));

                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void modifyFile(final File javaFile) {
        String java = StreamUtil.fileToString(javaFile);

        // Remove top JAXB comments.
        java = removeComments(java);

        // Sort annotations to ensure consistency
        java = sortLines(java, "@XmlElement(name = ", true);

        // Sort comments.
        java = sortLines(java, "* {@link", false);

        // Sort out object factory.
        if (javaFile.getName().contains("ObjectFactory")) {
            java = fixObjectFactory(java);
        }

        // Make Base objects abstract.
        if (javaFile.getName().contains("Base")) {
            java = makeAbstract(java);
        }

        StreamUtil.stringToFile(java, javaFile);
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

            if (commentLines.size() > 0) {
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

    private String makeAbstract(final String java) {
        final int index = java.indexOf(PUBLIC_CLASS);
        if (index != -1) {
            return java.substring(0, index) + PUBLIC_ABSTRACT_CLASS + java.substring(index + PUBLIC_CLASS.length());
        }
        return java;
    }
}
