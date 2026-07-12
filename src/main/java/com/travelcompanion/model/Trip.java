package com.travelcompanion.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Trip class - Main container class that holds all travel data.
 * Demonstrates COMPOSITION - contains multiple other objects.
 *
 * @author CSY2094 Student
 */
public class Trip {

    // Basic trip information
    private String tripId;           // Unique identifier
    private String name;             // Trip name (e.g., "Manali Road Trip")
    private LocalDate startDate;     // Trip start date
    private LocalDate endDate;       // Trip end date
    private double totalBudget;      // Planned budget in rupees
    private double currentSpend;     // Money spent so far
    private String vehicleType;      // Car, Bike, Bus, Train
    private int peopleCount;         // Number of travelers
    private double totalDistance;    // Total distance in kilometers

    // Collections of related objects (COMPOSITION)
    private List<Destination> destinations;   // Places to visit
    private List<BreakPoint> breakPoints;     // Rest stops
    private List<PhotoSpot> photoSpots;       // Photography locations
    private List<Expense> expenses;           // Trip expenses
    private List<Person> travellers;          // People traveling
    private Vehicle vehicle;                  // Vehicle details
    private List<Reminder> reminders;         // User reminders
    private List<Alarm> alarms;               // Scheduled alarms

    // Navigation tracking data
    private List<NavigationInstruction> navigationInstructions;
    private double currentLatitude;
    private double currentLongitude;

    // Statistics for post-trip analysis
    private int onTimeDepartureCount;
    private int lateArrivalCount;
    private List<String> missedAttractions;
    private int totalPhotosTaken;
    private boolean isPhoneDamaged;
    private boolean isBookedInAdvance;
    private double lastMinutePremium;
    private int wrongTurnCount;

    /**
     * Constructor for creating a new Trip.
     *
     * @param tripId Unique identifier for the trip
     * @param name Name of the trip
     * @param startDate Trip start date
     * @param endDate Trip end date
     * @param totalBudget Total budget in rupees
     */
    public Trip(String tripId, String name, LocalDate startDate,
                LocalDate endDate, double totalBudget) {
        this.tripId = tripId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalBudget = totalBudget;
        this.currentSpend = 0;
        this.peopleCount = 1;
        this.totalDistance = 0;

        // Initialize all collections as empty lists
        this.destinations = new ArrayList<>();
        this.breakPoints = new ArrayList<>();
        this.photoSpots = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.travellers = new ArrayList<>();
        this.reminders = new ArrayList<>();
        this.alarms = new ArrayList<>();
        this.navigationInstructions = new ArrayList<>();
        this.missedAttractions = new ArrayList<>();
    }

    /**
     * Adds an expense to the trip and updates current spending.
     *
     * @param expense The expense to add
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
        currentSpend += expense.getAmount();
    }

    /**
     * Calculates remaining budget.
     *
     * @return Amount left to spend
     */
    public double getRemainingBudget() {
        return totalBudget - currentSpend;
    }

    /**
     * Calculates what percentage of budget has been used.
     *
     * @return Percentage from 0 to 100
     */
    public double getBudgetUtilizationPercentage() {
        if (totalBudget == 0) {
            return 0;
        }
        return (currentSpend / totalBudget) * 100;
    }

    /**
     * Checks if trip is under budget.
     *
     * @return true if spending is less than or equal to budget
     */
    public boolean isUnderBudget() {
        return currentSpend <= totalBudget;
    }

    /**
     * Checks if trip is over budget.
     *
     * @return true if spending exceeds budget
     */
    public boolean isOverBudget() {
        return currentSpend > totalBudget;
    }

    /**
     * Gets warning threshold for budget alerts.
     * Returns 90 for critical warning, 75 for normal warning.
     *
     * @return Threshold percentage, or 0 if no warning needed
     */
    public double getBudgetWarningThreshold() {
        double utilization = getBudgetUtilizationPercentage();
        if (utilization > 90) {
            return 90;  // Critical - almost out of budget
        } else if (utilization > 75) {
            return 75;  // Warning - approaching budget limit
        }
        return 0;  // No warning needed
    }

    /**
     * Calculates trip duration in days.
     * Includes both start and end dates.
     *
     * @return Number of days
     */
    public int getDurationDays() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    /**
     * Adds a destination to the trip.
     *
     * @param destination The destination to add
     */
    public void addDestination(Destination destination) {
        this.destinations.add(destination);
    }

