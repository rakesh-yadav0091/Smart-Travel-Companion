package com.travelcompanion.model;

import java.time.LocalDate;

/**
 * Traffic violation or fine recorded during the trip.
 *
 * @author CSY2094 Student
 */
public class TrafficViolation {
    private String id;
    private String type;
    private double fineAmount;
    private LocalDate date;

    public TrafficViolation(String id, String type, double fineAmount) {
        this.id = id;
        this.type = type;
        this.fineAmount = fineAmount;
        this.date = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
