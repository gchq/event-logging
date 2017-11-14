# Event Logging

_Event Logging_ is a Java JAXB implementation of the _Event Logging XML Schema_ and an API for logging events conforming to the _Event Logging XML Schema_. _Event Logging_ can be incorporated into your Java application to provide a means of recording and outputting audit events.

## Generation of the JAXB artefacts

The JAXB artefacts are generated using the _xjc_ binary that ships with the Java JDK. This parses the XML Schema and builds a set of classes based on the schema. Prior to running _xjc_ the schema undergoes an automated tidy up process to rename many of the elements to improve the class names in the JAXB model. 

The generation process is reliant on having the required version of the _Event Logging_ XML schema in the directory _event-logging-generator/schema/_.  The Gradle build will download the schema from GitHub as long as the following line in the root _build.gradle_ file has been set to the correct schema version. 

```
def eventLoggingSchemaVer = "v3.1.2"
```

The Gradle build will generate the JAXB artefacts and go onto build the API jar.

The _Event Logging_ XML schema is authored in [github.com/gchq/event-logging-schema](https://github.com/gchq/event-logging-schema). The _Event Logging XML Schema_ in _event-logging-generator/schema/_ should never be edited directly. It should always be a copy of the desired version from _event-loggin-schema_.

## Building the _Event Logging_ API jar

The API jar is built using Gradle. This will generate the JAXB artefacts, as well as copying the API classes, test classes and XML schema from the base module into the event-logging-api module.

All files under event-logging-api/src are transient and will be generated or copied in as part of the Gradle build.

The build is run as follows:

`./gradlew clean build`

Towards the end of the build process, it will download the sources jar for the latest release of _event-logging_ from GitHub and compare the Java source files in it to those just built. This provides a quick way of seeing the impact on the API from any changes in the schema.  For example some schema changes that would not be a breaking change as far as an XML document is concerned (e.g. a rename of a complex type), would become a breaking change in the JAXB classes.

## Using the _Event Logging_ API

The interface for logging audit events is _LoggingEventsService.java_. A default implementation is included in the form of _DefaultEventLoggingService.java_. This simple implementation writes the serialized events out to a Log4J appender.

Examples of how to construct various types of events and log them can be found in the test class _base/src/test/java/event/logging/EventLoggingServiceIT.java_.

The following is a very simple example of logging an _Authentication_ type event.

``` java 
//Create the logging service
final EventLoggingService eventLoggingService = new DefaultEventLoggingService();

//Create the system that is logging the authenticat event
final System system = new System();
system.setName("Test System");
system.setEnvironment("Test");

//Describe the device the event occurred on 
final Device device = DeviceUtil.createDevice(null, "123.123.123.123");

//Create the user involved in the authenticate action (possibly different from
//the eventSource user)
final User user = EventLoggingUtil.createUser("someuser");

//Provide details of where the event came from
final Event.EventSource eventSource = new Event.EventSource();
eventSource.setSystem(system);
eventSource.setGenerator("JUnit");
eventSource.setDevice(device);
eventSource.setUser(user);

//Create the authenticate object to describe the authentication specific details
final Event.EventDetail.Authenticate authenticate = new Event.EventDetail.Authenticate();
authenticate.setAction(AuthenticateAction.LOGON);
authenticate.setUser(user);

//Create the detail of what happened
//TypeId is typically a system specific code that is unique to a use case in that system
final Event.EventDetail eventDetail = new Event.EventDetail();
eventDetail.setTypeId("LOGON");
eventDetail.setDescription("A user logon");
eventDetail.setAuthenticate(authenticate);

//Define the time the event happened
final Event.EventTime eventTime = EventLoggingUtil.createEventTime(new Date());

//Combine the sub-objects together
final Event event = eventLoggingService.createEvent();
event.setEventTime(eventTime);
event.setEventSource(eventSource);
event.setEventDetail(eventDetail);

//Send the event
eventLoggingService.log(event);
```

