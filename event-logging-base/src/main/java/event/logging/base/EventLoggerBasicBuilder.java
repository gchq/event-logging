package event.logging.base;

import event.logging.EventAction;
import event.logging.EventDetail;
import event.logging.Purpose;

import java.util.function.Supplier;

public interface EventLoggerBasicBuilder<T_EVENT_ACTION extends EventAction> {


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface TypeIdBuildStep {
        /**
         * @param typeId The typeId of the event, see {@link EventDetail#setTypeId(String)}
         * @return A builder instance.
         */
        DescriptionBuildStep withTypeId(final String typeId);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface DescriptionBuildStep {
        /**
         * @param description The description of the event, see {@link EventDetail#setDescription(String)}
         * @return A builder instance.
         */
        EventActionBuildStep withDescription(final String description);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//    interface EventActionBuildStep<T_EVENT_ACTION extends EventAction> {
//
//        WorkBuildStep<T_EVENT_ACTION> withDefaultEventAction(
//                final T_EVENT_ACTION defaultEventAction);
//    }

    interface EventActionBuildStep {

        /**
         * @param defaultEventAction The action of the logged event, see {@link EventAction}. This event action will be
         *                           used on the event unless one of the following methods is used which can override it:
         *                           {@link WorkBuildStep#withComplexLoggedAction(ComplexLoggedRunnable)}
         *                           {@link WorkBuildStep#withComplexLoggedResult(ComplexLoggedSupplier)}
         *                           {@link OptionalBuildMethods#withCustomExceptionHandler(LoggedWorkExceptionHandler)}
         * @param <T> The type of event action that will be logged, e.g.
         *        {@link event.logging.SearchEventAction}, {@link event.logging.ViewEventAction}, etc.
         * @return A builder instance.
         */
        <T extends EventAction> WorkBuildStep<T> withDefaultEventAction(
                final T defaultEventAction);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface WorkBuildStep<T_EVENT_ACTION extends EventAction> {

        <T_RESULT> EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withComplexLoggedResult(
                ComplexLoggedSupplier<T_RESULT, T_EVENT_ACTION> loggedWork);

        <T_RESULT> EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withSimpleLoggedResult(
                Supplier<T_RESULT> loggedWork);

        <T_RESULT> EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withComplexLoggedAction(
                ComplexLoggedRunnable<T_EVENT_ACTION> loggedAction);

        <T_RESULT> EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withSimpleLoggedAction(
                Runnable loggedAction);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface OptionalBuildMethods<T_EVENT_ACTION extends EventAction, T_BUILDER> {

        T_BUILDER withCustomExceptionHandler(LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler);

        T_BUILDER withPurpose(Purpose purpose);

        T_BUILDER withLoggingRequiredWhen(boolean isLoggingRequired);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface EventLoggerLoggedResultBuilder<T_EVENT_ACTION extends EventAction, T_RESULT>
            extends OptionalBuildMethods<T_EVENT_ACTION, EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT>> {

//        EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withCustomExceptionHandler(
//                LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler);
//
//        EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withPurpose(Purpose purpose);
//
//        EventLoggerLoggedResultBuilder<T_EVENT_ACTION, T_RESULT> withLoggingRequired(boolean isLoggingRequired);

        T_RESULT getResultAndLog();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    interface EventLoggerLoggedActionBuilder<T_EVENT_ACTION extends EventAction>
            extends OptionalBuildMethods<T_EVENT_ACTION, EventLoggerLoggedActionBuilder<T_EVENT_ACTION>> {

//        EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withCustomExceptionHandler(
//                LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler);
//
//        EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withPurpose(Purpose purpose);
//
//        EventLoggerLoggedActionBuilder<T_EVENT_ACTION> withLoggingRequired(boolean isLoggingRequired);

        void runActionAndLog();
    }

//    interface OptionalsBuildStep<T_EVENT_ACTION extends EventAction, T_RESULT, T_BUILDER> {
////            extends OptionalBuildMethods<T_EVENT_ACTION, OptionalsBuildStep<T_EVENT_ACTION, T_RESULT>> {
//
//        T_BUILDER withCustomExceptionHandler(LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler);
//
//        T_BUILDER withPurpose(Purpose purpose);
//
//        T_BUILDER withLoggingRequiredWhen(boolean isLoggingRequired);
//    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//    interface SupplierBuildStep<T_EVENT_ACTION extends EventAction, T_RESULT>
//            extends OptionalBuildMethods<T_EVENT_ACTION, T_RESULT> {
//
////        SupplierBuildStep<T_EVENT_ACTION, T_RESULT> withCustomExceptionHandler(
////                LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler);
////
////        SupplierBuildStep<T_EVENT_ACTION, T_RESULT> withPurpose(Purpose purpose);
//
////        SupplierBuildStep<T_EVENT_ACTION, T_RESULT> withLoggingRequired(boolean isLoggingRequired);
//
//        T_RESULT getResultAndLog();
//    }
//
//    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//    interface RunnableBuildStep<T_EVENT_ACTION extends EventAction, T_RESULT> {
//
////        RunnableBuildStep<T_EVENT_ACTION> withCustomExceptionHandler(
////                LoggedWorkExceptionHandler<T_EVENT_ACTION> exceptionHandler);
////
////        RunnableBuildStep<T_EVENT_ACTION> withPurpose(Purpose purpose);
////
////        RunnableBuildStep<T_EVENT_ACTION> withLoggingRequired(boolean isLoggingRequired);
//
//        void runActionAndLog();
//    }

//    interface StepUnTyped {
//
//        StepTyped<?> withX(final String x);
//
//    }
//
//    interface StepTyped<T2> {
//        StepTyped<T2> withY(final String x);
//    }
//
//    class TestBuilder<T1> implements StepUnTyped, StepTyped<T1> {
//
//        @Override
//        public StepTyped<?> withX(final String x) {
//            return this;
//        }
//
//        @Override
//        public StepTyped<T1> withY(final String x) {
//            return this;
//        }
//    }

}
