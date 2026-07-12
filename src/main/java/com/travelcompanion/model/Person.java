package com.travelcompanion.model;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String personId;
    private String name;
    private int age;
    private String gender;
    private boolean canDrive;
    private double luggageWeight;
    private List<String> specialNeeds;

    public Person(String personId, String name, int age) {
        this(personId, name, age, "Not specified");
    }

    public Person(String personId, String name, int age, String gender) {
        this.personId = personId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.canDrive = false;
        this.luggageWeight = 0;
        this.specialNeeds = new ArrayList<>();
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isCanDrive() {
        return canDrive;
    }

    public void setCanDrive(boolean canDrive) {
        this.canDrive = canDrive;
    }

    public double getLuggageWeight() {
        return luggageWeight;
    }

    public void setLuggageWeight(double luggageWeight) {
        this.luggageWeight = luggageWeight;
    }

    public List<String> getSpecialNeeds() {
        return specialNeeds;
    }

    public void addSpecialNeed(String need) {
        this.specialNeeds.add(need);
    }

    public boolean isSenior() {
        return age > 60;
    }

    public boolean isChild() {
        return age < 12;
    }

    @Override
    public String toString() {
        return name + " (" + age + ", " + gender + ")";
    }
}
