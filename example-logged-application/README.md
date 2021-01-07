# Example Logged Application

This directory contains a standalone java _main()_ application that serves as an example of how to integerate event-logging into your own applicaton.
This project uses Gradle as the build tool and SLF4J/Logback for the logging however if you are using Maven or another logger implementation it should not be too difficult to see how you can apply the example using those technologies.

## Files of note

### `build.gradle`

The Gradle build file that:
* sets up the _stroom/event-logging_ Bintray repository
* sets the dependency to _event-logging_

### `src/java/event/logging/example/CustomEventLoggingService.java`

You will likely want to create a class like this that provides the common values for your logged events by overriding the method:

```java

    @Override
    public Event createEvent(final String typeId,
                             final String description,
                             final Purpose purpose,
                             final EventAction eventAction) {
        return Event.builder()

                ...

                .build();
    }
```

These values will be specific to your application, such as the system name or the mechanism for getting them will be specific to your application, e.g. how you get the ID of the logged in user.

### `src/main/java/event/logging/example/UserContext.java`

This is a very simplistic example of holding the user ID and justification at a kind of session scope.
How you manage capturing the logged in user or their justification is up to you and your application framework.

### `src/main/java/event/logging/example/App.java`

This is the simplistic example application to demonstrate some of the methods of logging events for user actions.
It is a simple console based application that prompts for input in order to demostart some typical use cases such as logon, logoff, search etc.


### `src/main/resource/logback.xml`

This is the configuratioin file for the Logback logger that implements SLF4J.
_event-logging_ uses SLF4J by default for outputting the generated and serialised XML events.
This configuration file defines a specific appender for the `event-logger` logger.
`additivity` would typically be set to false to prevent the log events also being logged to the root logger.

Typically as in this file a rolling file appender is used, with files rolling on a minute basis.
Once a file has rolled it can be sent to stroom, stroom-proxy or some other log collection service.
The event logs are written to files in the `logs` directory.

## Running the application

To run the application in the absence of an IDE you will need Java8 installed.
The following command will run it.

```sh
./gradlew --console=plain run
```
By supplying no username when prompted, for example, you can see how failure conditions are logged differently.


