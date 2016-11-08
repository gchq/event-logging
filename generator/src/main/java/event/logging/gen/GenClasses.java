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

import event.logging.util.TransformerFactoryFactory;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GenClasses {
    private static final String PUBLIC_ABSTRACT_CLASS = "public abstract class ";

    private static final String PUBLIC_CLASS = "public class ";

    private static final String XJC_PATH = "xjc";
    private static final String SOURCE_SCHEMA_REGEX = "event-logging-v([0-9]*\\.){3}xsd";

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

        File rootDir = new File(".").getCanonicalFile();
        if (rootDir.toString().endsWith("generator")){
            //running from within the generator module so go up one
            rootDir = new File("..").getCanonicalFile();
        }
        System.out.println("Using root directory " + rootDir);
        processXSDFile(rootDir);
    }

    private void processXSDFile(final File rootDir) throws Exception {
        // Modify XSD file.
        final File xsdDir = new File(rootDir, "generator");
        final File targetDir = new File(xsdDir, "target");
        final File sourceXsd = new File(targetDir, "schema.xsd");
        final File modXsd = new File(xsdDir, "schema.mod.xsd");
        final File bindingFile = new File(xsdDir, "simple-binding.xjb");
        final Path translationsDir = Paths.get(xsdDir.toString(), "src", "main", "resources", "translations");

        List<Path> sourceSchemas = Files.find(xsdDir.toPath(), 1, (path, atts) -> {
            return path.getFileName().toString().matches(SOURCE_SCHEMA_REGEX);
        }).collect(Collectors.toList());

         File xsdFile = null;
        if (sourceSchemas.size() == 0) {
            System.out.println(String.format("ERROR - No source schema found in %s matching '%s'", xsdDir.toString(), SOURCE_SCHEMA_REGEX));
            System.exit(1);
        } else if (sourceSchemas.size() > 1) {
            System.out.println(String.format("ERROR - Too many source schemas found in %s matching '%s'", xsdDir.toString(), SOURCE_SCHEMA_REGEX));
            System.exit(1);
        } else {
            xsdFile = new File(xsdDir, sourceSchemas.get(0).getFileName().toString());
        }

        //Remove any existing schemas from the target dir
        Files.list(targetDir.toPath())
                .filter(path -> path.toString().endsWith(".xsd"))
                .forEach((path) -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException(String.format("Error deleting file %s", path.toString()),e);
                    }
                });

        //make a copy of the source schema to work on
        Files.copy(xsdFile.toPath(), sourceXsd.toPath());

        Path translatedXsdFile = doTransformations(sourceXsd.toPath(), translationsDir);

        String xsd = StreamUtil.fileToString(translatedXsdFile.toFile());
        //Remove 'ComplexType' from the type names to make the class names cleaner
        xsd = xsd.replaceAll("ComplexType", "");
        //Remove 'SimpleType' from the type names to make the class names cleaner
        xsd = xsd.replaceAll("SimpleType", "");
