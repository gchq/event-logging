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
import java.util.stream.Collectors;

/**
 * Class for generating the JAXB classes from a source schema. The schema is first
 * altered to improve the resulting java classes.
 */
public class GenClasses {
    private static final String PUBLIC_ABSTRACT_CLASS = "public abstract class ";

    private static final String PUBLIC_CLASS = "public class ";

    private static final String XJC_PATH = "/usr/bin/xjc";
    private static final String SOURCE_SCHEMA_REGEX = "event-logging-v.*\\.xsd";

    private static final String GENERATOR_PROJECT_NAME = "event-logging-generator";
    private static final String API_PROJECT_NAME = "event-logging-api";
    private static final String BASE_PROJECT_NAME = "event-logging-base";

    public static final String SCHEMA_DIR_NAME = "schema";
    private static final String PACKAGE_NAME = "event.logging";

    private static class IOSink extends Thread {
        private final InputStream inputStream;
        private final PrintStream printStream;

        public IOSink(final InputStream inputStream, final PrintStream printStream) {
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

    public void run() throws Exception {
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
                path.getFileName().toString().matches(SOURCE_SCHEMA_REGEX)
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
        xsd = xsd.replaceAll("ComplexType", "");
        //Remove 'SimpleType' from the type names to make the class names cleaner
        xsd = xsd.replaceAll("SimpleType", "");
        StreamUtil.stringToFile(xsd, modXsd.toFile());


        // Delete existing output.
        final Path apiProjectDir = rootDir.resolve(API_PROJECT_NAME);
        final Path srcDir = apiProjectDir.resolve("src");
        final Path mainDir = srcDir.resolve("main");
        final Path mainJavaDir = mainDir.resolve("java");
        final Path mainResourcesDir = mainDir.resolve("resources");

        //src dir in the api project is transient so delete everything ready to re-generate it
        deleteAll(srcDir);
        Files.createDirectories(mainJavaDir);
        Files.createDirectories(mainResourcesDir);

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
            System.out.printf("Executing xjc failed");
            System.exit(1);
        }

        // Now modify the generated classes.
        if (Files.isDirectory(mainJavaDir)) {
            // Modify Java files.
            Files.walkFileTree(mainJavaDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    if (path.toFile().getName().endsWith(".java")) {
                        modifyFile(path.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        // Copy other classes that make up the API.
        final Path baseProjectDir = rootDir.resolve(BASE_PROJECT_NAME);
        copyAll(baseProjectDir.resolve("src"), apiProjectDir.resolve("src"));

        // Copy the schema for validation purposes.
        Path schemaPath = mainResourcesDir.resolve("event/logging/impl");
        Files.createDirectories(schemaPath);
        Files.copy(modXsd, schemaPath.resolve("schema.xsd"));
    }

    private void deleteAll(Path dir) throws IOException {
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

    private void copyAll(Path from, Path to) throws IOException {
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
                }
                return FileVisitResult.CONTINUE;
            }
        });
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
            parts[i] = "    /**\n     * Create an" + parts[i].replaceAll("/\\*\\*", "");
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
        nonDecls.stream().forEach(sb::append);
        decls.stream().forEach(sb::append);
        sb.append("}\n");

        String str = sb.toString();

        // Replace Base Object creation.
        str = str.replaceAll("public Base[^ .]+ [^\n]+\n[^\n]+\n[^\n]+\n", "");
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
