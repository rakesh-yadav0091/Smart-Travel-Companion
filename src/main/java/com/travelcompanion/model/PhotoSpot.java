package com.travelcompanion.model;

/**
 * Photography location during a trip.
 *
 * @author CSY2094 Student
 */
public class PhotoSpot extends TravelEvent {
    private String bestTimeForPhotos;
    private int durationMinutes;

    public PhotoSpot(String id, String name, String description, double latitude, double longitude) {
        super(id, name, description);
        this.latitude = latitude;
        this.longitude = longitude;
        this.durationMinutes = 20;
    }

    @Override
    public String getEventType() {
        return "PHOTO_SPOT";
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
        return "Photo stop at " + name + ". " + description;
    }

    public String getBestTimeForPhotos() {
        return bestTimeForPhotos;
    }

    public void setBestTimeForPhotos(String bestTimeForPhotos) {
        this.bestTimeForPhotos = bestTimeForPhotos;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
