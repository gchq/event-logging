# Event Logging

_Event Logging_ is a Java JAXB implementation of the _Event Logging XML Schema_ and an API for logging events conforming to the _Event Logging XML Schema_. _Event Logging_ can be incorporated into your application to provide a means of recording and outputting audit events.

## Generation of the JAXB artefacts

The JAXB artefacts are generated using the _xjc_ binary that ships with the Java JDK. This parses the XML Schema and builds a set of classes based on the schema. Prior to running _xjc_ the schema undergoes an automated tidy up process to rename many of the elements to improve the class names in the JAXB model. Also it will apply all XSLT stylesheets found in _generator/src/main/resources/translations/_ to the _Event Logging XML Schema_. Currently the only stylesheet in use adds Event as a root element to the schema.

The generation process is reliant on having the required version of the _Event Logging_ XML schema in the directory _generator_.  Once this is in place it is simply a matter of running a Maven build.  The Maven build will generate the JAXB artefacts and go onto build the API jars.

The _Event Logging_ XML schema is authored in [github.com/gchq/event-logging-schema](https://github.com/gchq/event-logging-schema). The _Event Logging XML Schema_ in _generator_ should never be edited directly. It should always be a copy of the desired version from _event-loggin-schema_.

## Building the _Event Logging_ API jar

The API jar is built using the Maven root pom. This will generate the JAXB artefacts, as well as copying the API classes, test classes and XML schema from the base module into the event-logging module.

All files under event-logging/src are transient and will be generated as part of the Maven build.

The build is run as follows:

`mvn clean install -U`

## Using the _Event Logging_ API

The interface for logging audit events is _LoggingEventsService.java_. A default implementation is included in the form of _DefaultEventLoggingService.java_. This simple implementation writes the serialized events out to a Log4J appender.

Examples of how to construct various types of events and log them can be found in the test class _base/src/test/java/event/logging/EventLoggingServiceIT.java_.

The following is a very simple example of logging an _Authentication_ type event.

``` java 
//Create the logging service, choosing how to handle
final EventLoggingService eventLoggingService = new DefaultEventLoggingService();

final User user = new User();
user.setId("someuser");

final Event.EventDetail.Authenticate authenticate = new Event.EventDetail.Authenticate();
authenticate.setAction(AuthenticateAction.LOGON);
authenticate.setUser(user);

final Event.EventTime eventTime = EventLoggingUtil.createEventTime(new Date());
final Device device = DeviceUtil.createDevice(null, "123.123.123.123");
final User user = EventLoggingUtil.createUser("someuser");

final System system = new System();
system.setName("Test System");
system.setEnvironment("Test");

final Event.EventSource eventSource = new Event.EventSource();
eventSource.setSystem(system);
eventSource.setGenerator("JUnit");
eventSource.setDevice(device);
eventSource.setUser(user);

final Event.EventDetail eventDetail = new Event.EventDetail();
eventDetail.setTypeId("LOGON");
eventDetail.setDescription("A user logon");
eventDetail.setAuthenticate(authenticate);

final Event event = eventLoggingService.createEvent();
event.setEventTime(eventTime);
event.setEventSource(eventSource);
event.setEventDetail(eventDetail);

eventLoggingService.log(event);
```

