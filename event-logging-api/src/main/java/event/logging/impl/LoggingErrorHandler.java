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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class LoggingErrorHandler implements ErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingErrorHandler.class);
    private boolean ok = true;

    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        LOGGER.warn(exception.getMessage(), exception);
        ok = false;
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
        LOGGER.error(exception.getMessage(), exception);
        ok = false;
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        LOGGER.error(exception.getMessage(), exception);
        ok = false;
    }

    public boolean isOk() {
        return ok;
    }
}
