package com.travelcompanion.model;

import java.time.LocalDateTime;

/**
 * Reminder class for user notifications.
 *
 * @author CSY2094 Student
 */
public class Reminder {
    private String id;
    private String message;
    private LocalDateTime reminderTime;
    private boolean isCompleted;

    public Reminder(String id, String message, LocalDateTime reminderTime) {
        this.id = id;
        this.message = message;
        this.reminderTime = reminderTime;
        this.isCompleted = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isDue(LocalDateTime currentTime) {
        return currentTime.isAfter(reminderTime) && !isCompleted;
    }

    @Override
    public String toString() {
        return "Reminder: " + message + " at " + reminderTime;
    }
}
