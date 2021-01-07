package event.logging.base;

import event.logging.EventAction;

import java.util.Objects;

/**
 * Simple POJO to hold the result and outcome of some logged work. The result may be null
 * if the work has no result or returns null. Also includes an outcome for the work for cases
 * where no exception has been thrown but the work was deemed to have failed. It also holds
 * the EventAction that will be logged as part of the work.
 * @param <T_RESULT> The type of the result of the work or Void if there is no result.
 * @param <T_EVENT_ACTION> The type of the EventAction.
 */
public class ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION extends EventAction> {

    final LoggedOutcome<T_RESULT> loggedOutcome;
    final T_EVENT_ACTION eventAction;

    private ComplexLoggedOutcome(final T_RESULT result,
                                 final T_EVENT_ACTION eventAction,
                                 final boolean wasSuccessful,
                                 final String outcomeDescription) {
        this.loggedOutcome = LoggedOutcome.of(result, wasSuccessful, outcomeDescription);
        this.eventAction = Objects.requireNonNull(eventAction);
    }

    private ComplexLoggedOutcome(final LoggedOutcome<T_RESULT> loggedOutcome, final T_EVENT_ACTION eventAction) {
        this.loggedOutcome = loggedOutcome;
        this.eventAction = eventAction;
    }

    public static <T_RESULT, T_EVENT_ACTION extends EventAction> ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION> of(
            LoggedOutcome<T_RESULT> loggedOutcome,
            T_EVENT_ACTION eventAction) {
        return new ComplexLoggedOutcome<>(loggedOutcome, eventAction);
    }

    public static <T_RESULT, T_EVENT_ACTION extends EventAction> ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION> success(
            final T_RESULT result,
            final T_EVENT_ACTION eventAction) {

        return new ComplexLoggedOutcome<>(result, eventAction, true, null);
    }

    public static <T_EVENT_ACTION extends EventAction> ComplexLoggedOutcome<Void, T_EVENT_ACTION> success(
            final T_EVENT_ACTION eventAction) {

        return new ComplexLoggedOutcome<>((Void) null, eventAction, true, null);
    }

    public static <T_RESULT, T_EVENT_ACTION extends EventAction> ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION> failure(
            final T_RESULT result,
            final T_EVENT_ACTION eventAction,
            final String outcomeDescription) {

        return new ComplexLoggedOutcome<>(result, eventAction, false, outcomeDescription);
    }

    public static <T_EVENT_ACTION extends EventAction> ComplexLoggedOutcome<Void, T_EVENT_ACTION> failure(
            final T_EVENT_ACTION eventAction,
            final String outcomeDescription) {

        return new ComplexLoggedOutcome<>((Void) null, eventAction, false, outcomeDescription);
    }

    public T_RESULT getResult() {
        return loggedOutcome.getResult();
    }

    public T_EVENT_ACTION getEventAction() {
        return eventAction;
    }

    public String getOutcomeDescription() {
        return loggedOutcome.getOutcomeDescription();
    }

    public boolean wasSuccessful() {
        return loggedOutcome.wasSuccessful();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ComplexLoggedOutcome<?, ?> that = (ComplexLoggedOutcome<?, ?>) o;
        return Objects.equals(loggedOutcome, that.loggedOutcome) && Objects.equals(eventAction, that.eventAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loggedOutcome, eventAction);
    }

    @Override
    public String toString() {
        return "ComplexLoggedOutcome{" +
                "loggedOutcome=" + loggedOutcome +
                ", eventAction=" + eventAction +
                '}';
    }
}
