package event.logging.base.impl;

import event.logging.EventAction;
import event.logging.base.ComplexLoggedOutcome;
import event.logging.base.EventLoggerBuilder;
import event.logging.base.EventLoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public class MockEventLoggerBuilder<T_EVENT_ACTION extends EventAction>
        extends EventLoggerBuilderImpl<T_EVENT_ACTION> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockEventLoggerBuilder.class);

    public MockEventLoggerBuilder(final EventLoggingService eventLoggingService) {
        super(eventLoggingService);
    }

    @Override
    public EventLoggerBuilder.ActionSubBuilder<T_EVENT_ACTION> withSimpleLoggedAction(
            final Runnable loggedAction) {

        return new MockActionSubBuilder<>(
                this,
                eventAction -> {
                    loggedAction.run();
                    return ComplexLoggedOutcome.success(eventAction);
                });
    }

    @Override
    public EventLoggerBuilder.ActionSubBuilder<T_EVENT_ACTION> withComplexLoggedAction(
            final Function<T_EVENT_ACTION, ComplexLoggedOutcome<Void, T_EVENT_ACTION>> loggedAction) {

        return new MockActionSubBuilder<>(this, loggedAction);
    }

    @Override
    public <T_RESULT> EventLoggerBuilder.ResultSubBuilder<T_EVENT_ACTION, T_RESULT> withSimpleLoggedResult(
            final Supplier<T_RESULT> loggedWork) {

        return new MockResultSubBuilder<>(
                this,
                eventAction ->
                        ComplexLoggedOutcome.success(loggedWork.get(), eventAction));
    }

    @Override
    public <T_RESULT> EventLoggerBuilder.ResultSubBuilder<T_EVENT_ACTION, T_RESULT> withComplexLoggedResult(
            final Function<T_EVENT_ACTION, ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION>> loggedWork) {

        return new MockResultSubBuilder<>(this, loggedWork);
    }

    private void debugEvent(final String methodName) {
        final String typeId = getTypeId();
        final String description = getDescription();
        final String eventActionName = getEventAction().getClass().getSimpleName();

        final String info = String.join("typeId: '", typeId,
                "' description: '", description,
                "' eventActionName: '", eventActionName);

        LOGGER.debug(methodName + "() called for event - " + info);
    }

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public static class MockActionSubBuilder<T_EVENT_ACTION extends EventAction>
            extends ActionSubBuilderImpl<T_EVENT_ACTION> {

        private static final Logger LOGGER = LoggerFactory.getLogger(MockActionSubBuilder.class);

        private final MockEventLoggerBuilder<T_EVENT_ACTION> mockEventLoggerBuilder;

        private MockActionSubBuilder(
                final MockEventLoggerBuilder<T_EVENT_ACTION> mockEventLoggerBuilder,
                final Function<T_EVENT_ACTION, ComplexLoggedOutcome<Void, T_EVENT_ACTION>> loggedAction) {
            super(mockEventLoggerBuilder, loggedAction);

            this.mockEventLoggerBuilder = mockEventLoggerBuilder;
        }

        @Override
        public void runActionAndLog() {
            if (LOGGER.isDebugEnabled()) {
                mockEventLoggerBuilder.debugEvent("runActionAndLog");
            }
            getLoggedAction().apply(mockEventLoggerBuilder.getEventAction());
        }
    }

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static class MockResultSubBuilder<T_EVENT_ACTION extends EventAction, T_RESULT>
            extends ResultSubBuilderImpl<T_EVENT_ACTION, T_RESULT> {

        private static final Logger LOGGER = LoggerFactory.getLogger(MockResultSubBuilder.class);

        private final MockEventLoggerBuilder<T_EVENT_ACTION> mockEventLoggerBuilder;

        private MockResultSubBuilder(
                final MockEventLoggerBuilder<T_EVENT_ACTION> mockEventLoggerBuilder,
                final Function<T_EVENT_ACTION, ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION>> loggedWork) {
            super(mockEventLoggerBuilder, loggedWork);

            this.mockEventLoggerBuilder = mockEventLoggerBuilder;
        }

        @Override
        public T_RESULT getResultAndLog() {
            if (LOGGER.isDebugEnabled()) {
                mockEventLoggerBuilder.debugEvent("getResultAndLog");
            }

            return getLoggedWork()
                    .apply(mockEventLoggerBuilder.getEventAction())
                    .getResult();
        }
    }

}
