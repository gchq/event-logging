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

import event.logging.BaseOutcome;
import event.logging.Event;
import event.logging.EventAction;
import event.logging.HasOutcome;
import event.logging.base.EventLoggingService;
import event.logging.base.LoggedResult;
import event.logging.base.XMLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This is the default implementation for creating an event that writes to Log4J when logged.
 */
public class DefaultEventLoggingService implements EventLoggingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEventLoggingService.class);

    private static final String SCHEMA_LOCATION = SchemaLocator.getSchemaLocation();

    private static final String VALIDATE = "event.logging.validate";

    private final EventSerializer eventSerializer = new DefaultEventSerializer();
    private final LogReceiverFactory logReceiverFactory = LogReceiverFactory.getInstance();
    private final XMLValidator xmlValidator;

    private final Map<Class<? extends EventAction>, Optional<Function<EventAction, BaseOutcome>>> outcomeFactoryMap = new ConcurrentHashMap<>();

    /**
     * Used to set validation on or off overriding the system property. This is mainly for testing purposes.
     */
    private Boolean validate;

    public DefaultEventLoggingService() {
        xmlValidator = new event.logging.base.impl.DefaultXMLValidator(SCHEMA_LOCATION);
    }

    public DefaultEventLoggingService(ErrorHandler schemaValidationErrorHandler) {
        xmlValidator = new event.logging.base.impl.DefaultXMLValidator(SCHEMA_LOCATION, schemaValidationErrorHandler);
    }

    /**
     * @param schemaValidationErrorHandler
     * @param validationExceptionBehaviourMode
     *            Controls how the validator handles exceptions thrown by the schemaValidationExceptionHandler
     */
    public DefaultEventLoggingService(ErrorHandler schemaValidationErrorHandler,
                                      ValidationExceptionBehaviourMode validationExceptionBehaviourMode) {

        LOGGER.info("Using schema location " + SCHEMA_LOCATION);
        xmlValidator = new DefaultXMLValidator(SCHEMA_LOCATION, schemaValidationErrorHandler,
                validationExceptionBehaviourMode);
    }

    /**
     * Logs an event to the log.
     * 
     * @param event
     *            The event to log.
     */
    @Override
    public void log(final Event event) {
        final String data = eventSerializer.serialize(event);
        final String trimmed = data.trim();
        if (trimmed.length() > 0) {
            // Validate data here if the configuration option is set.
            if (checkValidating()) {
                xmlValidator.validate(trimmed);
            }

            final LogReceiver logReceiver = logReceiverFactory.getLogReceiver();
            logReceiver.log(trimmed);
        }
    }

    @Override
    public <T_RESULT, T_EVENT_ACTION extends EventAction> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final Function<T_EVENT_ACTION, LoggedResult<T_RESULT, T_EVENT_ACTION>> loggedWork,
            final BiFunction<T_EVENT_ACTION, Throwable, T_EVENT_ACTION> exceptionHandler) {

        Objects.requireNonNull(eventAction);
        Objects.requireNonNull(loggedWork);

        final T_RESULT result;

        try {
            // Perform the callers work, allowing them to provide a new EventAction based on the
            // result of the work e.g. if they are updating a record, they can capture the before state
            final LoggedResult<T_RESULT, T_EVENT_ACTION> loggedResult = loggedWork.apply(eventAction);

            final Event event = createEvent(eventTypeId, description, loggedResult.getEventAction());
            log(event);
            result = loggedResult.getResult();
        } catch (Throwable e) {
            T_EVENT_ACTION newEventAction = eventAction;
            if (exceptionHandler != null) {
                try {
                    // Allow caller to provide a new EventAction based on the exception
                    newEventAction = exceptionHandler.apply(eventAction, e);
                } catch (Exception exception) {
                    LOGGER.error( "Error running exception handler. " +
                            "Swallowing exception and rethrowing original exception", e);
                }
            } else {
                // No handler so see if we can add an outcome
                if (eventAction instanceof HasOutcome) {
                    addFailureOutcome(e, newEventAction);
                }
            }
            final Event event = createEvent(eventTypeId, description, newEventAction);
            log(event);
            // Rethrow the exception from the callers work
            throw e;
        }

        return result;
    }

    private void addFailureOutcome(final Throwable e, final EventAction eventAction) {
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
                baseOutcome.setDescription(e.getMessage() != null
                        ? e.getMessage()
                        : e.getClass().getName());
            }
        } catch (Exception exception) {
            LOGGER.error("Unable to add failure outcome to {}", eventAction.getClass().getName(), e);
        }
    }

    private Optional<BaseOutcome> createBaseOutcome(final EventAction eventAction) {
        // We need to call setOutcome on eventAction but we don't know what sub class of
        // BaseOutcome it is so need to use reflection to find out.
        // Scanning the methods on each call is expensive so figure out what the ctor
        // and setOutcome methods are on first use then cache them.
        return outcomeFactoryMap.computeIfAbsent(eventAction.getClass(), clazz -> {

            return Arrays.stream(eventAction.getClass().getMethods())
                    .filter(method -> method.getName().equals("setOutcome"))
                    .findAny()
                    .flatMap(method -> {
                        Class<?> outcomeClass = method.getParameterTypes()[0];

                        Constructor<?> constructor;
                        try {
                            constructor = outcomeClass.getDeclaredConstructor();
                        } catch (NoSuchMethodException e) {
                            LOGGER.warn("No noargs constructor found for " + outcomeClass.getName(), e);
                            return Optional.empty();
                        }

                        final Function<EventAction, BaseOutcome> func = eventAction2 -> {
                            try {
                                final BaseOutcome outcome = (BaseOutcome) constructor.newInstance();
                                method.invoke(eventAction, outcomeClass.cast(outcome));
                                return outcome;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        };
                        LOGGER.debug("Caching function for {}", eventAction.getClass().getName());
                        return Optional.of(func);
                    });
        })
                .flatMap(func ->
                        Optional.of(func.apply(eventAction)));
    }

    private boolean checkValidating() {
        // Check the programmatic flag first.
        if (validate != null) {
            return validate;
        }

        // If we aren't setting validate on .
        final String val = System.getProperty(VALIDATE);
        return Boolean.valueOf(val);
    }

    /**
     * Set to true if the event logging service should validate the output XML against the schema. This option helps
     * identify areas of code that are producing invalid data. For performance reasons it is recommended that
     * validation is not performed in production.
     * 
     * If validate is set to null then the system property shall be used to determine if validation is performed.
     * 
     * @param validate
     *            The validation flag.
     */
    @Override
    public void setValidate(final Boolean validate) {
        this.validate = validate;
    }

    /**
     * Use to determine if the event logging service is set to validate data against the XML schema.
     * 
     * @return True if the validate flag is set.
     */
    @Override
    public boolean isValidate() {
        return validate != null && validate;
    }

}
