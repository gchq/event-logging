package event.logging.base;

import event.logging.EventAction;
import event.logging.EventDetail;
import event.logging.Purpose;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This interface is used more as a wrapper to group all the inner interfaces.
 * This is a stepped builder which enforces the order in which builder methods are called.
 * It also has two sub builders to deal with needing different builder methods depending
 * on the input to the methods in {@link WorkStep}.
 * The mandatory methods are handled by having single method interfaces that each return the
 * next build step interface in the chain.
 */
public interface EventLoggerBuilder {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface TypeIdStep {
        /**
         * @param typeId The typeId of the event, see {@link EventDetail#setTypeId(String)}
         * @return A builder instance.
         */
        DescriptionStep withTypeId(final String typeId);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface DescriptionStep {
        /**
         * @param description The description of the event, see {@link EventDetail#setDescription(String)}
         * @return A builder instance.
         */
        EventActionStep withDescription(final String description);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface EventActionStep {

        /**
         * @param defaultEventAction The action of the logged event, see {@link EventAction}. This event action will be
         *                           used on the event unless one of the following methods is used which can override it:
         *                           {@link WorkStep#withComplexLoggedAction(Function)}
         *                           {@link WorkStep#withComplexLoggedResult(Function)}
         *                           {@link OptionalMethods#withCustomExceptionHandler(LoggedWorkExceptionHandler)}
         * @param <T>                The type of event action that will be logged, e.g.
         *                           {@link event.logging.SearchEventAction}, {@link event.logging.ViewEventAction}, etc.
         * @return A builder instance.
         */
        <T extends EventAction> WorkStep<T> withDefaultEventAction(
                final T defaultEventAction);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface WorkStep<T_EVENT_ACTION extends EventAction> {

        /**
         * @param loggedWork A lambda to perform the work that is being logged and to return the {@link EventAction}
         *                   , the result of the work and the outcome. This allows a new {@link EventAction} to be returned
         *                   based on the result of the work. The skeleton {@link EventAction} is passed in
         *                   to allow it to be copied. The result of the work must be returned within a
         *                   {@link ComplexLoggedOutcome} along with the desired {@link EventAction}.
         * @param <T_RESULT> The type of the result of the work.
         * @return The builder instance.
         */
        <T_RESULT> ResultSubBuilder<T_EVENT_ACTION, T_RESULT> withComplexLoggedResult(
                Function<T_EVENT_ACTION, ComplexLoggedOutcome<T_RESULT, T_EVENT_ACTION>> loggedWork);

        /**
         * @param loggedWork A simple {@link Supplier} to perform the work that is being logged and to return
         *                   the result of the work. The outcome will be assumed to be a success unless an
         *                   exception is thrown.
         * @param <T_RESULT> The type of the result of the work.
         * @return The builder instance.
         */
        <T_RESULT> ResultSubBuilder<T_EVENT_ACTION, T_RESULT> withSimpleLoggedResult(
                Supplier<T_RESULT> loggedWork);

        /**
         * @param loggedAction A lambda to perform the work that is being logged and to return the outcome.
         *                   This allows a new {@link EventAction} to be returned
         *                   based on the result of the work. The skeleton {@link EventAction} is passed in
         *                   to allow it to be copied. The result of the work must be returned within a
         *                   {@link ComplexLoggedOutcome} along with the desired {@link EventAction}.
         * @return The builder instance.
         */
        ActionSubBuilder<T_EVENT_ACTION> withComplexLoggedAction(
                Function<T_EVENT_ACTION, ComplexLoggedOutcome<Void, T_EVENT_ACTION>> loggedAction);

        /**
         * @param loggedAction A simple {@link Runnable} to perform the work that is being logged.
         *                   The outcome will be assumed to be a success unless an exception is thrown.
         * @return The builder instance.
         */
        ActionSubBuilder<T_EVENT_ACTION> withSimpleLoggedAction(
                Runnable loggedAction);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface OptionalMethods<T_EVENT_ACTION extends EventAction, T_BUILDER> {

        /**
         * Proved a custom exception handler that will intercept the exception and can then
         *
         * @param exceptionHandler A function to allow you to provide a different {@link EventAction} based on
         *                         the exception. The skeleton {@link EventAction} is passed in to allow it to be
         *                         copied.<br/>
         *                         If null or this method is not called then an outcome will be set on the skeleton
         *                         event action and the exception message will be added to the outcome
         *                         description. The outcome success will be set to false.<br/>
         *                         In either case, an event will be logged and the original exception re-thrown for
         *                         the caller to handle. Any exceptions in the handler will be ignored and the original
         *                         exception rethrown.
         * @return The builder instance.
         */
        T_BUILDER withCustomExceptionHandler(LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler);

        /**
         * Specify the purpose for the action being logged.
         *
         * @param purpose The purpose/justification for the action being logged.
         * @return The builder instance.
         */
        T_BUILDER withPurpose(Purpose purpose);

        /**
         * Allows for selective logging of events.
         *
         * @param isLoggingRequired False if the work should be performed without logging an event.
         * @return The builder instance.
         */
        T_BUILDER withLoggingRequiredWhen(boolean isLoggingRequired);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface ResultSubBuilder<T_EVENT_ACTION extends EventAction, T_RESULT>
            extends OptionalMethods<T_EVENT_ACTION, ResultSubBuilder<T_EVENT_ACTION, T_RESULT>> {

        /**
         * Run loggedWork, log the event for it and return the result of the work. If logged work
         * throws an exception it will be rethrown.
         * @return The result of loggedWork
         */
        T_RESULT getResultAndLog();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface ActionSubBuilder<T_EVENT_ACTION extends EventAction>
            extends OptionalMethods<T_EVENT_ACTION, ActionSubBuilder<T_EVENT_ACTION>> {

        /**
         * Run loggedWork and log the event for it. If logged work throws an exception it will
         * be rethrown.
         */
        void runActionAndLog();
    }
}
