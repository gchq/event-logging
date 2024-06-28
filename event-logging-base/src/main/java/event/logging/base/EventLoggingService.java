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

import java.time.Instant;
import java.util.function.Supplier;

/**
 * A service for recording audit events.
 * <p>The default implementation is {@link event.logging.base.impl.DefaultEventLoggingService}</p>
 */
public interface EventLoggingService {

    /**
     * System property key for setting the {@link event.logging.base.impl.LogReceiver} implementation
     * to use. If not set {@link event.logging.base.impl.LoggerLogReceiver} will be used.
     */
    String PROP_KEY_LOG_RECEIVER_CLASS = "event.logging.logreceiver";

    /**
     * System property key used by {@link event.logging.base.impl.DefaultEventLoggingService} to
     * control whether the serialised {@link Event} XML is validated against the XML schema.
     * A value of {@code true} for this property will enable validation.
     * <p>See also {@link EventLoggingService#setValidate(Boolean)}</p>
     */
    String PROP_KEY_VALIDATE = "event.logging.validate";

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
                        .withTimeCreated(Instant.now())
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
     * Logs an event to the {@link event.logging.base.impl.LogReceiver}
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
     * This method allows for a piece of work to be run and logged in one go with any exceptions being logged
     * appropriately. By default if an exception is throw in the logged work then a failure outcome will be
     * added to the
     * @return A builder instance.
     */
    EventLoggerBuilder.TypeIdStep loggedWorkBuilder();

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
