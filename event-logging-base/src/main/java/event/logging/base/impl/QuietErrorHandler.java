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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Logs all {@link SAXParseException}s to a {@link Logger} at DEBUG level only.
 * All {@link SAXParseException}s are captured for later retrieval.
 * Not thread safe.
 */
public class QuietErrorHandler implements ValidationErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuietErrorHandler.class);
    private boolean ok = true;
    private List<SAXParseException> warnings = null;
    private List<SAXParseException> errors = null;
    private List<SAXParseException> fatalErrors = null;

    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(exception.toString(), exception);
        }

        if (warnings == null) {
            warnings = new ArrayList<>();
        }
        ok = false;
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(exception.toString(), exception);
        }

        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(exception);
        ok = false;
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(exception.toString(), exception);
        }

        if (fatalErrors == null) {
            fatalErrors = new ArrayList<>();
        }
        fatalErrors.add(exception);
        ok = false;
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    public List<SAXParseException> getWarnings() {
        return warnings != null
                ? warnings
                : Collections.emptyList();
    }

    @Override
    public List<SAXParseException> getErrors() {
        return errors != null
                ? errors
                : Collections.emptyList();
    }

    @Override
    public List<SAXParseException> getFatalErrors() {
        return fatalErrors != null
                ? fatalErrors
                : Collections.emptyList();
    }
}
