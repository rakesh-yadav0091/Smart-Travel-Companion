package com.travelcompanion.util;

/**
 * DistanceCalculator provides utility methods for distance calculations.
 * Uses Haversine formula for spherical distance between GPS coordinates.
 *
 * @author CSY2094 Student
 */
public class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calculates distance between two points using Haversine formula.
     *
     * @param lat1 Latitude of first point
     * @param lon1 Longitude of first point
     * @param lat2 Latitude of second point
     * @param lon2 Longitude of second point
     * @return Distance in kilometers
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine formula
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
            + Math.cos(lat1Rad) * Math.cos(lat2Rad)
            * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Calculates estimated travel time based on distance and speed.
     *
     * @param distanceKm Distance in kilometers
     * @param speedKmh Speed in kilometers per hour
     * @return Time in minutes
     */
    public static double calculateTravelTime(double distanceKm, double speedKmh) {
        if (speedKmh <= 0) {
            return 0;
        }
        return (distanceKm / speedKmh) * 60;
    }

    /**
     * Calculates fuel required for a journey.
     *
     * @param distanceKm Distance in kilometers
     * @param fuelEfficiencyKmpl Fuel efficiency in km per liter
     * @return Liters of fuel required
     */
    public static double calculateFuelRequired(double distanceKm, double fuelEfficiencyKmpl) {
        if (fuelEfficiencyKmpl <= 0) {
            return 0;
        }
        return distanceKm / fuelEfficiencyKmpl;
    }

    /**
     * Calculates fuel cost for a journey.
     *
     * @param distanceKm Distance in kilometers
     * @param fuelEfficiencyKmpl Fuel efficiency in km per liter
     * @param fuelPricePerLiter Price per liter in rupees
     * @return Total fuel cost in rupees
     */
    public static double calculateFuelCost(double distanceKm, double fuelEfficiencyKmpl, double fuelPricePerLiter) {
        double litersNeeded = calculateFuelRequired(distanceKm, fuelEfficiencyKmpl);
        return litersNeeded * fuelPricePerLiter;
    }
}
