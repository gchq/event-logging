package event.logging.base;

@FunctionalInterface
public interface LoggedRunnable {

    LoggedOutcome<Void> run();
}