    /**
     * Adds a break point to the trip.
     *
     * @param breakPoint The break point to add
     */
    public void addBreakPoint(BreakPoint breakPoint) {
        this.breakPoints.add(breakPoint);
    }

    /**
     * Adds a photo spot to the trip.
     *
     * @param photoSpot The photo spot to add
     */
    public void addPhotoSpot(PhotoSpot photoSpot) {
        this.photoSpots.add(photoSpot);
    }

    /**
     * Adds a person to the trip.
     *
     * @param person The person to add
     */
    public void addTraveller(Person person) {
        this.travellers.add(person);
    }

    /**
     * Increments the on-time departure counter.
     */
    public void incrementOnTimeDeparture() {
        this.onTimeDepartureCount++;
    }

    /**
     * Increments the late arrival counter.
     */
    public void incrementLateArrival() {
        this.lateArrivalCount++;
    }

    /**
     * Adds an attraction that was missed.
     *
     * @param attraction Name of missed attraction
     */
    public void addMissedAttraction(String attraction) {
        this.missedAttractions.add(attraction);
    }

    /**
     * Increments the photo counter.
     */
    public void incrementPhotosTaken() {
        this.totalPhotosTaken++;
    }

    /**
     * Increments the wrong turn counter.
     */
    public void incrementWrongTurn() {
        this.wrongTurnCount++;
    }

    /**
     * Calculates total luggage weight across all travelers.
     *
     * @return Total weight in kilograms
     */
    public double getTotalLuggageWeight() {
        double total = 0;
        for (Person person : travellers) {
            total += person.getLuggageWeight();
        }
        return total;
    }

    /**
     * Gets total number of attractions (destinations + photo spots).
     *
     * @return Total attraction count
     */
    public int getTotalAttractions() {
        return destinations.size() + photoSpots.size();
    }

    // Getter methods

    public String getTripId() {
        return tripId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public double getCurrentSpend() {
        return currentSpend;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public List<BreakPoint> getBreakPoints() {
        return breakPoints;
    }

    public List<PhotoSpot> getPhotoSpots() {
        return photoSpots;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<Person> getTravellers() {
        return travellers;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public List<NavigationInstruction> getNavigationInstructions() {
        return navigationInstructions;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public int getOnTimeDepartureCount() {
        return onTimeDepartureCount;
    }

    public int getLateArrivalCount() {
        return lateArrivalCount;
    }

    public List<String> getMissedAttractions() {
        return missedAttractions;
    }

    public int getTotalPhotosTaken() {
        return totalPhotosTaken;
    }

    public boolean isPhoneDamaged() {
        return isPhoneDamaged;
    }

    public boolean isBookedInAdvance() {
        return isBookedInAdvance;
    }

    public double getLastMinutePremium() {
        return lastMinutePremium;
    }

    public int getWrongTurnCount() {
        return wrongTurnCount;
    }

    // Setter methods

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public void setCurrentSpend(double currentSpend) {
        this.currentSpend = currentSpend;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setNavigationInstructions(List<NavigationInstruction> instructions) {
        this.navigationInstructions = instructions;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public void setOnTimeDepartureCount(int onTimeDepartureCount) {
        this.onTimeDepartureCount = onTimeDepartureCount;
    }

    public void setLateArrivalCount(int lateArrivalCount) {
        this.lateArrivalCount = lateArrivalCount;
    }

    public void setTotalPhotosTaken(int totalPhotosTaken) {
        this.totalPhotosTaken = totalPhotosTaken;
    }

    public void setPhoneDamaged(boolean phoneDamaged) {
        isPhoneDamaged = phoneDamaged;
    }

    public void setBookedInAdvance(boolean bookedInAdvance) {
        isBookedInAdvance = bookedInAdvance;
    }

    public void setLastMinutePremium(double lastMinutePremium) {
        this.lastMinutePremium = lastMinutePremium;
    }

    public void setWrongTurnCount(int wrongTurnCount) {
        this.wrongTurnCount = wrongTurnCount;
    }

    /**
     * Returns a string representation of the trip.
     *
     * @return Formatted string with trip details
     */
    @Override
    public String toString() {
        return String.format("Trip: %s | Start: %s | End: %s | Budget: %.0f | Spent: %.0f | Days: %d",
            name, startDate, endDate, totalBudget, currentSpend, getDurationDays());
    }
}
