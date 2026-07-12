package com.travelcompanion;

// Import required JavaFX classes
import com.travelcompanion.ui.MainGUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Main application entry point for Smart Travel Companion.
 * This class launches the JavaFX application.
 *
 * @author CSY2094 Student
 * @version 2.0
 */
public class MainApp extends Application {

    // Application title displayed in window header
    private static final String APP_TITLE = "Smart Travel Companion";

    /**
     * Starting point of the JavaFX application.
     * Creates the main window and displays the user interface.
     *
     * @param primaryStage The main window stage provided by JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create the main GUI component
            MainGUI mainGUI = new MainGUI();

            // Create scene with width 1400 and height 900 pixels
            Scene scene = new Scene(mainGUI, 1400, 900);

            // Apply CSS styling
            URL cssResource = getClass().getResource("/styles/app.css");
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
            }

            // Configure the window properties
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(800);

            // Display the window
            primaryStage.show();

            // Log successful startup to console
            System.out.println("Application started successfully");

        } catch (Exception error) {
            // Print error if application fails to start
            System.err.println("Failed to start application: " + error.getMessage());
            error.printStackTrace();
        }
    }

    /**
     * Main method that launches the JavaFX application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
