package com.travelcompanion.model;

import java.time.LocalDate;

/**
 * Expense recorded during a trip.
 *
 * @author CSY2094 Student
 */
public class Expense {
    private String id;
    private String category;
    private String description;
    private double amount;
    private LocalDate date;
    private boolean shared;

    public Expense(String id, String category, String description, double amount) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = LocalDate.now();
    }

    public Expense(String id, String description, double amount, String category, LocalDate date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public double calculatePerPersonShare(int peopleCount) {
        if (!shared || peopleCount <= 0) {
            return amount;
        }
        return amount / peopleCount;
    }

    @Override
    public String toString() {
        return description + " - Rs. " + String.format("%.0f", amount) + " (" + category + ")";
    }
}
