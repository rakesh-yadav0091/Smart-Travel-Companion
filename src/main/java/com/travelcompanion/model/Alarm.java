package com.travelcompanion.model;

import java.time.LocalTime;

/**
 * Alarm class for wake-up and departure alerts.
 *
 * @author CSY2094 Student
 */
public class Alarm {
    private String id;
    private String label;
    private LocalTime alarmTime;
    private boolean isEnabled;
    private boolean isRecurring;
    private String soundFile;

    public Alarm(String id, String label, LocalTime alarmTime) {
        this.id = id;
        this.label = label;
        this.alarmTime = alarmTime;
        this.isEnabled = true;
        this.isRecurring = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(LocalTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }

    public boolean shouldRingNow(LocalTime currentTime) {
        if (!isEnabled) {
            return false;
        }
        int currentMinutes = currentTime.getHour() * 60 + currentTime.getMinute();
        int alarmMinutes = alarmTime.getHour() * 60 + alarmTime.getMinute();
        return Math.abs(currentMinutes - alarmMinutes) <= 1;
    }

    @Override
    public String toString() {
        return "Alarm: " + label + " at " + alarmTime + (isRecurring ? " (Recurring)" : "");
    }
}
