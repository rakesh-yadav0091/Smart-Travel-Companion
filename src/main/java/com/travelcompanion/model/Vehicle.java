package com.travelcompanion.model;

/**
 * Vehicle details for a trip.
 *
 * @author CSY2094 Student
 */
public class Vehicle {
    private String id;
    private String type;
    private String registrationNumber;
    private double fuelEfficiency;
    private int seatingCapacity;

    public Vehicle(String id, String type, String registrationNumber) {
        this.id = id;
        this.type = type;
        this.registrationNumber = registrationNumber;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public double getFuelEfficiency() {
        return fuelEfficiency;
    }

    public void setFuelEfficiency(double fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }
}
