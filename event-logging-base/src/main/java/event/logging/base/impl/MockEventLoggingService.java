package event.logging.base.impl;

import event.logging.Event;
import event.logging.EventDetail;
import event.logging.base.EventLoggerBuilder;
import event.logging.base.EventLoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A mock implementation of {@link EventLoggingService} that can be used for unit test
 * code that uses {@link EventLoggingService} and needs a simple implementation that does not
 * log.
 */
@SuppressWarnings("unused")
public class MockEventLoggingService implements EventLoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockEventLoggingService.class);

    @Override
    public void log(final Event event) {
        if (LOGGER.isDebugEnabled()) {
            debugEvent(event);
        }
    }

    @Override
    public EventLoggerBuilder.TypeIdStep loggedWorkBuilder() {

        //noinspection rawtypes - Can't know the type at this point, will later
        return new MockEventLoggerBuilder(this);
    }

    @Override
    public void setValidate(final Boolean validate) {

    }

    @Override
    public boolean isValidate() {
        return false;
    }

    private void debugEvent(final Event event) {
        String typeId = null;
        String description = null;
        String eventActionName = null;

        if (event != null && event.getEventDetail() != null) {
            final EventDetail eventDetail = event.getEventDetail();
            typeId = eventDetail.getTypeId();
            description = eventDetail.getDescription();
            eventActionName = eventDetail.getEventAction().getClass().getSimpleName();
        }

        final String info = String.join("typeId: '", typeId,
                "' description: '", description,
                "' eventActionName: '", eventActionName);

        LOGGER.debug("log() called for event - " + info);
    }
}
