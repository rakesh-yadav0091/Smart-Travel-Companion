package com.travelcompanion.service;

import com.travelcompanion.model.NavigationInstruction;
import com.travelcompanion.util.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * NavigationService provides route calculation and turn-by-turn directions.
 *
 * @author CSY2094 Student
 */
public class NavigationService {

    private static final double AVERAGE_SPEED_CITY = 40.0; // km/h
    private static final double AVERAGE_SPEED_HIGHWAY = 80.0; // km/h
    private static final double AVERAGE_SPEED_BIKE = 50.0; // km/h

    /**
     * Calculates route between two points.
     *
     * @param startLat Start latitude
     * @param startLon Start longitude
     * @param endLat End latitude
     * @param endLon End longitude
     * @param vehicleType Type of vehicle (car, bike, etc.)
     * @return Route details as string
     */
    public RouteResult calculateRoute(double startLat, double startLon,
                                      double endLat, double endLon,
                                      String vehicleType) {
        RouteResult result = new RouteResult();

        // Calculate distance using Haversine formula
        double distance = DistanceCalculator.calculateDistance(startLat, startLon, endLat, endLon);
        result.setDistanceKm(distance);

        // Calculate estimated time based on vehicle type
        double speed = getAverageSpeed(vehicleType);
        double timeHours = distance / speed;
        result.setEstimatedTimeMinutes(timeHours * 60);
        result.setEstimatedTimeHours(timeHours);

        // Generate turn-by-turn instructions
        List<NavigationInstruction> instructions = generateInstructions(startLat, startLon, endLat, endLon, distance);
        result.setInstructions(instructions);

        // Calculate fuel cost estimate
        double fuelCost = calculateFuelCost(distance, vehicleType);
        result.setEstimatedFuelCost(fuelCost);

        return result;
    }

    /**
     * Gets average speed based on vehicle type.
     */
    private double getAverageSpeed(String vehicleType) {
        if (vehicleType == null) {
            return AVERAGE_SPEED_CITY;
        }

        String lowerType = vehicleType.toLowerCase();
        if (lowerType.contains("bike") || lowerType.contains("motorcycle")) {
            return AVERAGE_SPEED_BIKE;
        } else if (lowerType.contains("highway")) {
            return AVERAGE_SPEED_HIGHWAY;
        } else {
            return AVERAGE_SPEED_CITY;
        }
    }

    /**
     * Calculates estimated fuel cost.
     */
    private double calculateFuelCost(double distanceKm, String vehicleType) {
        double fuelEfficiency; // km per liter
        double fuelPrice = 96.0; // rupees per liter

        if (vehicleType != null && vehicleType.toLowerCase().contains("bike")) {
            fuelEfficiency = 45.0; // Bike gives 45 km/l
        } else {
            fuelEfficiency = 15.0; // Car gives 15 km/l
        }

        double litersNeeded = distanceKm / fuelEfficiency;
        return litersNeeded * fuelPrice;
    }

