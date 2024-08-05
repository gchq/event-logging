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

import event.logging.Event;
import event.logging.base.EventLoggerBuilder;
import event.logging.base.EventLoggingService;
import event.logging.base.XMLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * <p>This is the default implementation of {@link EventLoggingService} for logging events.
 * By default it writes serialised events to an SLF4J logger when {@link #log(Event)} is called.</p>
 *
 * <p>It is recommended to extend this class and override the {{@link #createEvent()}} methods to create
 * a common skeleton event for your system.</p>
 *
 * <p>Set the java system property <code>event.logging.validate</code> to <code>true</code> to enable
 * validation of the serialised XML against the XML Schema. This has a performance overhead so is
 * disabled by default.</p>
 *
 * <p>If you want the serialised XML events to be consumed by something other than SLF4J then create
 * your own implementation of {@link LogReceiver} and set the Java system property
 * <code>event.logging.logreceiver</code> to the class name of your class.</p>
 */
public class DefaultEventLoggingService implements EventLoggingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEventLoggingService.class);

    private static final String SCHEMA_LOCATION = SchemaLocator.getSchemaLocation();

    private final EventSerializer eventSerializer = new DefaultEventSerializer();
    private final LogReceiverFactory logReceiverFactory = LogReceiverFactory.getInstance();
    private final XMLValidator xmlValidator;

    /**
     * Used to set validation on or off overriding the system property. This is mainly for testing purposes.
     */
    private Boolean validate;

    /**
     * Uses {@link QuietErrorHandler} and {@link ValidationExceptionBehaviourMode#LOG} for validation (if enabled),
     * i.e. it will attempt to validate as much of the event as possible, logging all validation messages in one
     * ERROR/WARN SLF4J log message at the end of validation.
     * <p>See {@link DefaultEventLoggingService#DefaultEventLoggingService(Supplier, ValidationExceptionBehaviourMode)}
     * for more control of validation behaviour.</p>
     */
    public DefaultEventLoggingService() {
        xmlValidator = new DefaultXMLValidator(SCHEMA_LOCATION);
    }

    /**
     * Uses the {@link ValidationErrorHandler} supplied by {@code validationErrorHandlerSupplier}
     * and {@link ValidationExceptionBehaviourMode#LOG} for validation (if enabled),
     * i.e. it will log all validation messages in one ERROR/WARN SLF4J log message at the end of validation.
     * The implementation of the {@link ValidationErrorHandler} will determine whether only the first problem
     * is logged or all are logged.
     * <p>See {@link DefaultEventLoggingService#DefaultEventLoggingService(Supplier, ValidationExceptionBehaviourMode)}
     * for more control of validation behaviour.</p>
     *
     * @param validationErrorHandlerSupplier Supplies a new instance of a {@link ValidationErrorHandler} to handle
     *                                       validation warnings/errors.
     */
    public DefaultEventLoggingService(final Supplier<ValidationErrorHandler> validationErrorHandlerSupplier) {
        xmlValidator = new DefaultXMLValidator(
                SCHEMA_LOCATION,
                validationErrorHandlerSupplier);
    }

    /**
     * If you want this service to throw an exception if the event fails validation then we recommend passing
     * a supplier of a {@link  QuietErrorHandler} and {@link ValidationExceptionBehaviourMode#THROW}.
     * <p>
     * If you want this service to simply log validation messages to a {@link Logger} then we recommend using
     * the no-args constructor {@link DefaultEventLoggingService#DefaultEventLoggingService()}.
     * </p>
     * @param validationErrorHandlerSupplier Supplies a new instance of a {@link ValidationErrorHandler} to handle
     *                                       validation warnings/errors.
     * @param validationExceptionBehaviourMode Controls how the validator handles exceptions thrown by the
     *                                         {@link ValidationErrorHandler} as supplied by
     *                                         {@code validationErrorHandlerSupplier}.
     */
    public DefaultEventLoggingService(final Supplier<ValidationErrorHandler> validationErrorHandlerSupplier,
                                      final ValidationExceptionBehaviourMode validationExceptionBehaviourMode) {

        LOGGER.info("Using schema location " + SCHEMA_LOCATION);
        xmlValidator = new DefaultXMLValidator(
                SCHEMA_LOCATION,
                validationErrorHandlerSupplier,
                validationExceptionBehaviourMode);
    }

    /**
     * Logs an event to the {@link LogReceiver}.
     *
     * @param event The event to log.
     */
    @Override
    public void log(final Event event) {
        final String data = eventSerializer.serialize(event);
        final String trimmed = data.trim();
        if (!trimmed.isEmpty()) {
            // Validate data here if the configuration option is set.
            if (isValidationRequired()) {
                xmlValidator.validate(trimmed);
            }

            final LogReceiver logReceiver = logReceiverFactory.getLogReceiver();
            logReceiver.log(trimmed);
        }
    }

    @Override
    public EventLoggerBuilder.TypeIdStep loggedWorkBuilder() {

        // noinspection rawtypes - don't know the type yet
        return new EventLoggerBuilderImpl(this);
    }

    private boolean isValidationRequired() {
        // Check the programmatic flag first.
        if (validate != null) {
            return validate;
        }

        // If we aren't setting validate on .
        final String val = System.getProperty(EventLoggingService.PROP_KEY_VALIDATE);
        return Boolean.parseBoolean(val);
    }

    /**
     * Set to true if the event logging service should validate the output XML against the schema. This option helps
     * identify areas of code that are producing invalid data. For performance reasons it is recommended that
     * validation is not performed in production.
     * <p>
     * If validate is set to null then the system property shall be used to determine if validation is performed.
     *
     * @param validate The validation flag.
     */
    @Override
    public void setValidate(final Boolean validate) {
        this.validate = validate;
    }

    /**
     * Use to determine if the event logging service is set to validate data against the XML schema.
     * Takes into account the value supplied by {@link DefaultEventLoggingService#setValidate(Boolean)}
     * and the value of system property <code>event.logging.validate</code>.
     *
     * @return True if the validate flag is set.
     */
    @Override
    public boolean isValidate() {
        return isValidationRequired();
    }
}
