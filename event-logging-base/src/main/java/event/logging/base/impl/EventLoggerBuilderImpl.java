package event.logging.base.impl;

import event.logging.BaseOutcome;
import event.logging.Event;
import event.logging.EventAction;
import event.logging.HasOutcome;
import event.logging.Purpose;
import event.logging.base.ComplexLoggedOutcome;
import event.logging.base.EventLoggerBuilder;
import event.logging.base.EventLoggingService;
import event.logging.base.LoggedWorkExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class EventLoggerBuilderImpl<T_EVENT_ACTION extends EventAction> implements
        EventLoggerBuilder.TypeIdStep,
        EventLoggerBuilder.DescriptionStep,
        EventLoggerBuilder.EventActionStep,
        EventLoggerBuilder.WorkStep<T_EVENT_ACTION> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggerBuilderImpl.class);

    // For each event action hold a function to create the appropriate subclass of BaseOutcome
    private static final Map<Class<? extends EventAction>, Optional<Function<EventAction, BaseOutcome>>>
            OUTCOME_FACTORY_MAP = new ConcurrentHashMap<>();

    private final EventLoggingService eventLoggingService;

    private String eventTypeId;
    private String description;
    private T_EVENT_ACTION eventAction;
    private Purpose purpose;
    private boolean isLogEventRequired = true;
    private LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler;

    public EventLoggerBuilderImpl(final EventLoggingService eventLoggingService) {
        this.eventLoggingService = eventLoggingService;
    }

    @Override
    public EventLoggerBuilder.DescriptionStep withTypeId(final String typeId) {
        this.eventTypeId = typeId;
        return this;
    }

    String getTypeId() {
        return eventTypeId;
    }

    @Override
    public EventLoggerBuilder.EventActionStep withDescription(final String description) {
        this.description = description;
        return this;
    }

    String getDescription() {
        return description;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EventAction> EventLoggerBuilder.WorkStep<T> withDefaultEventAction(
            final T defaultEventAction) {

        // At this point we are moving from the builder having unknown type to it having a known
        // EventAction so type casts are unavoidable.

        this.eventAction = (T_EVENT_ACTION) defaultEventAction;
        return (EventLoggerBuilder.WorkStep<T>) this;
    }

    T_EVENT_ACTION getEventAction() {
        return eventAction;
    }

    @Override
    public <T_RESULT> EventLoggerBuilder.ResultSubBuilder<T_EVENT_ACTION, T_RESULT> withComplexLoggedResult(
            final Function<T_EVENT_ACTION, ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION>> loggedWork) {

        return new ResultSubBuilderImpl<>(
                this,
                Objects.requireNonNull(loggedWork, "loggedWork must be provided"));
    }

    @Override
    public <T_RESULT> EventLoggerBuilder.ResultSubBuilder<T_EVENT_ACTION, T_RESULT> withSimpleLoggedResult(
            final Supplier<T_RESULT> loggedWork) {

        return new ResultSubBuilderImpl<>(
                this,
                (eventAction ->
                        ComplexLoggedOutcome.success(
                                Objects.requireNonNull(loggedWork, "loggedWork must be provided").get(),
                                eventAction)));
    }

    @Override
    public EventLoggerBuilder.ActionSubBuilder<T_EVENT_ACTION> withComplexLoggedAction(
            final Function<T_EVENT_ACTION, ComplexLoggedOutcome<Void, T_EVENT_ACTION>> loggedAction) {

        return new ActionSubBuilderImpl<>(
                this,
                Objects.requireNonNull(loggedAction, "loggedAction must be provided"));
    }

    @Override
    public EventLoggerBuilder.ActionSubBuilder<T_EVENT_ACTION> withSimpleLoggedAction(
            final Runnable loggedAction) {

        return new ActionSubBuilderImpl<>(
                this,
                eventAction -> {
                    loggedAction.run();
                    return ComplexLoggedOutcome.success(eventAction);
                });
    }

    private <T_RESULT> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final Purpose purpose,
            final T_EVENT_ACTION eventAction,
            final Function<T_EVENT_ACTION, ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION>> loggedWork,
            final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler,
            final boolean isLoggingRequired) {

        Objects.requireNonNull(eventAction);
        Objects.requireNonNull(loggedWork);

        final T_RESULT result;
        if (isLoggingRequired) {
            try {
                // Perform the callers work, allowing them to provide a new EventAction based on the
                // result of the work e.g. if they are updating a record, they can capture the before state
                final ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION> complexLoggedOutcome = loggedWork
                        .apply(eventAction);

                final Event event = eventLoggingService.createEvent(
                        eventTypeId,
                        description,
                        purpose,
                        complexLoggedOutcome.getEventAction());

                // From a logging point of view the work may be unsuccessful even if no ex is thrown
                // so add the outcome to the event
                if (!complexLoggedOutcome.wasSuccessful() && eventAction instanceof HasOutcome) {
                    addFailureOutcome(
                            complexLoggedOutcome.getOutcomeDescription(),
                            complexLoggedOutcome.getEventAction());
                }

                eventLoggingService.log(event);
                result = complexLoggedOutcome.getResult();
            } catch (final ValidationException e) {
                // The event being logged is malformed, so there is no point trying to log a 'failed' outcome
                // event.
                throw e;
            } catch (final Throwable e) {
                T_EVENT_ACTION newEventAction = eventAction;
                if (exceptionHandler != null) {
                    try {
                        // Allow caller to provide a new EventAction based on the exception
                        newEventAction = exceptionHandler.handle(eventAction, e);
                    } catch (Exception exception) {
                        LOGGER.error("Error running exception handler. " +
                                "Swallowing exception and rethrowing original exception", e);
                    }
                } else {
                    // No handler so see if we can add an outcome
                    if (eventAction instanceof HasOutcome) {
                        addFailureOutcome(e, newEventAction);
                    }
                }
                final Event event = eventLoggingService.createEvent(eventTypeId, description, newEventAction);
                eventLoggingService.log(event);
                // Rethrow the exception from the callers work
                throw e;
            }
        } else {
            result = loggedWork.apply(eventAction).getResult();
        }

        return result;
    }

    private void addFailureOutcome(final Throwable e, final EventAction eventAction) {
        final String description = e.getMessage() != null
                ? e.getMessage()
                : e.getClass().getName();

        addFailureOutcome(description, eventAction);
    }

    private void addFailureOutcome(final String description, final EventAction eventAction) {
        try {
            final HasOutcome hasOutcome = (HasOutcome) eventAction;
            BaseOutcome baseOutcome = hasOutcome.getOutcome();

            if (baseOutcome == null) {
                // eventAction has no outcome so we need to create one on it
                baseOutcome = createBaseOutcome(eventAction)
                        .orElse(null);
            }

            if (baseOutcome == null) {
                LOGGER.error("Unable to set outcome on {}", eventAction.getClass().getName());
            } else {
                baseOutcome.setSuccess(false);
                baseOutcome.setDescription(description);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to add failure outcome to {}", eventAction.getClass().getName(), e);
        }
    }

    private Optional<BaseOutcome> createBaseOutcome(final EventAction eventAction) {
        // We need to call setOutcome on eventAction but we don't know what sub class of
        // BaseOutcome it is so need to use reflection to find out.
        // Scanning the methods on each call is expensive so figure out what the ctor
        // and setOutcome methods are on first use then cache them.
        // Manually storing the mappings would be brittle to schema changes.
        return OUTCOME_FACTORY_MAP.computeIfAbsent(eventAction.getClass(), clazz -> {

            return Arrays.stream(eventAction.getClass().getMethods())
                    .filter(outcomeSetter -> outcomeSetter.getName().equals("setOutcome"))
                    .findAny()
                    .flatMap(outcomeSetter -> {
                        Class<?> outcomeClass = outcomeSetter.getParameterTypes()[0];

                        Constructor<?> constructor;
                        try {
                            constructor = outcomeClass.getDeclaredConstructor();
                        } catch (NoSuchMethodException e) {
                            LOGGER.warn("No noargs constructor found for " + outcomeClass.getName(), e);
                            return Optional.empty();
                        }

                        final Function<EventAction, BaseOutcome> outcomeAdderFunc = anEventAction -> {
                            try {
                                final BaseOutcome outcome = (BaseOutcome) constructor.newInstance();
                                outcomeSetter.invoke(anEventAction, outcomeClass.cast(outcome));
                                return outcome;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        };
                        LOGGER.debug("Caching function for {}", eventAction.getClass().getName());
                        return Optional.of(outcomeAdderFunc);
                    });
        })
                .flatMap(outcomeSetter ->
                        Optional.of(outcomeSetter.apply(eventAction)));
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public static class ResultSubBuilderImpl<T_EVENT_ACTION extends EventAction, T_RESULT>
            implements EventLoggerBuilder.ResultSubBuilder<T_EVENT_ACTION, T_RESULT> {

        private final EventLoggerBuilderImpl<T_EVENT_ACTION> basicBuilder;
        private final Function<T_EVENT_ACTION, ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION>> loggedWork;

        ResultSubBuilderImpl(
                final EventLoggerBuilderImpl<T_EVENT_ACTION> basicBuilder,
                final Function<T_EVENT_ACTION, ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION>> loggedWork) {

            this.loggedWork = loggedWork;
            this.basicBuilder = basicBuilder;
        }

        Function<T_EVENT_ACTION, ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION>> getLoggedWork() {
            return loggedWork;
        }

        @Override
        public EventLoggerBuilder.ResultSubBuilder<T_EVENT_ACTION, T_RESULT> withCustomExceptionHandler(
                final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {
            basicBuilder.exceptionHandler = exceptionHandler;
            return this;
        }

        @Override
        public EventLoggerBuilder.ResultSubBuilder<T_EVENT_ACTION, T_RESULT> withPurpose(
                final Purpose purpose) {
            basicBuilder.purpose = purpose;
            return this;
        }

        @Override
        public EventLoggerBuilder.ResultSubBuilder<T_EVENT_ACTION, T_RESULT> withLoggingRequiredWhen(
                final boolean isLoggingRequired) {
            basicBuilder.isLogEventRequired = isLoggingRequired;
            return this;
        }

        @Override
        public T_RESULT getResultAndLog() {
            // do logging
            return basicBuilder.loggedResult(
                    basicBuilder.eventTypeId,
                    basicBuilder.description,
                    basicBuilder.purpose,
                    basicBuilder.eventAction,
                    loggedWork,
                    basicBuilder.exceptionHandler,
                    basicBuilder.isLogEventRequired);
        }
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public static class ActionSubBuilderImpl<T_EVENT_ACTION extends EventAction>
            implements EventLoggerBuilder.ActionSubBuilder<T_EVENT_ACTION> {

        private final EventLoggerBuilderImpl<T_EVENT_ACTION> basicBuilder;
        private final Function<T_EVENT_ACTION, ComplexLoggedOutcome<Void, T_EVENT_ACTION>> loggedAction;

        ActionSubBuilderImpl(
                final EventLoggerBuilderImpl<T_EVENT_ACTION> basicBuilder,
                final Function<T_EVENT_ACTION, ComplexLoggedOutcome<Void, T_EVENT_ACTION>> loggedAction) {

            this.basicBuilder = basicBuilder;
            this.loggedAction = loggedAction;
        }

        Function<T_EVENT_ACTION, ComplexLoggedOutcome<Void, T_EVENT_ACTION>> getLoggedAction() {
            return loggedAction;
        }

        @Override
        public EventLoggerBuilder.ActionSubBuilder<T_EVENT_ACTION> withCustomExceptionHandler(
                final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {
            basicBuilder.exceptionHandler = exceptionHandler;
            return this;
        }

        /**
         * {@link EventLoggerBuilder.OptionalMethods#withPurpose(Purpose)}
         */
        @Override
        public EventLoggerBuilder.ActionSubBuilder<T_EVENT_ACTION> withPurpose(final Purpose purpose) {
            basicBuilder.purpose = purpose;
            return this;
        }

        /**
         * {@link EventLoggerBuilder.OptionalMethods#withLoggingRequiredWhen(boolean)}
         */
        @Override
        public EventLoggerBuilder.ActionSubBuilder<T_EVENT_ACTION> withLoggingRequiredWhen(
                final boolean isLoggingRequired) {
            basicBuilder.isLogEventRequired = isLoggingRequired;
            return this;
        }

        /**
         *
         */
        @Override
        public void runActionAndLog() {
            // do logging
            basicBuilder.loggedResult(
                    basicBuilder.eventTypeId,
                    basicBuilder.description,
                    basicBuilder.purpose,
                    basicBuilder.eventAction,
                    loggedAction,
                    basicBuilder.exceptionHandler,
                    basicBuilder.isLogEventRequired);
        }
    }
}
