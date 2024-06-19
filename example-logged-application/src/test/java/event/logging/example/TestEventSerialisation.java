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
}
