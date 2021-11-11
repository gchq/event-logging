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
package event.logging.base;

import event.logging.Event;
import event.logging.EventAction;
import event.logging.EventDetail;
import event.logging.EventTime;
import event.logging.Purpose;

import java.util.Date;
import java.util.function.Supplier;

/**
 * A service for creating events.
 */
public interface EventLoggingService {

    /**
     * Creates an event that may have some common values set by default depending on the particular EventLoggingService
     * implementation being used. If this method is not implemented it will return an empty event by default.
     *
     * Using {@link EventLoggingService#createEvent(String, String, EventAction)} should be preferred.
     * 
     * @return An event that is ready to have additional properties set.
     */

    default Event createEvent() {
        return createEvent(null, null, null);
    }

    /**
     * Creates an event that may have some common values set by default depending on the particular EventLoggingService
     * implementation being used. If this method is not implemented it will return an event that contains only
     * the supplied typeId, description and eventAction.
     *
     * @param typeId The typeId of the event, see {@link EventDetail#setTypeId(String)}
     * @param description The description of the event, see {@link EventDetail#setDescription(String)}
     * @param eventAction The action of the logged event, see {@link EventAction}
     * @return An event that is ready to have additional properties set.
     */
    default Event createEvent(final String typeId,
                              final String description,
                              final EventAction eventAction) {
        return createEvent(typeId, description, null, eventAction);
    }

    /**
     * Creates an event that may have some common values set by default depending on the particular EventLoggingService
     * implementation being used. If this method is not implemented it will return an event that contains only
     * the supplied typeId, description, purpose and eventAction. It also has the current time set on the event.
     * It is expected that this method is implemented to provide an event with all common values set,
     * e.g. system name, environment, device, etc.
     *
     * @param typeId The typeId of the event, see {@link EventDetail#setTypeId(String)}
     * @param description The description of the event, see {@link EventDetail#setDescription(String)}
     * @param purpose The justification for the action, see {@link EventDetail#setPurpose(Purpose)}
     * @param eventAction The action of the logged event, see {@link EventAction}
     * @return An event that is ready to have additional properties set.
     */
    default Event createEvent(final String typeId,
                              final String description,
                              final Purpose purpose,
                              final EventAction eventAction) {
        return Event.builder()
                .withEventTime(EventTime.builder()
                        .withTimeCreated(new Date())
                        .build())
                .withEventDetail(EventDetail.builder()
                        .withTypeId(typeId)
                        .withDescription(description)
                        .withPurpose(purpose)
                        .withEventAction(eventAction)
                        .build())
                .build();
    }

    /**
     * Logs an event.
     * 
     * @param event The event to log.
     */
    void log(Event event);

    /**
     * Logs an event with the supplied typeId, description and {@link EventAction}.
     * Makes use of {@link event.logging.EventLoggingService#createEvent(String, String, EventAction)}
     * to provide an event with other common values set.
     *
     * @param typeId The typeId of the event, see {@link EventDetail#setTypeId(String)}
     * @param description The description of the event, see {@link EventDetail#setDescription(String)}
     * @param eventAction The action of the logged event, see {@link EventAction}
     */
    default void log(final String typeId,
                     final String description,
                     final EventAction eventAction) {
        log(createEvent(typeId, description, null, eventAction));
    }

    /**
     * Logs an event with the supplied typeId, description and {@link EventAction}.
     * Makes use of {@link event.logging.EventLoggingService#createEvent(String, String, EventAction)}
     * to provide an event with other common values set.
     *
     * @param typeId The typeId of the event, see {@link EventDetail#setTypeId(String)}
     * @param description The description of the event, see {@link EventDetail#setDescription(String)}
     * @param eventAction The action of the logged event, see {@link EventAction}
     */
    default void log(final String typeId,
                     final String description,
                     final Purpose purpose,
                     final EventAction eventAction) {
        log(createEvent(typeId, description, purpose, eventAction));
    }

