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

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A service for creating events.
 */
public interface EventLoggingService {

    /**
     * Creates an event that may have some common values set by default depending on the particular EventLoggingService
     * implementation being used. If this method is not implemented it will return an empty event by default.
     * 
     * @return An event that is ready to have additional properties set.
     */
    default Event createEvent() {
        return new Event();
    }

    /**
     * Creates an event that may have some common values set by default depending on the particular EventLoggingService
     * implementation being used. If this method is not implemented it will return an event that contains only
     * the supplied typeId, description and eventAction. It is expected that this method is implemented to provide
     * an event with all common values set, e.g. system name, environment, device, etc.
     *
     * @param typeId The typeId of the event, see {@link EventDetail#setTypeId(String)}
     * @param description The description of the event, see {@link EventDetail#setDescription(String)}
     * @param eventAction The action of the logged event, see {@link EventAction}
     * @return An event that is ready to have additional properties set.
     */
    default Event createEvent(final String typeId,
                              final String description,
                              final EventAction eventAction) {
        return Event.builder()
                .withEventDetail(EventDetail.builder()
                        .withTypeId(typeId)
                        .withDescription(description)
                        .withEventAction(eventAction)
                        .build())
                .build();
    }

    /**
     * Creates an event builder instance that may have some common values set by default depending on the particular
     * EventLoggingService implementation being used. The event builder provides a fluent API for building a complete
     * event object in a single command. If this method is not implemented it will return a fresh builder
     * instance with no modifications applied.
     *
     * @return An event builder instance that is ready to have additional properties set.
     */
    default Event.Builder<Void> buildEvent() {
        return Event.builder();
    }

    /**
     * Logs an event.
     * 
     * @param event The event to log.
     */
    void log(Event event);


    /**
     * See also {@link EventLoggingService#loggedResult(String, String, EventAction, Function, BiFunction)}
     * Use this form when you do not need to modify the event based on the result of the work and the work
     * has no result.
     * If an exception occurs in {@code loggedWork} then an unsuccessful outcome will be added to the
     * {@link EventAction} before it is logged.
     */
    default <T_EVENT_ACTION extends EventAction> void loggedAction(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final Runnable loggedWork) {

        final Function<T_EVENT_ACTION, LoggedResult<Void, T_EVENT_ACTION>> loggedResultFunction = event -> {
            loggedWork.run();
            return LoggedResult.of(null, event);
        };

        loggedResult(
                eventTypeId,
                description,
                eventAction,
                loggedResultFunction,
                null);
    }

    /**
     * See also {@link EventLoggingService#loggedResult(String, String, EventAction, Function, BiFunction)}
     * Use this form when the logged work has no result.
     * If an exception occurs in {@code loggedWork} then an unsuccessful outcome will be added to the
     * {@link EventAction} before it is logged.
     */
    default <T_EVENT_ACTION extends EventAction> void loggedAction(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final UnaryOperator<T_EVENT_ACTION> loggedWork,
            final BiFunction<T_EVENT_ACTION, Throwable, T_EVENT_ACTION> exceptionHandler) {

        final Function<T_EVENT_ACTION, LoggedResult<Void, T_EVENT_ACTION>> loggedResultFunction = eventAction2 ->
                LoggedResult.of(null, loggedWork.apply(eventAction2));

        loggedResult(
                eventTypeId,
                description,
                eventAction,
                loggedResultFunction,
                exceptionHandler);
    }

    /**
     * See also {@link EventLoggingService#loggedResult(String, String, EventAction, Function, BiFunction)}
     * Use this form when you do not need to modify the event based on the result of the work.
     * If an exception occurs in {@code loggedWork} then an unsuccessful outcome will be added to the
     * {@link EventAction} before it is logged.
     */
    default <T_RESULT, T_EVENT_ACTION extends EventAction> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final Supplier<T_RESULT> loggedWork) {

        final Function<T_EVENT_ACTION, LoggedResult<T_RESULT, T_EVENT_ACTION>> loggedResultFunction = event ->
                LoggedResult.of(loggedWork.get(), event);

        return loggedResult(
                eventTypeId,
                description,
                eventAction,
                loggedResultFunction,
                null);
    }

    /**
     * Performs {@code loggedWork} and logs an event using the supplied {@link EventAction}.
     * An event is logged if the work is successful or if an exception occurs.
     * Use this form when you want to modify the event based on the result of the work, e.g. recording the
     * before and after of an update, or any exception thrown performing the work.
     * @param eventTypeId The value to set on Event/EventDetail/TypeId. See
     *                    {@link event.logging.EventDetail#setTypeId(String)} for details.
     * @param description A human readable description of the event being logged. Can include IDs/values specific
     *                    to the event, e.g. "Creating user account jbloggs". See also
     *                    {@link event.logging.EventDetail#setDescription(String)}
     * @param eventAction The skeleton {@link EventAction} that will be used to create the event unless
     *                    {@code loggedWork} of {@code exceptionHandler} provide an alternative.
     * @param loggedWork A function to perform the work that is being logged and to return the {@link EventAction}
     *                   and the result of the work. This allows a new {@link EventAction} to be returned
     *                   based on the result of the work. The skeleton {@link EventAction} is passed in
     *                   to allow it to be copied. The result of the work must be returned within a {@link LoggedResult}
     *                   along with the desired {@link EventAction}.
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
    <T_RESULT, T_EVENT_ACTION extends EventAction> T_RESULT loggedResult(
            final String eventTypeId,
            final String description,
            final T_EVENT_ACTION eventAction,
            final Function<T_EVENT_ACTION, LoggedResult<T_RESULT, T_EVENT_ACTION>> loggedWork,
            final BiFunction<T_EVENT_ACTION, Throwable, T_EVENT_ACTION> exceptionHandler);

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
