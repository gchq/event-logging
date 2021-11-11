package event.logging.base.impl;

import event.logging.EventAction;
import event.logging.base.ComplexLoggedOutcome;
import event.logging.base.ComplexLoggedRunnable;
import event.logging.base.ComplexLoggedSupplier;
import event.logging.base.EventLoggerBasicBuilder;
import event.logging.base.EventLoggingService;

import java.util.function.Supplier;

public class MockEventLoggerBasicBuilder<T_EVENT_ACTION extends EventAction>
        extends EventLoggerBasicBuilderImpl<T_EVENT_ACTION> {

    MockEventLoggerBasicBuilder(final EventLoggingService eventLoggingService) {
        super(eventLoggingService);
    }

    @Override
    public <T_RESULT> EventLoggerBasicBuilder.EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withSimpleLoggedAction(
            final Runnable loggedAction) {

        return new MockEventLoggerLoggedActionBuilder<>(
                this,
                ComplexLoggedOutcome::success);
    }

    @Override
    public <T_RESULT> EventLoggerBasicBuilder.EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withComplexLoggedAction(
            final ComplexLoggedRunnable<T_EVENT_ACTION> loggedAction) {

        return new MockEventLoggerLoggedActionBuilder<>(this, loggedAction);
    }

    @Override
    public <T_RESULT> EventLoggerBasicBuilder.EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withSimpleLoggedResult(
            final Supplier<T_RESULT> loggedWork) {

        return new MockEventLoggerLoggedResultBuilder<>(
                this,
                eventAction ->
                        ComplexLoggedOutcome.success(loggedWork.get(), eventAction));
    }

    @Override
    public <T_RESULT> EventLoggerBasicBuilder.EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withComplexLoggedResult(
            final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedWork) {

        return new MockEventLoggerLoggedResultBuilder<>(this, loggedWork);
    }

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


public static class MockEventLoggerLoggedActionBuilder<T_EVENT_ACTION extends EventAction>
        extends EventLoggerLoggedActionBuilderImpl<T_EVENT_ACTION> {

    private final MockEventLoggerBasicBuilder<T_EVENT_ACTION> mockEventLoggerBasicBuilder;

    public MockEventLoggerLoggedActionBuilder(
            final MockEventLoggerBasicBuilder<T_EVENT_ACTION> mockEventLoggerBasicBuilder,
            final ComplexLoggedRunnable<T_EVENT_ACTION> loggedAction) {
        super(mockEventLoggerBasicBuilder, loggedAction);

        this.mockEventLoggerBasicBuilder = mockEventLoggerBasicBuilder;
    }

    @Override
    public void runActionAndLog() {
        getLoggedAction().run(mockEventLoggerBasicBuilder.getEventAction());
    }
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public static class MockEventLoggerLoggedResultBuilder<T_EVENT_ACTION extends EventAction, T_RESULT>
        extends EventLoggerLoggedResultBuilderImpl<T_EVENT_ACTION, T_RESULT> {

    private final MockEventLoggerBasicBuilder<T_EVENT_ACTION> mockEventLoggerBasicBuilder;

    public MockEventLoggerLoggedResultBuilder(
            final MockEventLoggerBasicBuilder<T_EVENT_ACTION> mockEventLoggerBasicBuilder,
            final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedWork) {
        super(mockEventLoggerBasicBuilder, loggedWork);

        this.mockEventLoggerBasicBuilder = mockEventLoggerBasicBuilder;
    }

    @Override
    public T_RESULT getResultAndLog() {
        return getLoggedWork()
                .get(mockEventLoggerBasicBuilder.getEventAction())
                .getResult();
    }
}
}
