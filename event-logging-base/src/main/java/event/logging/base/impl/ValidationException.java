package event.logging.base.impl;

import org.xml.sax.SAXParseException;

public class ValidationException extends RuntimeException {

    ValidationException(final String message) {
        super(message);
    }

    ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    ValidationException(final SAXParseException saxParseException) {
        super(saxParseException.toString(), saxParseException);
    }
}
