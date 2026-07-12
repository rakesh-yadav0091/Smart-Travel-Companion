package com.travelcompanion.model;

import java.time.LocalTime;

/**
 * Destination class representing a place to visit.
 * Extends TravelEvent - demonstrates INHERITANCE.
 *
 * @author CSY2094 Student
 */
public class Destination extends TravelEvent {

    // Additional fields specific to destinations
    private String city;              // City name
    private String state;             // State or province
    private String country;           // Country name
    private int stayDurationHours;    // How many hours to spend here
    private double entryFee;          // Cost to enter (if any)
    private LocalTime openingTime;    // When the destination opens
    private LocalTime closingTime;    // When the destination closes
    private double rating;            // User rating from 1.0 to 5.0
    private String category;          // Temple, Museum, Nature, Adventure, etc.
    private boolean isBooked;         // Whether tickets are already booked
    private boolean visited;          // Whether the destination has been visited
    private String dressCode;         // Recommended or required dress code
    private String bestTimeToVisit;   // Best time of day or season

    /**
     * Constructor for Destination.
     *
     * @param id Unique identifier
     * @param name Name of the destination
     * @param description Description of the destination
     * @param latitude GPS latitude coordinate
     * @param longitude GPS longitude coordinate
     */
    public Destination(String id, String name, String description,
                       double latitude, double longitude) {
        // Call parent constructor
        super(id, name, description);
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = 4.0;      // Default rating
        this.isBooked = false;   // Not booked by default
        this.visited = false;    // Pending by default
    }

    /**
     * Returns the type of event (used for display).
     * Implementation of abstract method from TravelEvent.
     *
     * @return Event type string
     */
    @Override
    public String getEventType() {
        return "DESTINATION";
    }

    /**
     * Returns the duration in minutes.
     * Converts stay duration from hours to minutes.
     * Implementation of abstract method from TravelEvent.
     *
     * @return Duration in minutes
     */
    @Override
    public int getDurationMinutes() {
        return stayDurationHours * 60;
    }

    /**
     * Destinations are mandatory (not optional).
     * Implementation of abstract method from TravelEvent.
     *
     * @return false because destinations are required
     */
    @Override
    public boolean isOptional() {
        return false;
    }

    /**
     * Returns a reminder message for this destination.
     * Implementation of abstract method from TravelEvent.
     *
     * @return Reminder text
     */
    @Override
    public String getReminderMessage() {
        return "Time to visit " + name + ". " + description;
    }

    /**
     * Checks if the destination is open at the given time.
     *
     * @param currentTime The time to check
     * @return true if open, false if closed
     */
    public boolean isOpenNow(LocalTime currentTime) {
        // If opening or closing times are not set, assume always open
        if (openingTime == null || closingTime == null) {
            return true;
        }
        // Check if current time is between opening and closing
        return currentTime.isAfter(openingTime) && currentTime.isBefore(closingTime);
    }

    /**
     * Calculates minutes remaining until closing.
     *
     * @param currentTime The current time
     * @return Minutes until closing, or Long.MAX_VALUE if no closing time
     */
    public long getMinutesUntilClosing(LocalTime currentTime) {
        if (closingTime == null) {
            return Long.MAX_VALUE;
        }
        // Calculate time difference between now and closing time
        return java.time.Duration.between(currentTime, closingTime).toMinutes();
    }

    // Getter and setter methods

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getStayDurationHours() {
        return stayDurationHours;
    }

    public void setStayDurationHours(int stayDurationHours) {
        this.stayDurationHours = stayDurationHours;
    }

    public double getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(double entryFee) {
        this.entryFee = entryFee;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String getDressCode() {
        return dressCode;
    }

    public void setDressCode(String dressCode) {
        this.dressCode = dressCode;
    }

    public String getBestTimeToVisit() {
        return bestTimeToVisit;
    }

    public void setBestTimeToVisit(String bestTimeToVisit) {
        this.bestTimeToVisit = bestTimeToVisit;
    }
}
