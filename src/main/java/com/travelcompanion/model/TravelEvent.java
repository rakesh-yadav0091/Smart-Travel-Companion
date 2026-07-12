package com.travelcompanion.model;

import java.time.LocalDateTime;

/**
 * Abstract base class for all travel-related events.
 * Demonstrates the concept of ABSTRACTION in object-oriented programming.
 * This class cannot be instantiated directly; it must be extended.
 *
 * @author CSY2094 Student
 */
public abstract class TravelEvent {

    // Protected fields - accessible to subclasses
    protected String id;              // Unique identifier for the event
    protected String name;            // Display name of the event
    protected String description;      // Detailed description
    protected LocalDateTime startTime; // When the event begins
    protected LocalDateTime endTime;   // When the event ends
    protected double latitude;         // GPS latitude coordinate
    protected double longitude;        // GPS longitude coordinate
    protected double cost;             // Estimated or actual cost

    /**
     * Constructor for TravelEvent.
     *
     * @param id Unique identifier
     * @param name Display name
     * @param description Detailed description
     */
    public TravelEvent(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Abstract method - must be implemented by subclasses.
     * Returns the type of event (Destination, BreakPoint, PhotoSpot, etc.)
     *
     * @return String representing event type
     */
    public abstract String getEventType();

    /**
     * Abstract method - must be implemented by subclasses.
     * Returns the duration of the event in minutes.
     *
     * @return Duration in minutes
     */
    public abstract int getDurationMinutes();

    /**
     * Abstract method - must be implemented by subclasses.
     * Indicates whether this event is optional or mandatory.
     *
     * @return true if optional, false if mandatory
     */
    public abstract boolean isOptional();

    /**
     * Abstract method - must be implemented by subclasses.
     * Returns a reminder message for this event.
     *
     * @return Reminder text
     */
    public abstract String getReminderMessage();

    // Getter methods (accessors)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getCost() {
        return cost;
    }

    // Setter methods (mutators)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Returns a string representation of the travel event.
     *
     * @return Formatted string with event details
     */
    @Override
    public String toString() {
        return String.format("%s: %s (%d minutes) - %s",
            getEventType(), name, getDurationMinutes(), description);
    }
}
