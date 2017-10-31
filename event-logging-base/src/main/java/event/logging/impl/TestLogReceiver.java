package event.logging.impl;

public  class TestLogReceiver implements LogReceiver {
    private static String message = "";

    @Override
    public void log(final String message) {
        this.message = message;
    }

    public static String getCurrentMessage() {
        final String currentMessage = message;
        message = "";
        return currentMessage;
    }
}