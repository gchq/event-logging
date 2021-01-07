package event.logging.base;

import java.util.Objects;

/**
 * Simple POJO to hold the result and outcome of some logged work. The result may be null
 * if the work has no result or returns null. Also includes an outcome for the work for cases
 * where no exception has been thrown but the work was deemed to have failed.
 * @param <T_RESULT> The type of the result of the work or Void if there is no result.
 */
public class LoggedOutcome<T_RESULT> {
    final T_RESULT result;
    final boolean wasSuccessful;
    final String outcomeDescription;

    private LoggedOutcome(final T_RESULT result,
                          final boolean wasSuccessful,
                          final String outcomeDescription) {
        this.result = result;
        this.wasSuccessful = wasSuccessful;
        this.outcomeDescription = outcomeDescription;
    }

    public static <T_RESULT> LoggedOutcome<T_RESULT> of(final T_RESULT result,
                                                        final boolean wasSuccessful,
                                                        final String outcomeDescription) {
        return new LoggedOutcome<>(result, wasSuccessful, outcomeDescription);
    }

    public static <T_RESULT> LoggedOutcome<T_RESULT> success(
            final T_RESULT result) {

        return new LoggedOutcome<>(result, true, null);
    }

    public static LoggedOutcome<Void> success() {

        return new LoggedOutcome<>((Void) null, true, null);
    }

    public static <T_RESULT> LoggedOutcome<T_RESULT> failure(
            final T_RESULT result,
            final String outcomeDescription) {

        return new LoggedOutcome<>(result, false, outcomeDescription);
    }

    public static LoggedOutcome<Void> failure(
            final String outcomeDescription) {

        return new LoggedOutcome<>((Void) null, false, outcomeDescription);
    }

    public T_RESULT getResult() {
        return result;
    }

    public String getOutcomeDescription() {
        return outcomeDescription;
    }

    public boolean wasSuccessful() {
        return wasSuccessful;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LoggedOutcome<?> that = (LoggedOutcome<?>) o;
        return wasSuccessful == that.wasSuccessful && Objects.equals(result, that.result) && Objects.equals(outcomeDescription, that.outcomeDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, wasSuccessful, outcomeDescription);
    }

    @Override
    public String toString() {
        return "LoggedOutcome{" +
                "result=" + result +
                ", wasSuccessful=" + wasSuccessful +
                ", outcomeDescription='" + outcomeDescription + '\'' +
                '}';
    }
}