    /**
     * Generates turn-by-turn navigation instructions.
     */
    private List<NavigationInstruction> generateInstructions(double startLat, double startLon,
                                                             double endLat, double endLon,
                                                             double totalDistance) {
        List<NavigationInstruction> instructions = new ArrayList<>();

        // Start instruction
        NavigationInstruction start = new NavigationInstruction();
        start.setStepNumber(1);
        start.setInstruction("Start from your location");
        start.setDirection("START");
        start.setDistance(0);
        start.setRoadName("Starting Point");
        instructions.add(start);

        // Calculate number of turns based on distance
        int numberOfTurns = Math.max(2, (int) (totalDistance / 50) + 1);
        double segmentDistance = totalDistance / numberOfTurns;

        double currentLat = startLat;
        double currentLon = startLon;
        double latStep = (endLat - startLat) / numberOfTurns;
        double lonStep = (endLon - startLon) / numberOfTurns;

        for (int i = 1; i <= numberOfTurns; i++) {
            NavigationInstruction instruction = new NavigationInstruction();
            instruction.setStepNumber(i + 1);

            // Alternate between different turn types
            if (i % 3 == 0) {
                instruction.setDirection("LEFT");
                instruction.setInstruction("Take left turn");
            } else if (i % 3 == 1) {
                instruction.setDirection("RIGHT");
                instruction.setInstruction("Take right turn");
            } else {
                instruction.setDirection("STRAIGHT");
                instruction.setInstruction("Continue straight");
            }

            instruction.setDistance(segmentDistance);
            instruction.setRoadName("Road Segment " + i);
            instruction.setLatitude(currentLat + latStep * i);
            instruction.setLongitude(currentLon + lonStep * i);

            instructions.add(instruction);
        }

        // Arrival instruction
        NavigationInstruction arrival = new NavigationInstruction();
        arrival.setStepNumber(instructions.size() + 1);
        arrival.setInstruction("You have arrived at your destination");
        arrival.setDirection("ARRIVE");
        arrival.setDistance(0);
        arrival.setRoadName("Destination");
        arrival.setLatitude(endLat);
        arrival.setLongitude(endLon);
        instructions.add(arrival);

        return instructions;
    }

    /**
     * Gets speed limit for a road (simulated).
     */
    public int getSpeedLimit(double latitude, double longitude) {
        // Simulate speed limits based on coordinates
        // In real implementation, this would query a roads API
        if (isHighway(latitude, longitude)) {
            return 80;
        } else if (isCityRoad(latitude, longitude)) {
            return 50;
        } else if (isResidential(latitude, longitude)) {
            return 30;
        } else {
            return 60;
        }
    }

    /**
     * Checks if location is on highway (simulated).
     */
    private boolean isHighway(double lat, double lon) {
        // Simplified simulation - in real app, check against map data
        return lat > 28.0 && lat < 31.0;
    }

    /**
     * Checks if location is on city road (simulated).
     */
    private boolean isCityRoad(double lat, double lon) {
        return lat > 19.0 && lat < 20.0;
    }

    /**
     * Checks if location is residential (simulated).
     */
    private boolean isResidential(double lat, double lon) {
        return !isHighway(lat, lon) && !isCityRoad(lat, lon);
    }

    /**
     * Inner class for route calculation results.
     */
    public static class RouteResult {
        private double distanceKm;
        private double estimatedTimeMinutes;
        private double estimatedTimeHours;
        private double estimatedFuelCost;
        private List<NavigationInstruction> instructions;

        public double getDistanceKm() {
            return distanceKm;
        }

        public void setDistanceKm(double distanceKm) {
            this.distanceKm = distanceKm;
        }

        public double getEstimatedTimeMinutes() {
            return estimatedTimeMinutes;
        }

        public void setEstimatedTimeMinutes(double estimatedTimeMinutes) {
            this.estimatedTimeMinutes = estimatedTimeMinutes;
        }

        public double getEstimatedTimeHours() {
            return estimatedTimeHours;
        }

        public void setEstimatedTimeHours(double estimatedTimeHours) {
            this.estimatedTimeHours = estimatedTimeHours;
        }

        public double getEstimatedFuelCost() {
            return estimatedFuelCost;
        }

        public void setEstimatedFuelCost(double estimatedFuelCost) {
            this.estimatedFuelCost = estimatedFuelCost;
        }

        public List<NavigationInstruction> getInstructions() {
            return instructions;
        }

        public void setInstructions(List<NavigationInstruction> instructions) {
            this.instructions = instructions;
        }

        public String getFormattedSummary() {
            return String.format(
                "Distance: %.1f km\nEstimated Time: %.0f minutes (%.1f hours)\nFuel Cost: Rs. %.0f",
                distanceKm, estimatedTimeMinutes, estimatedTimeHours, estimatedFuelCost
            );
        }
    }
}
