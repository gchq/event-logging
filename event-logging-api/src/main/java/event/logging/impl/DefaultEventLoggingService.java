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
package event.logging.impl;

import event.logging.Event;
import event.logging.EventLoggingService;
import event.logging.XMLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;

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

    /**
     * Used to set validation on or off overriding the system property. This is mainly for testing purposes.
     */
    private Boolean validate;

    public DefaultEventLoggingService() {
        xmlValidator = new event.logging.impl.DefaultXMLValidator(SCHEMA_LOCATION);
    }

    public DefaultEventLoggingService(ErrorHandler schemaValidationErrorHandler) {
        xmlValidator = new event.logging.impl.DefaultXMLValidator(SCHEMA_LOCATION, schemaValidationErrorHandler);
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
     * Creates an empty event.
     * 
     * @return An event that is ready to have additional elements added.
     */
    @Override
    public Event createEvent() {
        return new Event();
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
