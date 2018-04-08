package me.konnect;

public class EventFeedException extends Exception {
    private int httpStatusCode = 404;

    public EventFeedException(String message) {
        super(message);
    }

    public EventFeedException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpErrorCode() {
        return httpStatusCode;
    }

}
