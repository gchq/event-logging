# Event Logging


_Event Logging_ is an API for logging events conforming to the [_Event Logging XML Schema_](https://github.com/gchq/event-logging-schema).
The API uses a generated Java JAXB model of _Event Logging XML Schema_.
_Event Logging_ can be incorporated into your Java application to provide a means of recording and outputting audit events.

## Using the _Event Logging_ API

### Gradle/Maven dependencies

The Event Logging API is available as a Maven/Gradle dependency on [Bintray](https://bintray.com/stroom/event-logging/event-logging). To include it in your Gradle build add the following:

To include it in your Gradle build add the following:

```groovy
repositories {
  //...
  maven { url "https://dl.bintray.com/stroom/event-logging" }
}

//...

dependencies {
  compile 'event-logging:event-logging:v4.0.7_schema-v3.2.4'
}
```

> **NOTE**:  
Version 3.x.x+ of event-logging is compatible with Java 8+  

The second version number in the version string is the version of the _event-logging-schema_ XML Schema that the library uses.

### Calling the API

The interface for logging audit events is _LoggingEventsService.java_.
A default implementation is included in the form of _DefaultEventLoggingService.java_.
This simple implementation writes the serialized events out to a Log4J appender.

Examples of how to construct various types of events and log them can be found in the test class _base/src/test/java/event/logging/EventLoggingServiceIT.java_.

The following is a very simple example of logging an _Authentication_ type event.

```java
// Create the logging service
final EventLoggingService eventLoggingService = new DefaultEventLoggingService();

// Create the event object
final Event event = eventLoggingService.buildEvent()
        .withEventTime(EventTime.builder()
                .withTimeCreated(new Date())
                .build())
        .withEventSource(EventSource.builder()
                .withSystem(SystemDetail.builder()
                        .withName("Test System")
                        .withEnvironment("Test")
                        .build())
                .withGenerator("JUnit")
                .withDevice(Device.builder()
                        .withIPAddress("123.123.123.123")
                        .build())
                .withUser(User.builder()
                        .withId("someuser")
                        .build())
                .build())
        .withEventDetail(EventDetail.builder()
                .withTypeId("LOGON")
                .withDescription("A user logon")
                .withAuthenticate(AuthenticateEventAction.builder()
                        .withAction(AuthenticateAction.LOGON)
                        .withUser(User.builder()
                                .withId("someuser")
                                .build())
                        .build())
                .build())
        .build();

// Send the event 
eventLoggingService.log(event);
```

Alternatively you can use the fully fluent style (using the `end()` methods on the `Builder` classes) to build the event.
While more compact, this is heavily reliant on careful indentation to ensure readability.

```java
// Create the logging service
final EventLoggingService eventLoggingService = new DefaultEventLoggingService();

// Create the event object
final Event event = eventLoggingService.buildEvent()
        .withEventTime()
                .withTimeCreated(new Date())
                .end()
        .withEventSource()
                .withSystem()
                        .withName("Test System")
                        .withEnvironment("Test")
                        .end()
                .withGenerator("JUnit")
                .withDevice()
                        .withIPAddress("123.123.123.123")
                        .end()
                .withUser()
                        .withId("someuser")
                        .end()
                .end()
        .withEventDetail()
                .withTypeId("LOGON")
                .withDescription("A user logon")
                .withAuthenticate()
                        .withAction(AuthenticateAction.LOGON)
                        .withUser()
                                .withId("someuser")
                                .end()
                        .end()
                .end()
        .build();

// Send the event
eventLoggingService.log(event);
```

_event-logging_ is used by [_Stroom_](https://github.com/gchq/stroom).
An example of how it used can be seen here: [`StroomEventLoggingServiceImpl`](https://github.com/gchq/stroom/blob/master/stroom-event-logging/stroom-event-logging-impl/src/main/java/stroom/event/logging/impl/StroomEventLoggingServiceImpl.java)

> **NOTE**:  
We appreciate the JAXB generated code is not the nicest to work with. A fluent builder style version of the library is in the pipeline.

## Developing this library

### Generation of the JAXB artefacts

The JAXB artefacts are generated using the _xjc_ binary that ships with the Java JDK. This parses the XML Schema and builds a set of classes based on the schema. Prior to running _xjc_ the schema undergoes an automated tidy up process to rename many of the elements to improve the class names in the JAXB model. 

The generation process is reliant on having the required version of the _Event Logging_ XML schema in the directory _event-logging-generator/schema/_.  The Gradle build will download the schema from GitHub as long as the following line in the root _build.gradle_ file has been set to the correct schema version. 

```
def eventLoggingSchemaVer = "v3.1.2"
```

The Gradle build will generate the JAXB artefacts and go onto build the API jar.

The _Event Logging_ XML schema is authored in [github.com/gchq/event-logging-schema](https://github.com/gchq/event-logging-schema). The _Event Logging XML Schema_ in _event-logging-generator/schema/_ should never be edited directly. It should always be a copy of the desired version from _event-loggin-schema_.

### Building the _Event Logging_ API jar

The API jar is built using Gradle. This will generate the JAXB artefacts, as well as copying the API classes, test classes and XML schema from the base module into the event-logging-api module.

All files under event-logging-api/src are transient and will be generated or copied in as part of the Gradle build.

The build is run as follows:

`./gradlew clean build`

To run the build with specific version number do something like:

`./gradlew clean build -Pversion=v1.2.3_schema-v4.5.6`

Towards the end of the build process, it will download the sources jar for the latest release of _event-logging_ from GitHub and compare the Java source files in it to those just built. This provides a quick way of seeing the impact on the API from any changes in the schema.  For example some schema changes that would not be a breaking change as far as an XML document is concerned (e.g. a rename of a complex type), would become a breaking change in the JAXB classes.

### Developing the schema in conjunction with the JAXB library

By default the build will download the _-client_ variant of the schema from github.
This is not ideal when you are making changes to the schema and want to see the impact on the JAXB library.
If you want to run the build using a local copy of the schema you can do something like the following:

```bash
./buildAgainstLocalSchema.sh
# Replace /home/dev/git_work/event-logging-schema with the path to your event-logging-schema repo.
```

This will build the _client_ variant of the schema then build _event-logging_.

### Releasing the _Event Logging_ API jar

To perform a release simply tag the _master_ branch as follows:

`git tag -a vX.Y.Z_schema-vA.B.C`

Where `X.Y.Z` is the version of the _event-logging_ API library and `A.B.C` is the version of the event-logging XMLSchema. The two version numbers are totally independent of each other and have different life-cycles, e.g. a minor release of the schema could trigger a breaking change and major release of the API. Equally there may be a new release of the API with an identical schema version to the previous one. The build process will validate the tag when it is used as the version property in Travis.

When prompted to enter the commit message set the first line to `event-logging-vX.Y.Z_schema-vA.B.C` and lines 3+ to be the changes made, as extracted from the CHANGELOG.md file. Once the tag is picked up by Travis, the build will be run and the build artefacts published to [GitHub releases](https://github.com/gchq/event-logging/releases).


