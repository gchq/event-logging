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
import event.logging.AuthenticateEventAction;
import event.logging.Device;
import event.logging.Event;
import event.logging.EventDetail;
import event.logging.EventSource;
import event.logging.EventTime;
import event.logging.SystemDetail;
import event.logging.User;
import event.logging.base.EventLoggingService;
import event.logging.base.util.DeviceUtil;
import event.logging.base.util.EventLoggingUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class TestEventVersion {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestEventVersion.class);


    @BeforeAll
    public static void setupCustomLogAppender() {
        System.setProperty(EventLoggingService.PROP_KEY_LOG_RECEIVER_CLASS, "event.logging.base.impl.TestLogReceiver");
    }

    @AfterAll
    public static void removeCustomLogAppender() {
        System.setProperty(EventLoggingService.PROP_KEY_LOG_RECEIVER_CLASS, "event.logging.impl.LoggerLogReceiver");
    }

    @Test
    public void testNullVersion() {
        System.setProperty(EventLoggingService.PROP_KEY_VALIDATE, Boolean.TRUE.toString());

        EventLoggingService eventLoggingService = new DefaultEventLoggingService();

        final Event event = createBasicEvent(eventLoggingService, "LOGIN", "LOGIN");

        event.setVersion(null);
        eventLoggingService.log(event);

        final String message = TestLogReceiver.getCurrentMessage();
//        LOGGER.info("message:\n{}", message);
        Assertions.assertThat(message)
                .contains("Version=\"" + BuildInfo.getSchemaVersion() +"\"");
    }

    @Test
    public void testBlankVersion() {
        System.setProperty(EventLoggingService.PROP_KEY_VALIDATE, Boolean.TRUE.toString());

        EventLoggingService eventLoggingService = new DefaultEventLoggingService();

        final Event event = createBasicEvent(eventLoggingService, "LOGIN", "LOGIN");

        event.setVersion("  ");
        eventLoggingService.log(event);

        final String message = TestLogReceiver.getCurrentMessage();
//        LOGGER.info("message:\n{}", message);
        Assertions.assertThat(message)
                .contains("Version=\"" + BuildInfo.getSchemaVersion() +"\"");
    }

    @Test
    public void testInvalidVersion() {
        System.setProperty(EventLoggingService.PROP_KEY_VALIDATE, Boolean.TRUE.toString());

        EventLoggingService eventLoggingService = new DefaultEventLoggingService();

        final Event event = createBasicEvent(eventLoggingService, "LOGIN", "LOGIN");

        event.setVersion("foo");
        eventLoggingService.log(event);

        final String message = TestLogReceiver.getCurrentMessage();
//        LOGGER.info("message:\n{}", message);
        Assertions.assertThat(message)
                .contains("Version=\"" + BuildInfo.getSchemaVersion() +"\"");
    }

    @Test
    public void testValidVersion() {
        System.setProperty(EventLoggingService.PROP_KEY_VALIDATE, Boolean.TRUE.toString());

        EventLoggingService eventLoggingService = new DefaultEventLoggingService();

        final Event event = createBasicEvent(eventLoggingService, "LOGIN", "LOGIN");

        event.setVersion(BuildInfo.getSchemaVersion());
        eventLoggingService.log(event);

        final String message = TestLogReceiver.getCurrentMessage();
//        LOGGER.info("message:\n{}", message);
        Assertions.assertThat(message)
                .contains("Version=\"" + BuildInfo.getSchemaVersion() +"\"");
    }

    private Event createBasicEvent(final EventLoggingService eventLoggingService,
                                   final String typeId,
                                   final String description) {

        final EventTime eventTime = EventLoggingUtil.createCurrentEventTime();
        final Device device = DeviceUtil.createDevice(null, "123.123.123.123");
        final User user = EventLoggingUtil.createUser("someuser");

        final SystemDetail system = new SystemDetail();
        system.setName("Test System");
        system.setEnvironment("Test");

        final EventSource eventSource = new EventSource();
        eventSource.setSystem(system);
        eventSource.setGenerator("JUnit");
        eventSource.setDevice(device);
        eventSource.setUser(user);

        final EventDetail eventDetail = new EventDetail();
        eventDetail.setTypeId(typeId);
        eventDetail.setDescription(description);

        final Event event = eventLoggingService.createEvent();

        event.setEventTime(eventTime);
        event.setEventSource(eventSource);
        event.setEventDetail(eventDetail);

        final User authUser = new User();
        authUser.setId("someuser");

        final AuthenticateEventAction authenticateEventAction = new AuthenticateEventAction();
        authenticateEventAction.setAction(AuthenticateAction.LOGON);
        authenticateEventAction.setAuthenticationEntity(authUser);

        event.getEventDetail().setEventAction(authenticateEventAction);
        event.getEventTime().setTimeCreated(Instant.now());

        return event;
    }
}
