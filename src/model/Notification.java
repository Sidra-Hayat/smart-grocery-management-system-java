package model;

import java.time.LocalDateTime;

public class Notification {

    public enum Type {LOW_STOCK, OTHER}
    private LocalDateTime timestamp;
    private String message;
    private Type type;

    public Notification(String message, Type type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
