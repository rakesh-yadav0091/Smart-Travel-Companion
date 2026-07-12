package com.travelcompanion.model;

/**
 * NavigationInstruction represents a single step in turn-by-turn directions.
 *
 * @author CSY2094 Student
 */
public class NavigationInstruction {

    private int stepNumber;
    private String instruction;
    private String direction;
    private double distance;
    private String roadName;
    private double latitude;
    private double longitude;

    public NavigationInstruction() {
        this.distance = 0;
        this.direction = "STRAIGHT";
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFormattedInstruction() {
        return stepNumber + ". " + instruction + " on " + roadName + " (" + String.format("%.1f", distance) + " km)";
    }

    @Override
    public String toString() {
        return getFormattedInstruction();
    }
}
