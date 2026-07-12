package com.travelcompanion.dao;

/**
 * Interface for objects that can be saved to persistent storage.
 * Demonstrates the INTERFACE concept in object-oriented programming.
 * Any class that implements this interface must provide the CRUD methods.
 *
 * @author CSY2094 Student
 * @param <T> The type of object being persisted
 */
public interface Persistable<T> {

    /**
     * Saves an object to persistent storage.
     *
     * @param object The object to save
     */
    void save(T object);

    /**
     * Finds an object by its unique identifier.
     *
     * @param id The unique identifier to search for
     * @return The found object, or null if not found
     */
    T findById(String id);

    /**
     * Retrieves all objects from persistent storage.
     *
     * @return List of all objects
     */
    java.util.List<T> findAll();

    /**
     * Updates an existing object in persistent storage.
     *
     * @param object The object with updated data
     */
    void update(T object);

    /**
     * Deletes an object from persistent storage.
     *
     * @param id The unique identifier of the object to delete
     */
    void delete(String id);
}
