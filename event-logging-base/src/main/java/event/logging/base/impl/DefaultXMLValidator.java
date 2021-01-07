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
import org.xml.sax.ErrorHandler;
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

public final class DefaultXMLValidator implements XMLValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultXMLValidator.class);

    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
    private static final String XML11_PI = "<?xml version=\"1.1\" encoding=\"UTF-8\"?>\n";
    private static boolean xml11 = true;

    private final Schema schema;
    private final ErrorHandler validationErrorHandler;
    private static final ValidationExceptionBehaviourMode DEFAULT_VALIDATION_EXCEPTION_BEHAVIOUR_MODE = event.logging.base.impl.ValidationExceptionBehaviourMode.LOG;
    private final ValidationExceptionBehaviourMode validationExceptionBehaviourMode;

    public DefaultXMLValidator(final String schemaLocation) {
        // use the default of logging all validation messages
        this(schemaLocation, new LoggingErrorHandler(), DEFAULT_VALIDATION_EXCEPTION_BEHAVIOUR_MODE);

    }

    public DefaultXMLValidator(final String schemaLocation, final ErrorHandler validationErrorHandler) {
        this(schemaLocation, validationErrorHandler, DEFAULT_VALIDATION_EXCEPTION_BEHAVIOUR_MODE);
    }

    public DefaultXMLValidator(final String schemaLocation, final ErrorHandler validationErrorHandler,
                               final ValidationExceptionBehaviourMode validationExceptionBehaviourMode) {

        if (validationErrorHandler == null) {
            throw new RuntimeException("Null errorHandler supplied");
        }

        this.schema = loadSchema(schemaLocation);
        this.validationErrorHandler = validationErrorHandler;
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
        } catch (final FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final SAXException e) {
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
                doValidation(xml);
            } catch (final Exception e) {
                // wrap the checked exception to conform to the public API
                throw new RuntimeException("Error while validating against the schema", e);
            }
        } else {
            throw new RuntimeException("Unable to validate.  Schema object is null");
        }
    }

    private void validateAndLog(final String xml) {

        // Only validate if we successfully loaded the schema.
        if (schema != null) {
            try {
                doValidation(xml);
            } catch (final IOException | SAXException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private synchronized void doValidation(final String xml) throws SAXException, IOException {

        // Only validate if we successfully loaded the schema.
        if (schema != null) {
            byte[] bytes = null;

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

            validator.setErrorHandler(validationErrorHandler);
            StreamSource streamSource = new StreamSource(inputStream);
            validator.validate(streamSource);
        }
    }

}
