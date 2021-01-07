package event.logging.example;

/**
 * A very simplistic (single user only) context object for holding the currently logged
 * in user and the session scoped justification. This would normally be some session or request
 * scoped object made available to the logging service.
 */
public class UserContext {
    private String userId;
    private String justification;

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(final String justification) {
        this.justification = justification;
    }
}
