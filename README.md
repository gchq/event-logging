# Event Logging

_Event Logging_ is a Java API for logging audit events conforming to the [_Event Logging XML Schema_](https://github.com/gchq/event-logging-schema).
The API uses a generated Java JAXB model of the _Event Logging XML Schema_.
_Event Logging_ can be incorporated into your Java application to provide a means of recording and outputting audit events or user actions for compliance, security or monitoring.

As this library is essentially a Java representation of the [_Event Logging XML Schema_](https://github.com/gchq/event-logging-schema) it helps to refer to the schema to better understand the Java model.
The [schema documentation](https://gchq.github.io/event-logging-schema/) provides a lot of detail about how to model events using the schema which will be helpful when using this library.

The Javadoc for the latest release of the library is available [here](https://gchq.github.io/event-logging/).

This library requires Java 8 as a minimum.
The only dependencies it brings with it are:

* `jakarta.xml.bind:jakarta.xml.bind-api` 
* `com.sun.xml.bind:jaxb-impl` 
* `org.slf4j:slf4j-api`

By default the created events are serialised to XML and passed to an SLF4J logger which would typically be linked to a rolling file appender.
If the events are destined for [Stroom](https://github.com/gchq/stroom) then standard practice is to serialise the events to files locally.
Another process, e.g. a cron job, then sends the logs to their destination using curl or similar.
[stroom-log-sender](https://hub.docker.com/r/gchq/stroom-log-sender) can also be used for sending log files to stroom.


## Versions

For a list of the versions of this library see [here](https://gchq.github.io/event-logging/)


## What to Log

The aim of this API is to capture the following information about an action/event.

* *Who* - Who performed the action, i.e the user ID or some other identifier to link the event to a user and details of the user's device.
* *Where* - Where did the event happen, i.e on what system, device, network address.
* *What* - The detail of what they did, e.g. copy a file named X from A to B.
* *When* - The time the event happened.
* *Why* - The purpose and/or justification for their action, e.g. where compliance rules dictate that certain actions have prior approval.
          The requirement to capture the why is dependant on the rules in place for the system using this library.


## Using the _Event Logging_ API

### Gradle/Maven dependencies

The Event Logging API is available as a Maven/Gradle dependency on [Maven Central](https://mvnrepository.com/artifact/uk.gov.gchq.eventlogging/event-logging).

To include it in your Gradle build add the following:

```groovy
repositories {
  mavenCentral()
}

dependencies {
  compile 'uk.gov.gchq.eventlogging:event-logging:5.0-beta.16_schema-v4.0-beta.3'
}
```

To include it in your Maven build add the following:

```xml
<dependency>
    <groupId>uk.gov.gchq.eventlogging</groupId>
    <artifactId>event-logging</artifactId>
    <version>5.0-beta.16_schema-v4.0-beta.3</version>
</dependency>
```


> **NOTE**:  
**The Maven coordinates changed in the following major versions when event-logging was moved from Bintray to Maven Central.**  
* **3.3.0_schema-v3.3.1**  
* **4.0.8_schema-v3.2.4**  
* **5.0-beta.16_schema-v4.0-beta.3**

The second version number in the version string is the version of the _event-logging-schema_ XML Schema that the library uses.

v5 is currently in beta but we would encourage you to use it as only v5 includes the fluent builder style API and rich javadoc.
We do not anticipate making breaking changes unless we encounter issues with it, and aim to shortly take it out of beta.
This version is currently in use by stroom for recording its own events.

### Example Application

A standalone example application can be found in `./example-logged-application` that shows how you can include logging in your application.
See [here](./example-logged-application/README.md) for details.


### Calling the API

The API has two main parts, the class model that represents the schema and the classes involved with serialising and logging the events.


#### Event Class Model

The class model is auto-generated from the XML schema and includes fluent builder classes and methods to make constructing an event easier.
The top level class for constructing an event is `event.logging.Event`.

```java
final Event event = Event.builder()
        //...
        .build();
```


#### Logging Service

The interface for logging audit events is `EventLoggingService.java`.
A default implementation of this interface is included in the form of `DefaultEventLoggingService.java`.
This simple implementation serialises the `Event` passed to it to an XML String and passes that to an implementation of `LogReceiver`.

By default this library will use the `LoggerLogReceiver` implementation to receive and handle the XML events.
This implementation will write the XML to an SLF4J logger called `event-logger`.
You then need to add configuration to your logging framework, i.e. Log4J/Logback/etc. to handle the `event-logger` logs, e.g. writing them to rolled log files.
See [here](./example-logged-application/src/main/resources/logback.xml) for an example Logback configuration.

Sending your rolled event logs to Stroom would then be done using something like [send_to_stroom.sh](https://github.com/gchq/stroom-clients/tree/master/bash) or [stroom-log-sender](https://hub.docker.com/r/gchq/stroom-log-sender).

If you don't wish to use the `LoggerLogReceiver` to handle the logs, you can make your own implementation of `LogReceiver`.
In order to use your own implementation you need to set the system property `event.logging.logreceiver` with the fully qualified class name of your implementation.
`LogReceiverFactory` will then issue new instances of your implementation instead of `LoggerLogReceiver`.


#### Schema Validation

The library includes a copy of the XML Schema and `DefaultEventLoggingService` can validate serialised events against the schema.
By default schema validation is not enabled as it adds a performance overhead.
It is recommended however to enable schema validation during testing to ensure the events produced are valid against the schema.
While the JAXB model will ensure a degree of correctness, it cannot enforce things like string patterns or mandatory elements.

If you are using `DefaultEventLoggingService` then validation can be enabled either by setting the java system property `event.logging.validate` to `true` or calling `EventLoggingService.setValidate()`.


#### Examples

Examples of how to construct various types of events and log them can be found in the test class `base/src/test/java/event/logging/EventLoggingServiceIT.java`.

These examples assume you have created your own implementation of EventLoggingService that overrides the `createEvent(...)` methods.
Using your own implementation that extends `DefaultEventLoggingService` is the preferred approach to dealing with common values in your events.
See [CustomEventLoggingService](./example-logged-application/src/main/java/event/logging/example/CustomEventLoggingService.java) for an example.

The following is a very simple example of logging an _Authentication_ type event using the default logging service supplied with the API.
This example does not use any common event values so 

```java

// Define your own implementation of EventLoggingService that provides common event values
public static class CustomEventLoggingService extends DefaultEventLoggingService {

    @Override
    public Event createEvent(final String typeId,
                             final String description,
                             final Purpose purpose,
                             final EventAction eventAction) {
        return Event.builder()
            .withEventTime(EventTime.builder()
                    .withTimeCreated(new Date())
                    .build())
            .withEventSource(EventSource.builder()
                    .withSystem(SystemDetail.builder()
                            .withName("My System Name")
                            .withEnvironment("Test")
                            .withVersion(getBuildVersion())
                            .build())
                    .withGenerator("CustomEventLoggingService")
                    .withClient(getClientDevice())
                    .withDevice(getThisDevice())
                    .withUser(User.builder()
                            .withId(getLoggedInUserId())
                            .build())
                    .build())
            .withEventDetail(EventDetail.builder()
                    .withTypeId(typeId)
                    .withDescription(description)
                    .withPurpose(purpose != null ? purpose : getSessionPurpose())
                    .withEventAction(eventAction)
                    .build())
            .build();
    }

}

// Create the logging service
final EventLoggingService eventLoggingService = new CustomEventLoggingService();

// Log some work that produces a result. This will make use of createEvent for common event values.
final UserAccount userAccount = eventLoggingService.loggedResult(
    "LOGON",
    "User " + userId + " logged on",
    AuthenticateEventAction.builder()
        .withAction(AuthenticateAction.LOGON)
        .withUser(User.builder()
                .withId(userId)
                .build())
        .build()
    () -> {
        // Perform authentication and logon

        // An exception here will cause an unsuccessful logon event to be logged
        // then the original exception will be re-thrown.

        return account;
    });
```

The various forms of the `loggedXXX` methods provide the simplest means of logging an event and handle failure cases for you, setting Outcome/Success to false if an exception is thrown.
Some forms of `loggedXXX` allow the loggedWork lambda to return a LoggedOutcome object to indicate success/failure rather than using exceptions.
There are also forms of these methods that allow you to change the logged EventAction based on the work being performed, e.g. if you need to add in the details of the results of a search.
For examples of the various forms see [App.java](./example-logged-application/src/main/java/event/logging/example/App.java)

For instances where you want to handle failure conditions manually you can use this approach:

```java

eventLoggingService.log(
        "LogoffNowBanner",
        "User shown logoff now banner",
        ViewEventAction.builder()
                .addBanner(Banner.builder()
                        .withMessage("The system is about to be shutdown for maintenance, log off now!")
                        .build())
                .build());
```

When building the Event model you can use the fully fluent style (using the `end()` methods on the `Builder` classes) to build the event.
While more compact, this is heavily reliant on careful indentation to ensure readability.

```java
// Create the event object
final Event event = Event.builder()
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
```

_event-logging_ is used by [_Stroom_](https://github.com/gchq/stroom).
You can see how it is used in Stroom here: [`StroomEventLoggingServiceImpl`](https://github.com/gchq/stroom/blob/master/stroom-event-logging/stroom-event-logging-impl/src/main/java/stroom/event/logging/impl/StroomEventLoggingServiceImpl.java)


## Upgrading from v3/4 to v5

v5 of this library introduces a number of breaking changes that will require you to alter your existing event creation code if migrating to v5.
We appreciate that introducing breaking changes causes a lot of pain and we don't do it lightly.
Unfortunately there were a number of fundamental issues with the previous versions of the library that made it difficult to work with.


### Fluent Builders

_event-logging_ v5 has introduced fluent style builder classes and methods for each class in the `Event` model.
These make creating events or sub-trees within events much easier and neater.
There is however no requirement to change existing code to use the builders.

### Moved Classes

For clarity all classes have been made top-level.
Previously a lot of the classes were declared as static inner classes.
Where this has happened, e.g. `event.logging.Event.EventDetail` => `event.logging.EventDetail`, it should just be case of fixing the import or removing the extra parent class from the qualifier.


### Renamed Classes

The following classes have been renamed:

* `Event.EventDetail.Alert` => `AlertEventAction`
* `Event.EventDetail.Approval` => `ApprovalEventAction`
* `Event.EventDetail.Authenticate` => `AuthenticateEventAction`
* `Event.EventDetail.Authorise` => `AuthoriseEventAction`
* `Event.EventDetail.Authorise` => `AuthoriseEventAction`
* `CopyMove` => `CopyEventAction` & `MoveEventAction`
* `Export` => `ExportEventAction`
* `Import` => `ImportEventAction`
* `Install` => `InstallEventAction` & `InstallEventAction`
* `Event.EventDetail.Network` => `NetworkEventAction`
* `NetworkSrcDst` => `NetworkLocation`
* `NetworkSrcDstTrasnportProtocol` => `NetworkProtocol`
* `NetworkSrcDstTrasnportProtocol` => `NetworkProtocol`
* `Object` => `OtherObject`
* `Event.EventDetail.Print` => `PrintEventAction`
* `Event.EventDetail.Process` => `ProcessEventAction`
* `SendReceive` => `SendEventAction` & `ReceiveEventAction`
* `Search` => `SearchEventAction`
* `Unknown` => `UnknownEventAction`
* `Event.EventDetail.Update` => `UpdateEventAction`


### Type changes

Some properties have had their classes changed for clarity:

* `ObjectOutcome Event.EventDetail.create` => `CreateEventAction EventDetail.create`
* `ObjectOutcome Event.EventDetail.delete` => `DeleteEventAction EventDetail.delete`
* `ObjectOutcome Event.EventDetail.search` => `SearchEventAction EventDetail.search`
* `CopyMove Event.EventDetail.copy` => `CopyEventAction EventDetail.copy`
* `CopyMove Event.EventDetail.move` => `MoveEventAction EventDetail.move`
* `Install Event.EventDetail.uninstall` => `UninstallEventAction EventDetail.uninstall`
* `ObjectOutcome Event.EventDetail.view` => `ViewEventAction EventDetail.view`


### Removed Classes

A number of deprecated classes and properties have been removed:

* `Event.EventDetail.antiMalware`
* `AntiMalware`
* `BaseAdvancedQueryItemComplexType` - replaced with marker interface `AdvancedQueryItem`
* `SearchResult` - use `SearchResults` instead.
* `Event.EventDetail.classification` - use `Event.classification` instead.
* `From`


### New Interfaces

A number of new marker interfaces have been added to make the API clearer.
For example all the possible event actions (e.g. send, create, delete, etc.) under `EventDetail` now implement the marker interface `EventAction`.


## Developing this library

The documentation for developers contributing to this library see [here](./docs/developing_this_lib.md).
