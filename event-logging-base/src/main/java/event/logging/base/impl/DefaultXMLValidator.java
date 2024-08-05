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

import event.logging.base.XMLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class DefaultXMLValidator implements XMLValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultXMLValidator.class);

    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
    private static final String XML11_PI = "<?xml version=\"1.1\" encoding=\"UTF-8\"?>\n";
    private static boolean xml11 = true;

    private final Schema schema;
    private final Supplier<ValidationErrorHandler> validationErrorHandlerSupplier;
    private static final ValidationExceptionBehaviourMode DEFAULT_VALIDATION_EXCEPTION_BEHAVIOUR_MODE =
            event.logging.base.impl.ValidationExceptionBehaviourMode.LOG;
    private final ValidationExceptionBehaviourMode validationExceptionBehaviourMode;

    public DefaultXMLValidator(final String schemaLocation) {
        // use the default of logging all validation messages
        this(
                schemaLocation,
                QuietErrorHandler::new,
                DEFAULT_VALIDATION_EXCEPTION_BEHAVIOUR_MODE);

    }

    public DefaultXMLValidator(final String schemaLocation,
                               final Supplier<ValidationErrorHandler> validationErrorHandlerSupplier) {
        this(schemaLocation, validationErrorHandlerSupplier, DEFAULT_VALIDATION_EXCEPTION_BEHAVIOUR_MODE);
    }

    public DefaultXMLValidator(final String schemaLocation,
                               final Supplier<ValidationErrorHandler> validationErrorHandlerSupplier,
                               final ValidationExceptionBehaviourMode validationExceptionBehaviourMode) {

        if (validationErrorHandlerSupplier == null) {
            throw new RuntimeException("Null errorHandler supplied");
        }

        this.schema = loadSchema(schemaLocation);
        this.validationErrorHandlerSupplier = validationErrorHandlerSupplier;
        this.validationExceptionBehaviourMode = validationExceptionBehaviourMode;

    }

    private Schema loadSchema(final String schemaLocation) {
        Schema schema = null;

        try {
            if (schemaLocation == null) {
                throw new NullPointerException("You must specify a schema location");
            }

            final InputStream inputStream = this.getClass().getResourceAsStream(schemaLocation);
            if (inputStream == null) {
                throw new FileNotFoundException("Unable to locate schema on classpath: " + schemaLocation);
            }

            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final LoggingErrorHandler errorHandler = new LoggingErrorHandler();
            schemaFactory.setErrorHandler(errorHandler);
            schema = schemaFactory.newSchema(new StreamSource(inputStream));
            if (!errorHandler.isOk()) {
                schema = null;
            }
        } catch (final FileNotFoundException | SAXException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return schema;
    }

    @Override
    public void validate(final String xml) {

        if (validationExceptionBehaviourMode.equals(ValidationExceptionBehaviourMode.LOG)) {
            validateAndLog(xml);
        } else {
            validateAndThrow(xml);
        }
    }

    private void validateAndThrow(final String xml) {
        // Only validate if we successfully loaded the schema.
        if (schema != null) {
            try {
                final ValidationErrorHandler validationErrorHandler = validationErrorHandlerSupplier != null
                        ? validationErrorHandlerSupplier.get()
                        : null;
                doValidation(xml, validationErrorHandler);
                if (validationErrorHandler != null) {
                    final String msg = buildMessage(xml, validationErrorHandler);
                    if (!validationErrorHandler.isOk()) {
                        // errors and/or fatal errors so throw
                        throw new RuntimeException(msg);
                    } else if (validationErrorHandler.hasWarnings()) {
                        // only warnings so log
                        LOGGER.warn(msg);
                    }
                }
            } catch (final Exception e) {
                // wrap the checked exception to conform to the public API
                throw new ValidationException("Error while validating against the schema", e);
            }
        } else {
            throw new ValidationException("Unable to validate.  Schema object is null");
        }
    }

    private void validateAndLog(final String xml) {
        // Only validate if we successfully loaded the schema.
        if (schema != null) {
            try {
                final ValidationErrorHandler validationErrorHandler = validationErrorHandlerSupplier != null
                        ? validationErrorHandlerSupplier.get()
                        : null;
                doValidation(xml, validationErrorHandler);
                if (validationErrorHandler != null) {
                    final String msg = buildMessage(xml, validationErrorHandler);

                    if (!validationErrorHandler.isOk()) {
                        LOGGER.error(msg);
                    } else if (validationErrorHandler.hasWarnings()) {
                        LOGGER.warn(msg);
                    }
                }

            } catch (final IOException | SAXException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private synchronized void doValidation(final String xml,
                                           final ValidationErrorHandler validationErrorHandler)
            throws SAXException, IOException {

        final byte[] bytes;

        /*
         * If we want to treat the input as XML v1.1 then we need to add the XML declaration.
         */
        if (xml11) {
            bytes = (XML11_PI + xml).getBytes(DEFAULT_CHARSET);
        } else {
            bytes = xml.getBytes(DEFAULT_CHARSET);
        }
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        final Validator validator = schema.newValidator();

        if (validationErrorHandler != null) {
            validator.setErrorHandler(validationErrorHandler);
        }

        StreamSource streamSource = new StreamSource(inputStream);
        validator.validate(streamSource);
    }

    private static String buildMessage(final String xml,
                                       final ValidationErrorHandler validationErrorHandler) {
        final StringBuilder sb = new StringBuilder();
        sb.append("The Event XML failed validation against the schema with ")
                .append(validationErrorHandler.getFatalErrorCount())
                .append(" fatal error(s), ")
                .append(validationErrorHandler.getErrorCount())
                .append(" error(s) and ")
                .append(validationErrorHandler.getWarningCount())
                .append(" warning(s):");

        final String indent = "  ";

        if (validationErrorHandler.hasFatalErrors()) {
            sb.append("\n")
                    .append(validationErrorHandler.getFatalErrors()
                            .stream()
                            .map(e -> indent + "Fatal: " + e.toString())
                            .collect(Collectors.joining("\n")));
        }

        if (validationErrorHandler.hasErrors()) {
            sb.append("\n")
                    .append(validationErrorHandler.getErrors()
                            .stream()
                            .map(e -> indent + "Error: " + e.toString())
                            .collect(Collectors.joining("\n")));
        }

        if (validationErrorHandler.hasWarnings()) {
            sb.append("\n")
                    .append(validationErrorHandler.getWarnings()
                            .stream()
                            .map(e -> indent + "Warning: " + e.toString())
                            .collect(Collectors.joining("\n")));
        }

        // Append the actual XML, so it is easier to see where the problem is
        if (xml != null && !xml.isBlank()) {
            sb.append("\n")
                    .append(indent)
                    .append("XML:");
            xml.lines().forEach(line ->
                    sb.append("\n")
                            .append(indent)
                            .append(line));
        }

        return sb.toString();
    }
}