//        Path modifiedXsd = Paths.get(translatedXsdFile.toString().replace(".xsd",".mod.xsd"));
        StreamUtil.stringToFile(xsd, modXsd);

        final String packageName = "event.logging";

        // Delete existing output.
        final File outputDir = new File(rootDir, "event-logging");
        final File srcDir = new File(outputDir, "src");
        final File mainDir = new File(srcDir, "main");
        final File mainJava = new File(mainDir, "java");
        final File mainResources = new File(mainDir, "resources");

        deleteAll(srcDir.toPath());
        Files.createDirectories(mainJava.toPath());
        Files.createDirectories(mainResources.toPath());

        final String command = XJC_PATH + " -xmlschema -extension -p " + packageName + " -d "
                + mainJava.getCanonicalPath() + " " + modXsd.getCanonicalPath() + " -b " + bindingFile.getCanonicalPath();

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
        if (mainJava.isDirectory()) {
            // Modify Java files.
            Files.walkFileTree(mainJava.toPath(), new SimpleFileVisitor<Path>() {
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
        final File baseDir = new File(rootDir, "base");
        copyAll(baseDir.toPath(), outputDir.toPath());

        // Copy the schema for validation purposes.
        Path schemaPath = mainResources.toPath().resolve("event/logging/impl");
        Files.createDirectories(schemaPath);
        Files.copy(modXsd.toPath(), schemaPath.resolve("schema.xsd"));
    }

    /**
     * Applies all .xsl files in translationsDir (in alphanumeric order) to
     * the passed xsdFile
     * @param xsdFile
     * @param translationsDir
     * @return The path of the final output
     */
    private Path doTransformations(final Path xsdFile, final Path translationsDir){
        AtomicReference<Path> workingXsd = new AtomicReference<>(xsdFile);

        try {
            Files.list(translationsDir)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".xsl"))
                    .sorted()
                    .forEachOrdered(xsltFile -> {
                        workingXsd.set(transformFile(workingXsd.get(), xsltFile));
                    });
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Error reading directory %s", translationsDir.toString()), e);
        }
        return workingXsd.get();
    }

    /**
     * Transform xsdFile using the stylesheet xsltFile
     * @param xsdFile
     * @param xsltFile Stylesheet with a name conforming to nnn_someName.xsl where nnn is
     *                 a number to control the priority of this stylesheet
     * @return The path of the output file
     */
    private Path transformFile(final Path xsdFile, final Path xsltFile){

        String xsltFilename = xsltFile.getFileName().toString();
        String prefix = xsltFilename.substring(0, xsltFilename.indexOf("_") );

        String outFilename = xsdFile.getFileName().toString().replace(".xsd", "_" + prefix + ".xsd");

        Path outFile = Paths.get(xsdFile.getParent().toString(), outFilename);

        System.out.println(
                String.format("Transforming %s into %s using stylesheet %s",
                        xsdFile.getFileName().toString(),
                        outFile.getFileName().toString(),
                        xsltFile.getFileName().toString()));

        final TransformerFactory transformerFactory = TransformerFactoryFactory.newInstance();
        try {
            final Transformer transformer = transformerFactory.newTransformer(new StreamSource(xsltFile.toFile()));
            transformer.transform(new StreamSource(xsdFile.toFile()), new StreamResult(outFile.toFile()));
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Error transforming %s using stylesheet %s",
                            xsdFile.toString(),
                            xsltFile.toString()), e);
        }
        return outFile;
    }

    private void deleteAll(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(!file.getFileName().toString().contains(".gitkeep")) {
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

        // java = addInheritance(javaFile, java, eventObjectTypes,
        // "EventObject");
        // if (java.contains("bannersAndChatsAndConfigurations")) {
        // // Add import if it doesn't already exist.
        // java = addImport(java, "EventObject");
        // java = java.replaceAll(
        // "List<java.lang.Object> bannersAndChatsAndConfigurations",
        // "List<EventObject> objects");
        // java = java
        // .replaceAll(
        // "List<java.lang.Object> getBannersAndChatsAndConfigurations",
        // "List<EventObject> getObjects");
        // java = java
        // .replaceAll(
        // "bannersAndChatsAndConfigurations = new ArrayList<java.lang.Object>",
        // "objects = new ArrayList<EventObject>");
        // java = java.replaceAll("bannersAndChatsAndConfigurations",
        // "objects");
        // }
        //
        // java = addInheritance(javaFile, java, advancedElementTypes,
        // "AdvancedElement");
        // java = addInheritance(javaFile, java, operatorElementTypes,
        // "Operator");
        // if (java.contains("termsAndAndsAndOrs")) {
        // // Add import if it doesn't already exist.
        // java = addImport(java, "AdvancedElement");
        // java = java.replaceAll("List<java.lang.Object> termsAndAndsAndOrs",
        // "List<AdvancedElement> objects");
        // java = java.replaceAll(
        // "List<java.lang.Object> getTermsAndAndsAndOrs",
        // "List<AdvancedElement> getObjects");
        // java = java.replaceAll(
        // "termsAndAndsAndOrs = new ArrayList<java.lang.Object>",
        // "objects = new ArrayList<AdvancedElement>");
        // java = java.replaceAll("termsAndAndsAndOrs", "objects");
        // }

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

//	private String addInheritance(final File javaFile, String java, final String[] changeTypes, final String inherit) {
//		for (final String type : changeTypes) {
//			int index = type.lastIndexOf(".");
//			if (index != -1) {
//				// This is an inner type.
//				String parentType = type.substring(0, index);
//				final String innerType = type.substring(index + 1);
//				index = parentType.lastIndexOf(".");
//				if (index != -1) {
//					parentType = parentType.substring(index + 1);
//				}
//
//				if (javaFile.getName().equals(parentType + ".java")) {
//					java = inherit(java, innerType, inherit);
//				}
//
//			} else {
//				if (javaFile.getName().equals(type + ".java")) {
//					java = inherit(java, type, inherit);
//				}
//
//			}
//		}
//		return java;
//	}
//
//	private String addImport(String java, final String type) {
//		if (!java.contains("import event.logging." + type + ";")) {
//			final int classIndex = java.indexOf("class ");
//			int importIndex = java.substring(0, classIndex).lastIndexOf("import ");
//			importIndex = java.indexOf("\n", importIndex);
//			java = java.substring(0, importIndex + 1) + "import event.logging." + type + ";\n"
//					+ java.substring(importIndex + 1);
//		}
//		return java;
//	}
//
//	private String inherit(String java, final String className, final String inherit) {
//		// Add import if it doesn't already exist.
//		java = addImport(java, inherit);
//
//		// Inherit
//		java = java.replaceAll("class " + className + " \\{", "class " + className + " implements " + inherit + " {");
//
//		return java;
//	}
}
