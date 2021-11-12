package event.logging.example;

import event.logging.EventLoggingService;
import event.logging.impl.MockEventLoggingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class TestAppService {

    @Test
    void logoffUser() {
        final EventLoggingService eventLoggingService = new MockEventLoggingService();
        final UserContext userContext = new UserContext();
        userContext.setUserId("jbloggs");
        userContext.setJustification("Just because.");
        final AppService appService = new AppService(eventLoggingService, userContext);

        appService.logoffUser();
        Assertions.assertThat(userContext.getUserId())
                .isNull();
    }

    @Test
    void performSearch() {
        final EventLoggingService eventLoggingService = new MockEventLoggingService();
        final UserContext userContext = new UserContext();
        userContext.setUserId("jbloggs");
        userContext.setJustification("Just because.");
        final AppService appService = new AppService(eventLoggingService, userContext);

        final List<String> methodNames = appService.performSearch();

        Assertions.assertThat(methodNames)
                .isNotEmpty();
    }
}
