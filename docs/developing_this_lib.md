# Developing this library

## Generation of the JAXB artefacts

The JAXB artefacts are generated using the _com.sun.tools.xjc_ tool that ships with Java.
This parses the XML Schema and builds a set of classes based on the schema.
_xjc_ is run using a number of additional plugins to handle the creation of fluent builders and common interfaces.
Prior to running _xjc_ the schema undergoes an automated tidy up process to rename many of the elements to improve the class names in the JAXB model, i.e. `FooComplexType` becomes `Foo`.

The generation process is reliant on having the required version of the _Event Logging_ XML schema in the directory _event-logging-generator/schema/_.
This directory is ignored by source control.
The Gradle build will download the schema from GitHub as long as the following line in the root _build.gradle_ file has been set to the correct schema version. 

``` groovy
def eventLoggingSchemaVer = "v3.1.2"
```

The class that manages the code generation is `event.logging.gen.GenClasses`.
As well running `xjc` it copies various non-generated classes and resources into the _event-logging-api_ module from _event-logging-base_ which is what the published jar is ultimately built from.
As part of this copy process any packages or imports for `event.logging.base` are changed to `event.logging` to reflect their new home.

### Bindings

To further fine tune the code that is generated from the schema, _xjc_ uses a bindings file `simple-binding.xjb`.
This allows us to do things like changing the name of generated classes or making classes implement a non-generated interface.
Note the bindings file is run against a modified version of the source schema (_schema.mod.xsd_) that is produced by _GenClasses_.
The main aim of this is to remove the suffix `ComplexType` from the complex types so that the Java classes don't have this as a suffix.
Because the bindings file operates against the modified schema any xpaths will need to be in terms of that scheme, i.e. `@name='Foo'` rather than `@name='FooComplexType'`.
Any changes to the schema may require changes to the bindings, e.g. the introduction of a new event action would require some additional bindings to make its class implement the `EventAction` interface.

### Generated Code Comparison

Part of the build process is to compare the generated code against a previous release.
This allows you to see how any change to the schema has impacted the java code.
As the build is unable to determine what constitutes the previous release you need to specify it in the root build file.

``` groovy
ext.previousReleaseVersion = "v4.0.7_schema-v3.2.4"
```

The Gradle build will generate the JAXB artefacts and go onto build the API jar.

The _Event Logging_ XML schema is authored in [github.com/gchq/event-logging-schema](https://github.com/gchq/event-logging-schema).
The _Event Logging XML Schema_ in _event-logging-generator/schema/_ should never be edited directly.
It should always be a copy of the desired version from _event-loggin-schema_.

## Building the _Event Logging_ API jar

The API jar is built using Gradle. This will generate the JAXB artefacts, as well as copying the API classes, test classes and XML schema from the base module into the event-logging-api module.

All files under event-logging-api/src are transient and will be generated or copied in as part of the Gradle build.

The build is run as follows:

`./gradlew clean build`

To run the build with specific version number do something like:

`./gradlew clean build -Pversion=v1.2.3_schema-v4.5.6`

Towards the end of the build process, it will download the sources jar for the latest release of _event-logging_ from GitHub and compare the Java source files in it to those just built.
This provides a quick way of seeing the impact on the API from any changes in the schema.
For example some schema changes that would not be a breaking change as far as an XML document is concerned (e.g. a rename of a complex type), would become a breaking change in the JAXB classes.

## Developing the schema in conjunction with the JAXB library

By default the build will download the _-client_ variant of the schema from github.
This is not ideal when you are making changes to the schema and want to see the impact on the JAXB library.
If you want to run the build using a local copy of the schema you can do something like the following:

```bash
./buildAgainstLocalSchema.sh
```

This will build the _client_ variant of the schema from whatever version of the master `event-logging.xsd` schema is in the local `event-logging-schema` repo, then build _event-logging_ from it.

### Documentation

The Javadoc for the library is automatically pulled from the schema annotations by `jaxb2-rich-contract-plugin`.
Therefore it is important to ensure that all elements, types and complex types in the schema are fully annotated to provide a rich set of javadocs.

## Releasing the _Event Logging_ API jar

To perform a release simply tag the _master_ branch as follows:

`git tag -a vX.Y.Z_schema-vA.B.C`

Where `X.Y.Z` is the version of the _event-logging_ API library and `A.B.C` is the version of the event-logging XMLSchema.
The two version numbers are totally independent of each other and have different life-cycles, e.g. a minor release of the schema could trigger a breaking change and major release of the API.
Equally there may be a new release of the API with an identical schema version to the previous one.
The build process will validate the tag when it is used as the version property in Travis.

When prompted to enter the commit message set the first line to `event-logging-vX.Y.Z_schema-vA.B.C` and lines 3+ to be the changes made, as extracted from the CHANGELOG.md file.
Once the tag is picked up by Travis, the build will be run and the build artefacts published to [GitHub releases](https://github.com/gchq/event-logging/releases).

