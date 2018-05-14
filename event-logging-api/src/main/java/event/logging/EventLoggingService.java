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
package event.logging;

import event.logging.Event;

/**
 * A service for creating events.
 */
public interface EventLoggingService {
    /**
     * Creates an event that may have some common values set by default depending on the particular EventLoggingService
     * implementation being used.
     * 
     * @return An event that is ready to have additional properties set.
     */
    Event createEvent();

    /**
     * Logs an event.
     * 
     * @param event
     *            The event to log.
     */
    void log(Event event);

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
