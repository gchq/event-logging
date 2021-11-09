package event.logging.base;

import event.logging.BaseOutcome;
import event.logging.Event;
import event.logging.EventAction;
import event.logging.HasOutcome;
import event.logging.Purpose;
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

public class EventLoggerBasicBuilder<T_EVENT_ACTION extends EventAction> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggerBasicBuilder.class);

    // For each event action hold a function to create the appropriate subclass of BaseOutcome
    private static final Map<Class<? extends EventAction>, Optional<Function<EventAction, BaseOutcome>>>
            OUTCOME_FACTORY_MAP = new ConcurrentHashMap<>();

    private final EventLoggingService eventLoggingService;
    private final String eventTypeId;
    private final String description;
    private final T_EVENT_ACTION eventAction;

    private Purpose purpose;
    private boolean isLogEventRequired = true;
    private LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler;

    EventLoggerBasicBuilder(final EventLoggingService eventLoggingService,
                            final String eventTypeId,
                            final String description,
                            final T_EVENT_ACTION eventAction) {
        this.eventLoggingService = eventLoggingService;
        this.eventTypeId = Objects.requireNonNull(eventTypeId, "Type ID required for all events.");
        this.description = Objects.requireNonNull(description, "Description required for all events.");
        this.eventAction = Objects.requireNonNull(
                eventAction, "Initial event action required for all events.");
    }

    private EventLoggerBasicBuilder(final EventLoggingService eventLoggingService,
                                    final String eventTypeId,
                                    final String description,
                                    final T_EVENT_ACTION eventAction,
                                    final boolean isLogEventRequired,
                                    final Purpose purpose,
                                    final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {
        this.eventLoggingService = eventLoggingService;
        this.eventTypeId = eventTypeId;
        this.description = description;
        this.eventAction = eventAction;
        this.isLogEventRequired = isLogEventRequired;
        this.purpose = purpose;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Proved a custom exception handler that will intercept the exception and can then
     * @param exceptionHandler A function to allow you to provide a different {@link EventAction} based on
     *                         the exception. The skeleton {@link EventAction} is passed in to allow it to be
     *                         copied.<br/>
     *                         If null or this method is not called then an outcome will be set on the skeleton
     *                         event action and the exception message will be added to the outcome
     *                         description. The outcome success will be set to false.<br/>
     *                         In either case, an event will be logged and the original exception re-thrown for
     *                         the caller to handle. Any exceptions in the handler will be ignored and the original
     *                         exception rethrown.
     * @return The builder instance.
     */
    public EventLoggerBasicBuilder<T_EVENT_ACTION> withCustomExceptionHandler(
            final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * Specify the purpose for the action being logged.
     * @param purpose The purpose/justification for the action being logged.
     * @return The builder instance.
     */
    public EventLoggerBasicBuilder<T_EVENT_ACTION> withPurpose(final Purpose purpose) {
        this.purpose = purpose;
        return this;
    }

    /**
     * Allows for selective logging of events.
     * @param isLoggingRequired False if the work should be performed without logging an event.
     * @return The builder instance.
     */
    public EventLoggerBasicBuilder<T_EVENT_ACTION> withLoggingRequired(
            final boolean isLoggingRequired) {

        this.isLogEventRequired = isLoggingRequired;
        return this;
    }

    /**
     * @param loggedWork A lambda to perform the work that is being logged and to return the {@link EventAction}
     *                   , the result of the work and the outcome. This allows a new {@link EventAction} to be returned
     *                   based on the result of the work. The skeleton {@link EventAction} is passed in
     *                   to allow it to be copied. The result of the work must be returned within a
     *                   {@link ComplexLoggedOutcome} along with the desired {@link EventAction}.
     * @param <T_RESULT> The type of the result of the work.
     * @return The builder instance.
     */
    public <T_RESULT> EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withComplexLoggedResult(
            final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedWork) {

        return new EventLoggerLoggedResultBuilder<>(
                this,
                Objects.requireNonNull(loggedWork, "loggedWork must be provided"));
    }

    /**
     * @param loggedWork A simple {@link Supplier} to perform the work that is being logged and to return
     *                   the result of the work. The outcome will be assumed to be a success unless an
     *                   exception is thrown.
     * @param <T_RESULT> The type of the result of the work.
     * @return The builder instance.
     */
    public <T_RESULT> EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withSimpleLoggedResult(
            final Supplier<T_RESULT> loggedWork) {

        return new EventLoggerLoggedResultBuilder<>(
                this,
                (eventAction ->
                        ComplexLoggedOutcome.success(
                                Objects.requireNonNull(loggedWork, "loggedWork must be provided").get(),
                                eventAction)));
    }

    /**
     * @param loggedAction A lambda to perform the work that is being logged and to return the outcome.
     *                   This allows a new {@link EventAction} to be returned
     *                   based on the result of the work. The skeleton {@link EventAction} is passed in
     *                   to allow it to be copied. The result of the work must be returned within a
     *                   {@link ComplexLoggedOutcome} along with the desired {@link EventAction}.
     * @return The builder instance.
     */
    public EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withComplexLoggedAction(
            final ComplexLoggedRunnable<T_EVENT_ACTION> loggedAction) {

        return new EventLoggerLoggedActionBuilder<>(
                this,
                Objects.requireNonNull(loggedAction, "loggedAction must be provided"));
    }

    /**
     * @param loggedAction A simple {@link Runnable} to perform the work that is being logged.
     *                   The outcome will be assumed to be a success unless an exception is thrown.
     * @return The builder instance.
     */
    public EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withSimpleLoggedAction(
            final Runnable loggedAction) {

        return new EventLoggerLoggedActionBuilder<>(
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
            final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedWork,
            final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler,
            final boolean isLoggingRequired) {

        Objects.requireNonNull(eventAction);
        Objects.requireNonNull(loggedWork);

        final T_RESULT result;
        if (isLoggingRequired) {
            try {
                // Perform the callers work, allowing them to provide a new EventAction based on the
                // result of the work e.g. if they are updating a record, they can capture the before state
                final ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION> complexLoggedOutcome = loggedWork.get(eventAction);

                final Event event = eventLoggingService.createEvent(eventTypeId, description, purpose, complexLoggedOutcome.getEventAction());

                // From a logging point of view the work may be unsuccessful even if no ex is thrown
                // so add the outcome to the event
                if (!complexLoggedOutcome.wasSuccessful() && eventAction instanceof HasOutcome) {
                    addFailureOutcome(
                            complexLoggedOutcome.getOutcomeDescription(),
                            complexLoggedOutcome.getEventAction());
                }

                eventLoggingService.log(event);
                result = complexLoggedOutcome.getResult();
            } catch (Throwable e) {
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
            result = loggedWork.get(eventAction).getResult();
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


    public static class EventLoggerLoggedResultBuilder<T_EVENT_ACTION extends EventAction, T_RESULT> {

        private final EventLoggerBasicBuilder<T_EVENT_ACTION> basicBuilder;
        private final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedWork;

        private EventLoggerLoggedResultBuilder(
                final EventLoggerBasicBuilder<T_EVENT_ACTION> basicBuilder,
                final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedWork) {

            this.loggedWork = loggedWork;
            this.basicBuilder = basicBuilder;
        }

        public EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withCustomExceptionHandler(
                final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {
            basicBuilder.withCustomExceptionHandler(exceptionHandler);
            return this;
        }

        public EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withPurpose(final Purpose purpose) {
            basicBuilder.withPurpose(purpose);
            return this;
        }

        public EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withLoggingRequired(final boolean isLoggingRequired) {
            basicBuilder.withLoggingRequired(isLoggingRequired);
            return this;
        }

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


    public static class EventLoggerLoggedActionBuilder<T_EVENT_ACTION extends EventAction> {

        private final EventLoggerBasicBuilder<T_EVENT_ACTION> basicBuilder;
        private final ComplexLoggedRunnable<T_EVENT_ACTION> loggedAction;

        private EventLoggerLoggedActionBuilder(
                final EventLoggerBasicBuilder<T_EVENT_ACTION> basicBuilder,
                final ComplexLoggedRunnable<T_EVENT_ACTION> loggedWork) {

            this.basicBuilder = basicBuilder;
            this.loggedAction = loggedWork;
        }

        /**
         * {@link event.logging.EventLoggerBasicBuilder#withCustomExceptionHandler(event.logging.LoggedWorkExceptionHandler)}
         */
        public EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withCustomExceptionHandler(
                final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {
            basicBuilder.withCustomExceptionHandler(exceptionHandler);
            return this;
        }

        /**
         * {@link event.logging.EventLoggerBasicBuilder#withPurpose(Purpose)}
         */
        public EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withPurpose(final Purpose purpose) {
            basicBuilder.withPurpose(purpose);
            return this;
        }

        /**
         * {@link event.logging.EventLoggerBasicBuilder#withLoggingRequired(boolean)}
         */
        public EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withLoggingRequired(final boolean isLoggingRequired) {
            basicBuilder.withLoggingRequired(isLoggingRequired);
            return this;
        }

        /**
         *
         */
        public void runActionAndLog() {
            // do logging
            basicBuilder.loggedResult(
                    basicBuilder.eventTypeId,
                    basicBuilder.description,
                    basicBuilder.purpose,
                    basicBuilder.eventAction,
                    loggedAction::run,
                    basicBuilder.exceptionHandler,
                    basicBuilder.isLogEventRequired);
        }
    }
}
