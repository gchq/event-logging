/*
 * Copyright 2018 Crown Copyright
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
package event.logging.base.impl;

import event.logging.AuthenticateAction;
import event.logging.Device;
import event.logging.Event;
import event.logging.System;
import event.logging.User;
import event.logging.base.EventLoggingService;
import event.logging.base.util.DeviceUtil;
import event.logging.base.util.EventLoggingUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class TestValidation {

//    static {
//        BasicConfigurator.configure();
//    }

    private static final String VALIDATE_PROP_KEY = "event.logging.validate";

    //    public static final String JUNIT_CONSOLE_APPENDER = "JUNIT_CONSOLE_APPENDER";
//
    private static final Logger LOGGER = LoggerFactory.getLogger(TestValidation.class);
//
//    private WriterAppender appender = null;
//    private StringWriter consoleWriter;


    @BeforeAll
    public void setupCustomLogAppender() {
        java.lang.System.setProperty("event.logging.logreceiver", "TestLogReceiver");


//        consoleWriter = new StringWriter();
//        appender = new WriterAppender(new PatternLayout("%d{ISO8601} %p - %m%n"), consoleWriter);
//        appender.setName(JUNIT_CONSOLE_APPENDER);
//        appender.setThreshold(org.apache.logger.Level.ERROR);
//
//        Logger validatorLogger = LoggerFactory.getLogger(DefaultXMLValidator.class);
//        validatorLogger.addAppender(appender);
//
//        Logger errorHandlerLogger = LoggerFactory.getLogger(LoggingErrorHandler.class);
//        errorHandlerLogger.addAppender(appender);

    }

    @AfterAll
    public void removeCustomLogAppender() {
//        LoggerFactory.getLogger(DefaultXMLValidator.class).removeAppender(JUNIT_CONSOLE_APPENDER);

        java.lang.System.setProperty("event.logging.logreceiver", "LoggerLogReceiver");
    }

    @Test
    public void testSchemaExists() {
        EventLoggingService eventLoggingService = new event.logging.base.impl.DefaultEventLoggingService();

        // Error is logged if schema cannot be found on classpath
        assertThat(event.logging.base.impl.TestLogReceiver.getCurrentMessage().isEmpty()).isTrue();
    }

    @Test
    public void testValidationOffBySystemProperty() {
        java.lang.System.setProperty(VALIDATE_PROP_KEY, Boolean.FALSE.toString());

        EventLoggingService eventLoggingService = new event.logging.base.impl.DefaultEventLoggingService();

        eventLoggingService.log(createBasicEvent(eventLoggingService, "LOGIN", "LOGIN", EventValidity.INVALID));

        assertThat(event.logging.base.impl.TestLogReceiver.getCurrentMessage().isEmpty()).isTrue();
    }

    @Test
    public void testValidationOnBySystemProperty() {

        java.lang.System.setProperty(VALIDATE_PROP_KEY, Boolean.TRUE.toString());

        EventLoggingService eventLoggingService = new event.logging.base.impl.DefaultEventLoggingService();

        eventLoggingService.log(createBasicEvent(eventLoggingService, "LOGIN", "LOGIN", EventValidity.INVALID));

        LOGGER.info("Expecting an ERROR in the log for this test");
        assertThat(event.logging.base.impl.TestLogReceiver.getCurrentMessage().isEmpty()).isFalse();
    }

    @Test
    public void testValidationOnByMethod() {
        java.lang.System.setProperty(VALIDATE_PROP_KEY, Boolean.FALSE.toString());

        EventLoggingService eventLoggingService = new event.logging.base.impl.DefaultEventLoggingService();

        eventLoggingService.setValidate(Boolean.TRUE);

        eventLoggingService.log(createBasicEvent(eventLoggingService, "LOGIN", "LOGIN", EventValidity.INVALID));

        LOGGER.info("Expecting an ERROR in the log for this test");
        assertThat(event.logging.base.impl.TestLogReceiver.getCurrentMessage().isEmpty()).isFalse();
    }

    @Test
    public void testValidationOffByMethod() {
        java.lang.System.setProperty(VALIDATE_PROP_KEY, Boolean.TRUE.toString());

        EventLoggingService eventLoggingService = new event.logging.base.impl.DefaultEventLoggingService();

        eventLoggingService.setValidate(Boolean.FALSE);

        eventLoggingService.log(createBasicEvent(eventLoggingService, "LOGIN", "LOGIN", EventValidity.INVALID));

        assertThat(event.logging.base.impl.TestLogReceiver.getCurrentMessage().isEmpty()).isFalse();
    }

    @Test
    public void testValidationPassed() {
        java.lang.System.setProperty(VALIDATE_PROP_KEY, Boolean.TRUE.toString());

        EventLoggingService eventLoggingService = new DefaultEventLoggingService();

        eventLoggingService.log(createBasicEvent(eventLoggingService, "LOGIN", "LOGIN", EventValidity.VALID));

        assertThat(TestLogReceiver.getCurrentMessage().isEmpty()).isFalse();
    }

    private Event createBasicEvent(final EventLoggingService eventLoggingService, final String typeId,
                                   final String description, final EventValidity eventValidState) {

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
        eventDetail.setTypeId(typeId);
        eventDetail.setDescription(description);

        final Event event = eventLoggingService.createEvent();

        if (eventValidState.equals(EventValidity.VALID)) {
            event.setEventTime(eventTime);
            event.setEventSource(eventSource);
            event.setEventDetail(eventDetail);

            final User authUser = new User();
            authUser.setId("someuser");

            final Event.EventDetail.Authenticate authenticate = new Event.EventDetail.Authenticate();
            authenticate.setAction(AuthenticateAction.LOGON);
            authenticate.setUser(authUser);

            event.getEventDetail().setAuthenticate(authenticate);
            event.getEventTime().setTimeCreated(new Date());
        }

        return event;
    }

    private enum EventValidity {
        VALID, INVALID;
    }
}
