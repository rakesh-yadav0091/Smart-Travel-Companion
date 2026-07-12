package com.travelcompanion.dao;

import com.travelcompanion.model.Trip;
import com.travelcompanion.model.Expense;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Trip Data Access Object.
 * Handles all CRUD (Create, Read, Update, Delete) operations for Trip objects.
 * Data is stored in CSV files for simplicity.
 *
 * @author CSY2094 Student
 */
public class TripDao implements Persistable<Trip> {

    // File paths for data storage
    private static final String DATA_DIRECTORY = "./data/";
    private static final String TRIPS_FILE_PATH = DATA_DIRECTORY + "trips.csv";
    private static final String EXPENSES_FILE_PATH = DATA_DIRECTORY + "expenses.csv";

    /**
     * Constructor creates the data directory if it doesn't exist.
     */
    public TripDao() {
        createDataDirectory();
    }

    public void saveTrip(Trip trip) {
        if (findById(trip.getTripId()) == null) {
            save(trip);
        } else {
            update(trip);
        }
    }

    public void addExpense(Expense expense, String tripId) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(EXPENSES_FILE_PATH, true))) {
            String[] record = {
                tripId,
                expense.getId(),
                expense.getDescription(),
                String.valueOf(expense.getAmount()),
                expense.getCategory(),
                expense.getDate().toString(),
                String.valueOf(expense.isShared())
            };
            writer.writeNext(record);
            System.out.println("Expense saved successfully: " + expense.getDescription());
        } catch (IOException error) {
            System.err.println("Failed to save expense: " + error.getMessage());
        }
    }

    public List<Expense> getExpensesForTrip(String tripId) {
        List<Expense> expenses = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(EXPENSES_FILE_PATH))) {
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                if (record.length >= 7 && record[0].equals(tripId)) {
                    Expense expense = new Expense(
                        record[1],
                        record[2],
                        Double.parseDouble(record[3]),
                        record[4],
                        LocalDate.parse(record[5])
                    );
                    expense.setShared(Boolean.parseBoolean(record[6]));
                    expenses.add(expense);
                }
            }
        } catch (IOException | CsvException error) {
            System.out.println("No existing expenses found for trip: " + tripId);
        }

        return expenses;
    }

    /**
     * Creates the data directory for storing CSV files.
     * Called automatically when the DAO is instantiated.
     */
    private void createDataDirectory() {
        try {
            Path dataPath = Paths.get(DATA_DIRECTORY);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println("Created data directory: " + DATA_DIRECTORY);
            }
        } catch (IOException error) {
            System.err.println("Failed to create data directory: " + error.getMessage());
        }
    }

    /**
     * Saves a trip to the CSV file.
     * Implements the CREATE operation of CRUD.
     *
     * @param trip The trip to save
     */
    @Override
    public void save(Trip trip) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(TRIPS_FILE_PATH, true))) {
            String[] record = {
                trip.getTripId(),
                trip.getName(),
                trip.getStartDate().toString(),
                trip.getEndDate().toString(),
                String.valueOf(trip.getTotalBudget()),
                String.valueOf(trip.getCurrentSpend()),
                trip.getVehicleType(),
                String.valueOf(trip.getPeopleCount()),
                String.valueOf(trip.getTotalDistance())
            };
            writer.writeNext(record);
            System.out.println("Trip saved successfully: " + trip.getName());
        } catch (IOException error) {
            System.err.println("Failed to save trip: " + error.getMessage());
        }
    }

    /**
     * Finds a trip by its unique ID.
     * Implements the READ operation of CRUD.
     *
     * @param tripId The unique identifier to search for
     * @return The found Trip object, or null if not found
     */
    @Override
    public Trip findById(String tripId) {
        try (CSVReader reader = new CSVReader(new FileReader(TRIPS_FILE_PATH))) {
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                if (record[0].equals(tripId)) {
                    Trip trip = new Trip(
                        record[0],
                        record[1],
                        LocalDate.parse(record[2]),
                        LocalDate.parse(record[3]),
                        Double.parseDouble(record[4])
                    );
                    trip.setCurrentSpend(Double.parseDouble(record[5]));
                    trip.setVehicleType(record[6]);
                    trip.setPeopleCount(Integer.parseInt(record[7]));
                    trip.setTotalDistance(Double.parseDouble(record[8]));
                    return trip;
                }
            }
        } catch (IOException | CsvException error) {
            System.err.println("Error finding trip: " + error.getMessage());
        }
        return null;  // Return null if trip not found
    }

    /**
     * Retrieves all trips from storage.
     * Implements the READ ALL operation of CRUD.
     *
     * @return List of all Trip objects
     */
    @Override
    public List<Trip> findAll() {
        List<Trip> trips = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(TRIPS_FILE_PATH))) {
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                Trip trip = new Trip(
                    record[0],
                    record[1],
                    LocalDate.parse(record[2]),
                    LocalDate.parse(record[3]),
                    Double.parseDouble(record[4])
                );
                trip.setCurrentSpend(Double.parseDouble(record[5]));
                trip.setVehicleType(record[6]);
                trip.setPeopleCount(Integer.parseInt(record[7]));
                trip.setTotalDistance(Double.parseDouble(record[8]));
                trips.add(trip);
            }
        } catch (IOException | CsvException error) {
            // File may not exist yet - that's fine
            System.out.println("No existing trips found. Starting fresh.");
        }

        return trips;
    }

    /**
     * Updates an existing trip in storage.
     * Implements the UPDATE operation of CRUD.
     *
     * @param trip The trip with updated data
     */
    @Override
    public void update(Trip trip) {
        List<Trip> allTrips = findAll();

        try (CSVWriter writer = new CSVWriter(new FileWriter(TRIPS_FILE_PATH))) {
            for (Trip existingTrip : allTrips) {
                if (!existingTrip.getTripId().equals(trip.getTripId())) {
                    writeTripToWriter(writer, existingTrip);
                }
            }
            writeTripToWriter(writer, trip);
            System.out.println("Trip updated successfully: " + trip.getName());
        } catch (IOException error) {
            System.err.println("Failed to update trip: " + error.getMessage());
        }
    }

    /**
     * Deletes a trip from storage.
     * Implements the DELETE operation of CRUD.
     *
     * @param tripId The ID of the trip to delete
     */
    @Override
    public void delete(String tripId) {
        List<Trip> allTrips = findAll();

        try (CSVWriter writer = new CSVWriter(new FileWriter(TRIPS_FILE_PATH))) {
            for (Trip trip : allTrips) {
                if (!trip.getTripId().equals(tripId)) {
                    writeTripToWriter(writer, trip);
                }
            }
            System.out.println("Trip deleted successfully: " + tripId);
        } catch (IOException error) {
            System.err.println("Failed to delete trip: " + error.getMessage());
        }
    }

    /**
     * Helper method to write a single trip to the CSV writer.
     *
     * @param writer The CSVWriter to use
     * @param trip The trip to write
     */
    private void writeTripToWriter(CSVWriter writer, Trip trip) {
        String[] record = {
            trip.getTripId(),
            trip.getName(),
            trip.getStartDate().toString(),
            trip.getEndDate().toString(),
            String.valueOf(trip.getTotalBudget()),
            String.valueOf(trip.getCurrentSpend()),
            trip.getVehicleType(),
            String.valueOf(trip.getPeopleCount()),
            String.valueOf(trip.getTotalDistance())
        };
        writer.writeNext(record);
    }
}