    /**
     * Creates a builder to assist with logging an event for a {@link Runnable} or {@link Supplier}.
     * This method allows for a piece of work to be run and logged in one go with any expceptions behing logged
     * appropriately.
     * @return A builder instance.
     */
    EventLoggerBasicBuilder.TypeIdBuildStep loggedWorkBuilder();

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_EVENT_ACTION extends EventAction> void loggedAction(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final Runnable loggedWork) {

        final ComplexLoggedSupplier<Void, T_EVENT_ACTION> loggedResultFunction = eventAction2 -> {
            loggedWork.run();
            // We don't have an outcome so assume success
            return ComplexLoggedOutcome.of(LoggedOutcome.success(), eventAction2);
        };

        loggedResult(
                eventTypeId,
                description,
                null,
                eventAction,
                loggedResultFunction,
                null);
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_EVENT_ACTION extends EventAction> void loggedAction(
            final String eventTypeId,
            final String description,
            final Purpose purpose,
            final T_EVENT_ACTION eventAction,
            final Runnable loggedWork) {

        final ComplexLoggedSupplier<Void, T_EVENT_ACTION> loggedResultFunction = eventAction2 -> {
            loggedWork.run();
            // We don't have an outcome so assume success
            return ComplexLoggedOutcome.of(LoggedOutcome.success(), eventAction2);
        };

        loggedResult(
                eventTypeId,
                description,
                purpose,
                eventAction,
                loggedResultFunction,
                null);
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_EVENT_ACTION extends EventAction> void loggedAction(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final LoggedRunnable loggedWork) {

        final ComplexLoggedSupplier<Void, T_EVENT_ACTION> loggedResultFunction = eventAction2 -> {
            final LoggedOutcome<Void> loggedOutcome = loggedWork.run();
            return ComplexLoggedOutcome.of(loggedOutcome, eventAction2);
        };

        loggedResult(
                eventTypeId,
                description,
                null,
                eventAction,
                loggedResultFunction,
                null);
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_EVENT_ACTION extends EventAction> void loggedAction(
            final String eventTypeId,
            final String description,
            final Purpose purpose,
            final T_EVENT_ACTION eventAction,
            final LoggedRunnable loggedWork) {

        final ComplexLoggedRunnable<T_EVENT_ACTION> loggedRunnable = eventAction2 -> {
            final LoggedOutcome<Void> loggedOutcome = loggedWork.run();
            return ComplexLoggedOutcome.of(loggedOutcome, eventAction2);
        };

        loggedWorkBuilder()
                .withTypeId(eventTypeId)
                .withDescription(description)
                .withDefaultEventAction(eventAction)
                .withComplexLoggedAction(loggedRunnable)
                .withPurpose(purpose)
                .runActionAndLog();
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_EVENT_ACTION extends EventAction> void loggedAction(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final ComplexLoggedRunnable<T_EVENT_ACTION> loggedWork,
            final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {

        loggedWorkBuilder()
                .withTypeId(eventTypeId)
                .withDescription(description)
                .withDefaultEventAction(eventAction)
                .withComplexLoggedAction(loggedWork)
                .withCustomExceptionHandler(exceptionHandler)
                .runActionAndLog();
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_EVENT_ACTION extends EventAction> void loggedAction(
            final String eventTypeId,
            final String description,
            final Purpose purpose,
            final T_EVENT_ACTION eventAction,
            final ComplexLoggedRunnable<T_EVENT_ACTION> loggedWork,
            final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {

        loggedWorkBuilder()
                .withTypeId(eventTypeId)
                .withDescription(description)
                .withDefaultEventAction(eventAction)
                .withComplexLoggedAction(loggedWork)
                .withPurpose(purpose)
                .withCustomExceptionHandler(exceptionHandler)
                .runActionAndLog();
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_RESULT, T_EVENT_ACTION extends EventAction> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final Supplier<T_RESULT> loggedWork) {

        final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedResultFunction = eventAction2 -> {
            T_RESULT result = loggedWork.get();
            // We don't have an outcome so assume success
            return ComplexLoggedOutcome.of(LoggedOutcome.success(result), eventAction2);
        };

        return loggedWorkBuilder()
                .withTypeId(eventTypeId)
                .withDescription(description)
                .withDefaultEventAction(eventAction)
                .withSimpleLoggedResult(loggedWork)
                .getResultAndLog();
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_RESULT, T_EVENT_ACTION extends EventAction> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final Purpose purpose,
            final T_EVENT_ACTION eventAction,
            final Supplier<T_RESULT> loggedWork) {

        return loggedWorkBuilder()
                .withTypeId(eventTypeId)
                .withDescription(description)
                .withDefaultEventAction(eventAction)
                .withSimpleLoggedResult(loggedWork)
                .withPurpose(purpose)
                .getResultAndLog();
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_RESULT, T_EVENT_ACTION extends EventAction> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final LoggedSupplier<T_RESULT> loggedWork) {

        final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedResultFunction = eventAction2 ->
                ComplexLoggedOutcome.of(loggedWork.get(), eventAction2);

        return loggedWorkBuilder()
                .withTypeId(eventTypeId)
                .withDescription(description)
                .withDefaultEventAction(eventAction)
                .withComplexLoggedResult(loggedResultFunction)
                .getResultAndLog();
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_RESULT, T_EVENT_ACTION extends EventAction> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final Purpose purpose,
            final T_EVENT_ACTION eventAction,
            final LoggedSupplier<T_RESULT> loggedWork) {

        final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedResultFunction = eventAction2 ->
                ComplexLoggedOutcome.of(loggedWork.get(), eventAction2);

        return loggedWorkBuilder()
                .withTypeId(eventTypeId)
                .withDescription(description)
                .withDefaultEventAction(eventAction)
                .withComplexLoggedResult(loggedResultFunction)
                .withPurpose(purpose)
                .getResultAndLog();
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     */
    @Deprecated
    default <T_RESULT, T_EVENT_ACTION extends EventAction> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedWork,
            final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {

        return loggedWorkBuilder()
                .withTypeId(eventTypeId)
                .withDescription(description)
                .withDefaultEventAction(eventAction)
                .withComplexLoggedResult(loggedWork)
                .withCustomExceptionHandler(exceptionHandler)
                .getResultAndLog();
    }

    /**
     * @deprecated Use {@link #loggedWorkBuilder()}
     * Performs {@code loggedWork} and logs an event using the supplied {@link EventAction}.
     * An event is logged if the work is successful or if an exception occurs.
     * Use this form when you want to modify the event based on the result of the work, e.g. recording the
     * before and after of an update, or any exception thrown performing the work.
     * @param eventTypeId The value to set on Event/EventDetail/TypeId. See
     *                    {@link event.logging.EventDetail#setTypeId(String)} for details.
     * @param description A human readable description of the event being logged. Can include IDs/values specific
     *                    to the event, e.g. "Creating user account jbloggs". See also
     *                    {@link event.logging.EventDetail#setDescription(String)}
     * @param purpose The purpose/justification for the action being logged. Can be null.
     * @param eventAction The skeleton {@link EventAction} that will be used to create the event unless
     *                    {@code loggedWork} of {@code exceptionHandler} provide an alternative.
     * @param loggedWork A lambda to perform the work that is being logged and to return the {@link EventAction}
     *                   , the result of the work and the outcome. This allows a new {@link EventAction} to be returned
     *                   based on the result of the work. The skeleton {@link EventAction} is passed in
     *                   to allow it to be copied. The result of the work must be returned within a
     *                   {@link ComplexLoggedOutcome} along with the desired {@link EventAction}.
     * @param exceptionHandler A function to allow you to provide a different {@link EventAction} based on
     *                         the exception. The skeleton {@link EventAction} is passed in to allow it to be
     *                         copied.<br/>
     *                         If null then an outcome will be set on the skeleton event action and
     *                         the exception message will be added to the outcome
     *                         description. The outcome success will be set to false.<br/>
     *                         In either case, an event will be logged and the original exception rethrown for
     *                         the caller to handle. Any exceptions in the handler will be ignored and the original
     *                         exception rethrown.
     */
    @Deprecated
    default <T_RESULT, T_EVENT_ACTION extends EventAction> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final Purpose purpose,
            final T_EVENT_ACTION eventAction,
            final ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedWork,
            final LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler) {

        return loggedWorkBuilder()
                .withTypeId(eventTypeId)
                .withDescription(description)
                .withDefaultEventAction(eventAction)
                .withComplexLoggedResult(loggedWork)
                .withPurpose(purpose)
                .withCustomExceptionHandler(exceptionHandler)
                .getResultAndLog();
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
    void setValidate(Boolean validate);

    /**
     * Use to determine if the event logging service is set to validate output data against the XML schema.
     * 
     * @return True if the validate flag is set.
     */
    boolean isValidate();
}
