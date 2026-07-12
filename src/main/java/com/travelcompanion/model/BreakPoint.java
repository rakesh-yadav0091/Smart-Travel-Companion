package com.travelcompanion.model;

/**
 * Rest stop or break point during a trip.
 *
 * @author CSY2094 Student
 */
public class BreakPoint extends TravelEvent {
    private int durationMinutes;
    private String facilities;
    private boolean foodAvailable;

    public BreakPoint(String id, String name, String description, double latitude, double longitude) {
        super(id, name, description);
        this.latitude = latitude;
        this.longitude = longitude;
        this.durationMinutes = 30;
    }

    @Override
    public String getEventType() {
        return "BREAK_POINT";
    }

    @Override
    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public String getReminderMessage() {
        return "Break at " + name + ". " + description;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public boolean isFoodAvailable() {
        return foodAvailable;
    }

    public void setFoodAvailable(boolean foodAvailable) {
        this.foodAvailable = foodAvailable;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
