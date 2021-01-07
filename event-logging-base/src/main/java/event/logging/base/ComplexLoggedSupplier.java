package event.logging.base;

import event.logging.EventAction;

@FunctionalInterface
public interface ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION extends EventAction> {

    /**
     * Run the logged work and return a {@link ComplexLoggedSupplier} containing the result
     * of the logged work, a new {@link EventAction} based on the work done and the outcome.
     * @param eventAction
     * @return
     */
    ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION> get(final T_EVENT_ACTION eventAction);
}
