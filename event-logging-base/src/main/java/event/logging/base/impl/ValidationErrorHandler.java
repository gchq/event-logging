package event.logging.base.impl;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.util.List;

/**
 * An error handler for XML validation. A new instance should be used for each validation.
 * <p>Implementations can either re-throw the {@link SAXParseException}s or silently capture them
 * for later inspection. The latter approach allows for multiple validation problems to be
 * captured rather than failing on the first one.</p>
 */
public interface ValidationErrorHandler extends ErrorHandler {

    /**
     * @return True if no errors or fatal errors have been encountered during validation.
     */
    boolean isOk();

    /**
     * @return A list of the warnings encountered during validation.
     */
    List<SAXParseException> getWarnings();

    /**
     * @return A list of the errors encountered during validation.
     */
    List<SAXParseException> getErrors();

    /**
     * @return A list of the fatal errors encountered during validation.
     */
    List<SAXParseException> getFatalErrors();

    /**
     * @return True if any warnings were encountered during validation.
     */
    default boolean hasWarnings() {
        return getWarningCount() > 0;
    }

    /**
     * @return True if any errors were encountered during validation.
     */
    default boolean hasErrors() {
        return getErrorCount() > 0;
    }

    /**
     * @return True if any fatal errors were encountered during validation.
     */
    default boolean hasFatalErrors() {
        return getFatalErrorCount() > 0;
    }

    /**
     * @return The number of warnings encountered during validation.
     */
    default int getWarningCount() {
        final List<SAXParseException> warnings = getWarnings();
        return warnings != null
                ? warnings.size()
                : 0;
    }

    /**
     * @return The number of errors encountered during validation.
     */
    default int getErrorCount() {
        final List<SAXParseException> errors = getErrors();
        return errors != null
                ? errors.size()
                : 0;
    }

    /**
     * @return The number of fatal errors encountered during validation.
     */
    default int getFatalErrorCount() {
        final List<SAXParseException> fatalErrors = getFatalErrors();
        return fatalErrors != null
                ? fatalErrors.size()
                : 0;
    }
}
