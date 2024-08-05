package event.logging.base;

import event.logging.EventAction;

public interface LoggedWorkExceptionHandler<A extends EventAction> {

   /**
    * A handler to provide a new {@link EventAction} based on the {@link Throwable}
    * and the original {@link EventAction}, e.g. where you need to add more detail to
    * the {@link EventAction} based on the work performed and the exception thrown.
    * <p>
    * This handler will not be called if the {@link Throwable} is a
    * {@link event.logging.base.impl.ValidationException}, i.e. the event is malformed.
    * </p>
    * @param eventAction The original {@link EventAction} for the logged work.
    * @param throwable The {@link Throwable} thrown by the logged work.
    * @return A new {@link EventAction} for use in the logged event.
    */
   A handle(final A eventAction, final Throwable throwable);
}
