package event.logging.base;

@FunctionalInterface
public interface LoggedSupplier<T> {

    LoggedOutcome<T> get();
}
