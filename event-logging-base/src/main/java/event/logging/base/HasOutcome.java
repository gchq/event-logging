package event.logging.base;

import event.logging.Outcome;

public interface HasOutcome {

    Outcome getOutcome();

    void setOutcome(final Outcome outcome);
}
