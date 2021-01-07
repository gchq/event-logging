package event.logging.base;

import event.logging.EventAction;

@FunctionalInterface
public interface ComplexLoggedRunnable<A extends EventAction> {

    ComplexLoggedOutcome<Void, A> run(final A eventAction);
}
