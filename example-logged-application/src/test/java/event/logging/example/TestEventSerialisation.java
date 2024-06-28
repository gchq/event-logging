package event.logging.example;

import event.logging.AuthenticateAction;
import event.logging.AuthenticateEventAction;
import event.logging.AuthenticateLogonType;
import event.logging.EventLoggingService;
import event.logging.User;
import event.logging.impl.DefaultEventLoggingService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEventSerialisation {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestEventSerialisation.class);

    @Test
    void test() {
        System.setProperty(EventLoggingService.PROP_KEY_VALIDATE, "true");
        EventLoggingService eventLoggingService = new DefaultEventLoggingService();

        final String userId = "bob";

        // Make sure that event serialisation works, e.g. all the xml deps are in place
        // We aren't asserting anything other than it not falling over.
        eventLoggingService.loggedWorkBuilder()
                .withTypeId("login")
                .withDescription("User " + userId + " logged in")
                .withDefaultEventAction(AuthenticateEventAction.builder()
                        .withUser(User.builder()
                                .withId(userId)
                                .build())
                        .withAction(AuthenticateAction.LOGON)
                        .withLogonType(AuthenticateLogonType.INTERACTIVE)
                        .build())
                .withSimpleLoggedAction(() -> {
                    LOGGER.info("Doing logon");
                })
                .runActionAndLog();
    }

//    private static class MyEventLoggingService extends DefaultEventLoggingService {
//
//        @Override
//        public Event createEvent(final String typeId, final String description, final Purpose purpose, final EventAction eventAction) {
//            return Event.builder()
//                    .withEventTime(EventTime.builder()
//                            .withTimeCreated(Instant.now())
//                            .build())
//                    .withEventSource(EventSource.builder()
//                            .withSystem(SystemDetail.builder()
//                                    .withName("MY_SYSTEM")
//                                    .withEnvironment("DEV")
//                                    .withVersion("1.2.3")
//                                    .build())
//                            .withGenerator(GENERATOR)
//                            .withDevice(device)
//                            .withClient(client)
//                            .withUser(getUser())
//                            .withRunAs(getRunAsUser())
//                            .build())
//                    .withEventDetail(EventDetail.builder()
//                            .withTypeId(typeId)
//                            .withDescription(description)
//                            .withPurpose(purpose)
//                            .withEventAction(eventAction)
//                            .build())
//                    .build();
//        }
//    }
}
