package com.travelcompanion.ui;

import com.travelcompanion.model.Trip;
import com.travelcompanion.model.Destination;
import com.travelcompanion.model.Expense;
import com.travelcompanion.model.WeatherData;
import com.travelcompanion.model.ClothingAdvice;
import com.travelcompanion.service.WeatherService;
import com.travelcompanion.service.ClothingRecommender;
import com.travelcompanion.service.NavigationService;
import com.travelcompanion.dao.TripDao;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDate;

/**
 * Main GUI class for Smart Travel Companion.
 * Contains all user interface components and event handlers.
 *
 * @author CSY2094 Student
 */
public class MainGUI extends BorderPane {

    private Trip currentTrip;
    private WeatherService weatherService;
    private ClothingRecommender clothingRecommender;
    private NavigationService navigationService;
    private TripDao tripDao;

    private TabPane tabPane;
    private Label statusLabel;
    private Text titleText;
    private Text weatherDisplayText;
    private ListView<String> destinationListView;
    private ListView<String> expenseListView;

    public MainGUI() {
        initializeServices();
        initializeUI();
        setupSampleData();
        startAmbientAnimations();
    }

    private void initializeServices() {
        weatherService = new WeatherService();
        clothingRecommender = new ClothingRecommender();
        navigationService = new NavigationService();
        tripDao = new TripDao();
    }

    private void initializeUI() {
        setStyle("-fx-background-color: #1a1a2e;");

        VBox header = createHeader();
        setTop(header);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: transparent;");

        tabPane.getTabs().addAll(
            createDashboardTab(),
            createTripTab(),
            createWeatherTab(),
            createNavigationTab(),
            createExpenseTab(),
            createSettingsTab()
        );
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && newTab.getContent() != null) {
                animatePanel(newTab.getContent());
            }
        });

        setCenter(tabPane);

        HBox statusBar = createStatusBar();
        setBottom(statusBar);
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #0f0f23; -fx-border-color: #ffd700; -fx-border-width: 0 0 2 0;");

        titleText = new Text("Smart Travel Companion");
        titleText.setFont(Font.font("Arial", 24));
        titleText.setStyle("-fx-fill: #ffd700; -fx-font-weight: bold;");

        Text subtitle = new Text("Plan, Track, and Optimize Your Journey");
        subtitle.setStyle("-fx-fill: #aaaaaa; -fx-font-size: 12px;");

        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        Button newTripBtn = createStyledButton("New Trip", "#4CAF50");
        Button saveTripBtn = createStyledButton("Save Trip", "#2196F3");
        Button loadTripBtn = createStyledButton("Load Trip", "#FF9800");

        newTripBtn.setOnAction(e -> showNewTripDialog());
        saveTripBtn.setOnAction(e -> saveCurrentTrip());
        loadTripBtn.setOnAction(e -> loadTrip());

        buttonBar.getChildren().addAll(newTripBtn, saveTripBtn, loadTripBtn);

        HBox topRow = new HBox(20);
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.getChildren().addAll(titleText, buttonBar);
        HBox.setHgrow(buttonBar, Priority.ALWAYS);

        header.getChildren().addAll(topRow, subtitle);
        return header;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox(15);
        statusBar.setPadding(new Insets(10, 20, 10, 20));
        statusBar.setStyle("-fx-background-color: #0f0f23; -fx-border-color: #ffd700; -fx-border-width: 2 0 0 0;");

        statusLabel = new Label("Ready - No active trip");
        statusLabel.setStyle("-fx-text-fill: #aaaaaa;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label versionLabel = new Label("Version 2.0 | CSY2094");
        versionLabel.setStyle("-fx-text-fill: #aaaaaa;");

        statusBar.getChildren().addAll(statusLabel, spacer, versionLabel);
        return statusBar;
    }

    private Tab createDashboardTab() {
        Tab tab = new Tab("Dashboard");
        tab.setClosable(false);

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1a1a2e;");

        Label welcomeLabel = new Label("Welcome to Smart Travel Companion");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        Label infoLabel = new Label(
            "This application helps you plan and manage your trips with features including:\n"
                + "  Trip Planning and Management\n"
                + "  Weather Forecast and Clothing Advice\n"
                + "  Route Planning with Distance and Time Calculation\n"
                + "  Expense Tracking and Budget Management\n"
                + "  Data Persistence with CSV Files"
        );
        infoLabel.setStyle("-fx-text-fill: white; -fx-wrap-text: true;");
        infoLabel.setWrapText(true);

        VBox statusCard = new VBox(10);
        statusCard.setPadding(new Insets(15));
        statusCard.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");
        applyCardEffect(statusCard);

        Label statusHeader = new Label("Current Trip Status");
        statusHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        Label tripStatusLabel = new Label();
        tripStatusLabel.setStyle("-fx-text-fill: white;");

        if (currentTrip == null) {
            tripStatusLabel.setText("No active trip. Click 'New Trip' to create one.");
        } else {
            tripStatusLabel.setText(
                "Trip Name: " + currentTrip.getName() + "\n"
                    + "Dates: " + currentTrip.getStartDate() + " to " + currentTrip.getEndDate() + "\n"
                    + "Budget: Rs. " + String.format("%.0f", currentTrip.getTotalBudget()) + "\n"
                    + "Spent: Rs. " + String.format("%.0f", currentTrip.getCurrentSpend()) + "\n"
                    + "Remaining: Rs. " + String.format("%.0f", currentTrip.getRemainingBudget())
            );
        }

        ProgressBar budgetProgress = new ProgressBar(
            currentTrip == null ? 0 : currentTrip.getBudgetUtilizationPercentage() / 100
        );
        budgetProgress.setPrefWidth(420);
        budgetProgress.setStyle("-fx-accent: #ffd700;");

        statusCard.getChildren().addAll(statusHeader, tripStatusLabel, budgetProgress);

        VBox statsCard = new VBox(10);
        statsCard.setPadding(new Insets(15));
        statsCard.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");
        applyCardEffect(statsCard);

        Label statsHeader = new Label("Quick Statistics");
        statsHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        Label statsLabel = new Label(
            "Total Features: 25+\n"
                + "Weather API: Open-Meteo (Free)\n"
                + "Data Storage: CSV Files\n"
                + "Navigation: Distance Calculation\n"
                + "Platform: JavaFX"
        );
        statsLabel.setStyle("-fx-text-fill: white;");

        statsCard.getChildren().addAll(statsHeader, statsLabel);

        VBox notificationCard = new VBox(10);
        notificationCard.setPadding(new Insets(15));
        notificationCard.setStyle("-fx-background-color: #203a55; -fx-border-radius: 10;");
        applyCardEffect(notificationCard);

        Label notificationHeader = new Label("Smart Notifications");
        notificationHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        Label notificationLabel = new Label(getDashboardNotification());
        notificationLabel.setStyle("-fx-text-fill: white;");
        notificationLabel.setWrapText(true);

        notificationCard.getChildren().addAll(notificationHeader, notificationLabel);

        content.getChildren().addAll(welcomeLabel, infoLabel, statusCard, statsCard, notificationCard);

        ScrollPane scrollPane = new ScrollPane(content);
        animatePanel(scrollPane);
        tab.setContent(scrollPane);
        return tab;
    }

    private Tab createTripTab() {
        Tab tab = new Tab("Trip Planner");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1a1a2e;");

        Label header = new Label("Plan Your Journey");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");

        form.add(new Label("Trip Name:"), 0, 0);
        TextField nameField = new TextField();
        nameField.setPromptText("Enter trip name");
        form.add(nameField, 1, 0);

        form.add(new Label("Start Date:"), 2, 0);
        DatePicker startPicker = new DatePicker();
        startPicker.setValue(LocalDate.now());
        form.add(startPicker, 3, 0);

        form.add(new Label("End Date:"), 0, 1);
        DatePicker endPicker = new DatePicker();
        endPicker.setValue(LocalDate.now().plusDays(3));
        form.add(endPicker, 1, 1);

        form.add(new Label("Budget (Rs.):"), 2, 1);
        TextField budgetField = new TextField();
        budgetField.setPromptText("Total budget");
        form.add(budgetField, 3, 1);

        form.add(new Label("Vehicle:"), 0, 2);
        ComboBox<String> vehicleCombo = new ComboBox<>();
        vehicleCombo.getItems().addAll("Car", "Bike", "Bus", "Train");
        vehicleCombo.setValue("Car");
        form.add(vehicleCombo, 1, 2);

        form.add(new Label("Travelers:"), 2, 2);
        Spinner<Integer> peopleSpinner = new Spinner<>(1, 20, 1);
        form.add(peopleSpinner, 3, 2);

        Button createBtn = createStyledButton("Create Trip", "#4CAF50");
        form.add(createBtn, 0, 3, 4, 1);

        Label destHeader = new Label("Destinations");
        destHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        destinationListView = new ListView<>();
        destinationListView.setPrefHeight(150);
        destinationListView.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white;");

        TextField searchDestinationField = new TextField();
        searchDestinationField.setPromptText("Search destinations by name or category");
        searchDestinationField.textProperty().addListener((obs, oldText, newText) -> filterDestinations(newText));

        HBox destButtons = new HBox(10);
        Button addDestBtn = createStyledButton("Add Destination", "#2196F3");
        Button removeDestBtn = createStyledButton("Remove Destination", "#f44336");
        Button visitedBtn = createStyledButton("Mark Visited", "#9C27B0");

        destButtons.getChildren().addAll(addDestBtn, removeDestBtn, visitedBtn);

        createBtn.setOnAction(e -> {
            if (!nameField.getText().isEmpty() && !budgetField.getText().isEmpty()) {
                String tripId = "TRIP_" + System.currentTimeMillis();
                double budget = parsePositiveAmount(budgetField.getText(), "Budget");
                if (budget <= 0) {
                    return;
                }

                currentTrip = new Trip(
                    tripId,
                    nameField.getText(),
                    startPicker.getValue(),
                    endPicker.getValue(),
                    budget
                );
                currentTrip.setVehicleType(vehicleCombo.getValue());
                currentTrip.setPeopleCount(peopleSpinner.getValue());

                statusLabel.setText("Active trip: " + currentTrip.getName());
                flashStatus();
                pulseNode(createBtn);
                showAlert("Success", "Trip created successfully!", Alert.AlertType.INFORMATION);
                refreshDashboard();
            } else {
                showAlert("Error", "Please fill all required fields!", Alert.AlertType.ERROR);
            }
        });

        addDestBtn.setOnAction(e -> showAddDestinationDialog());

        removeDestBtn.setOnAction(e -> {
            String selected = destinationListView.getSelectionModel().getSelectedItem();
            if (selected != null && currentTrip != null) {
                currentTrip.getDestinations().removeIf(destination -> selected.contains(destination.getName()));
                refreshDestinationList();
                pulseNode(destinationListView);
                showAlert("Success", "Destination removed!", Alert.AlertType.INFORMATION);
            }
        });

        visitedBtn.setOnAction(e -> markSelectedDestinationVisited());

        content.getChildren().addAll(header, form, destHeader, searchDestinationField, destinationListView, destButtons);

        ScrollPane scrollPane = new ScrollPane(content);
        animatePanel(scrollPane);
        tab.setContent(scrollPane);
        return tab;
    }

    private Tab createWeatherTab() {
        Tab tab = new Tab("Weather");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1a1a2e;");

        Label header = new Label("Weather Forecast and Clothing Advice");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        HBox locationBox = new HBox(10);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        TextField cityField = new TextField();
        cityField.setPromptText("Enter city (e.g., Delhi, Mumbai, Manali)");
        cityField.setPrefWidth(300);

        Button fetchBtn = createStyledButton("Get Weather", "#2196F3");

        locationBox.getChildren().addAll(new Label("Location:"), cityField, fetchBtn);

        VBox weatherBox = new VBox(10);
        weatherBox.setPadding(new Insets(15));
        weatherBox.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");
        applyCardEffect(weatherBox);

        weatherDisplayText = new Text();
        weatherDisplayText.setStyle("-fx-fill: white;");
        weatherDisplayText.setText("Enter a city to see weather information...");

        weatherBox.getChildren().addAll(weatherDisplayText);

        VBox clothingBox = new VBox(10);
        clothingBox.setPadding(new Insets(15));
        clothingBox.setStyle("-fx-background-color: #2d5e2d; -fx-border-radius: 10;");
        applyCardEffect(clothingBox);

        Label clothingHeader = new Label("Clothing Recommendation");
        clothingHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        TextArea clothingText = new TextArea();
        clothingText.setEditable(false);
        clothingText.setWrapText(true);
        clothingText.setPrefHeight(200);
        clothingText.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white;");

        clothingBox.getChildren().addAll(clothingHeader, clothingText);

        fetchBtn.setOnAction(e -> {
            String city = cityField.getText();
            if (!city.isEmpty()) {
                double lat = 28.6139;
                double lon = 77.2090;

                if (city.toLowerCase().contains("mumbai")) {
                    lat = 19.0760;
                    lon = 72.8777;
                } else if (city.toLowerCase().contains("manali")) {
                    lat = 32.2396;
                    lon = 77.1887;
                } else if (city.toLowerCase().contains("jaipur")) {
                    lat = 26.9124;
                    lon = 75.7873;
                }

                WeatherData weather = weatherService.getCurrentWeather(lat, lon);

                String weatherInfo = String.format(
                    "Location: %s\nTemperature: %.1f C\nWind Speed: %.1f km/h\nHumidity: %d%%\nUV Index: %d\nRain Chance: %d%%\nWeather: %s",
                    city,
                    weather.getTemperature(),
                    weather.getWindSpeed(),
                    weather.getHumidity(),
                    weather.getUvIndex(),
                    weather.getRainProbability(),
                    weatherService.getWeatherDescription(weather.getWeatherCode())
                );
                weatherDisplayText.setText(weatherInfo);

                ClothingAdvice advice = clothingRecommender.getAdvice(weather, null);
                clothingText.setText(advice.getSummary());

                String alert = clothingRecommender.getPreDepartureAlert(weather, null);
                if (!alert.isEmpty()) {
                    showAlert("Weather Alert", alert, Alert.AlertType.WARNING);
                }
            } else {
                showAlert("Error", "Please enter a city name!", Alert.AlertType.ERROR);
            }
        });

        content.getChildren().addAll(header, locationBox, weatherBox, clothingBox);

        ScrollPane scrollPane = new ScrollPane(content);
        animatePanel(scrollPane);
        tab.setContent(scrollPane);
        return tab;
    }

    private Tab createNavigationTab() {
        Tab tab = new Tab("Navigation");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1a1a2e;");

        Label header = new Label("Route Planning");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        GridPane routeForm = new GridPane();
        routeForm.setHgap(15);
        routeForm.setVgap(15);
        routeForm.setPadding(new Insets(15));
        routeForm.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");

        routeForm.add(new Label("From:"), 0, 0);
        TextField fromField = new TextField();
        fromField.setPromptText("Starting location");
        routeForm.add(fromField, 1, 0);

        routeForm.add(new Label("To:"), 2, 0);
        TextField toField = new TextField();
        toField.setPromptText("Destination");
        routeForm.add(toField, 3, 0);

        routeForm.add(new Label("Vehicle:"), 0, 1);
        ComboBox<String> vehicleNavCombo = new ComboBox<>();
        vehicleNavCombo.getItems().addAll("Car", "Bike", "Bus");
        vehicleNavCombo.setValue("Car");
        routeForm.add(vehicleNavCombo, 1, 1);

        Button calculateBtn = createStyledButton("Calculate Route", "#4CAF50");
        routeForm.add(calculateBtn, 0, 2, 4, 1);

        VBox resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(15));
        resultsBox.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");
        resultsBox.setVisible(false);
        applyCardEffect(resultsBox);

        Label distanceLabel = new Label();
        distanceLabel.setStyle("-fx-text-fill: white;");

        Label timeLabel = new Label();
        timeLabel.setStyle("-fx-text-fill: white;");

        Label fuelLabel = new Label();
        fuelLabel.setStyle("-fx-text-fill: white;");

        TextArea instructionsArea = new TextArea();
        instructionsArea.setEditable(false);
        instructionsArea.setWrapText(true);
        instructionsArea.setPrefHeight(200);
        instructionsArea.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white;");

        resultsBox.getChildren().addAll(distanceLabel, timeLabel, fuelLabel, instructionsArea);

        calculateBtn.setOnAction(e -> {
            String from = fromField.getText();
            String to = toField.getText();

            if (!from.isEmpty() && !to.isEmpty()) {
                NavigationService.RouteResult result = navigationService.calculateRoute(
                    28.6139, 77.2090,
                    31.1048, 77.1734,
                    vehicleNavCombo.getValue()
                );

                distanceLabel.setText("Distance: " + String.format("%.1f", result.getDistanceKm()) + " km");
                timeLabel.setText("Estimated Time: " + String.format("%.0f", result.getEstimatedTimeMinutes()) + " minutes");
                fuelLabel.setText("Fuel Cost: Rs. " + String.format("%.0f", result.getEstimatedFuelCost()));

                StringBuilder instructions = new StringBuilder("Turn-by-Turn Directions:\n\n");
                if (result.getInstructions() != null) {
                    for (var instruction : result.getInstructions()) {
                        instructions.append(instruction.getStepNumber())
                            .append(". ")
                            .append(instruction.getInstruction())
                            .append(" (")
                            .append(String.format("%.1f", instruction.getDistance()))
                            .append(" km)\n");
                    }
                }
                instructionsArea.setText(instructions.toString());
                resultsBox.setVisible(true);
                animatePanel(resultsBox);
            } else {
                showAlert("Error", "Please enter both locations!", Alert.AlertType.ERROR);
            }
        });

        content.getChildren().addAll(header, routeForm, resultsBox);

        ScrollPane scrollPane = new ScrollPane(content);
        animatePanel(scrollPane);
        tab.setContent(scrollPane);
        return tab;
    }

    private Tab createExpenseTab() {
        Tab tab = new Tab("Expenses");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1a1a2e;");

        Label header = new Label("Budget and Expense Tracker");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        VBox summaryBox = new VBox(10);
        summaryBox.setPadding(new Insets(15));
        summaryBox.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");
        applyCardEffect(summaryBox);

        Label budgetLabel = new Label();
        budgetLabel.setStyle("-fx-text-fill: white;");

        if (currentTrip != null) {
            budgetLabel.setText(
                "Total Budget: Rs. " + String.format("%.0f", currentTrip.getTotalBudget()) + "\n"
                    + "Spent: Rs. " + String.format("%.0f", currentTrip.getCurrentSpend()) + "\n"
                    + "Remaining: Rs. " + String.format("%.0f", currentTrip.getRemainingBudget()) + "\n"
                    + "Utilization: " + String.format("%.1f", currentTrip.getBudgetUtilizationPercentage()) + "%"
            );
        } else {
            budgetLabel.setText("No active trip. Create a trip first to track expenses.");
        }

        summaryBox.getChildren().addAll(budgetLabel);

        GridPane expenseForm = new GridPane();
        expenseForm.setHgap(15);
        expenseForm.setVgap(15);
        expenseForm.setPadding(new Insets(15));
        expenseForm.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");

        expenseForm.add(new Label("Description:"), 0, 0);
        TextField descField = new TextField();
        descField.setPromptText("e.g., Fuel, Food, Hotel");
        expenseForm.add(descField, 1, 0);

        expenseForm.add(new Label("Amount (Rs.):"), 2, 0);
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        expenseForm.add(amountField, 3, 0);

        expenseForm.add(new Label("Category:"), 0, 1);
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Fuel", "Food", "Stay", "Shopping", "Entry Fee", "Other");
        categoryCombo.setValue("Food");
        expenseForm.add(categoryCombo, 1, 1);

        expenseForm.add(new Label("Shared with group:"), 2, 1);
        CheckBox sharedCheck = new CheckBox();
        expenseForm.add(sharedCheck, 3, 1);

        Button addExpenseBtn = createStyledButton("Add Expense", "#4CAF50");
        expenseForm.add(addExpenseBtn, 0, 2, 4, 1);

        Label expenseListHeader = new Label("Expense History");
        expenseListHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        expenseListView = new ListView<>();
        expenseListView.setPrefHeight(200);
        expenseListView.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white;");

        addExpenseBtn.setOnAction(e -> {
            if (currentTrip != null && !descField.getText().isEmpty() && !amountField.getText().isEmpty()) {
                String expenseId = "EXP_" + System.currentTimeMillis();
                double amount = parsePositiveAmount(amountField.getText(), "Expense amount");
                if (amount <= 0) {
                    return;
                }

                Expense expense = new Expense(
                    expenseId,
                    descField.getText(),
                    amount,
                    categoryCombo.getValue(),
                    LocalDate.now()
                );
                expense.setShared(sharedCheck.isSelected());

                currentTrip.addExpense(expense);
                tripDao.addExpense(expense, currentTrip.getTripId());

                expenseListView.getItems().add(
                    descField.getText() + " - Rs. " + amount + " (" + categoryCombo.getValue() + ")"
                        + (expense.isShared() ? " | Per person: Rs. "
                        + String.format("%.0f", expense.calculatePerPersonShare(currentTrip.getPeopleCount())) : "")
                );

                budgetLabel.setText(
                    "Total Budget: Rs. " + String.format("%.0f", currentTrip.getTotalBudget()) + "\n"
                        + "Spent: Rs. " + String.format("%.0f", currentTrip.getCurrentSpend()) + "\n"
                        + "Remaining: Rs. " + String.format("%.0f", currentTrip.getRemainingBudget()) + "\n"
                        + "Utilization: " + String.format("%.1f", currentTrip.getBudgetUtilizationPercentage()) + "%"
                );

                descField.clear();
                amountField.clear();

                showBudgetWarningIfNeeded();
                showAlert("Success", "Expense added successfully!", Alert.AlertType.INFORMATION);
            } else if (currentTrip == null) {
                showAlert("Error", "No active trip. Create a trip first!", Alert.AlertType.ERROR);
            } else {
                showAlert("Error", "Please fill all fields!", Alert.AlertType.ERROR);
            }
        });

        content.getChildren().addAll(header, summaryBox, expenseForm, expenseListHeader, expenseListView);

        ScrollPane scrollPane = new ScrollPane(content);
        animatePanel(scrollPane);
        tab.setContent(scrollPane);
        return tab;
    }

    private Tab createSettingsTab() {
        Tab tab = new Tab("Settings");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #1a1a2e;");

        Label header = new Label("Application Settings");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        VBox dataBox = new VBox(10);
        dataBox.setPadding(new Insets(15));
        dataBox.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");
        applyCardEffect(dataBox);

        Label dataHeader = new Label("Data Management");
        dataHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        Button exportDataBtn = createStyledButton("Export All Data", "#2196F3");
        Button reportBtn = createStyledButton("Generate Trip Report", "#673AB7");
        Button clearDataBtn = createStyledButton("Clear Sample Data", "#f44336");

        dataBox.getChildren().addAll(dataHeader, exportDataBtn, reportBtn, clearDataBtn);

        VBox aboutBox = new VBox(10);
        aboutBox.setPadding(new Insets(15));
        aboutBox.setStyle("-fx-background-color: #2d2d2d; -fx-border-radius: 10;");
        applyCardEffect(aboutBox);

        Label aboutHeader = new Label("About");
        aboutHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        Label aboutText = new Label(
            "Smart Travel Companion\n"
                + "Version 2.0\n\n"
                + "Features:\n"
                + "- Trip Planning and Management\n"
                + "- Weather Forecast Integration\n"
                + "- Clothing Recommendations\n"
                + "- Route Planning with Distance Calculation\n"
                + "- Expense Tracking\n"
                + "- CSV Data Persistence\n\n"
                + "Developed for CSY2094 Practical Skills Assessment"
        );
        aboutText.setStyle("-fx-text-fill: white;");
        aboutText.setWrapText(true);

        aboutBox.getChildren().addAll(aboutHeader, aboutText);

        exportDataBtn.setOnAction(e -> {
            if (currentTrip != null) {
                tripDao.saveTrip(currentTrip);
                showAlert("Export", "Trip data exported to CSV files!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "No trip to export!", Alert.AlertType.ERROR);
            }
        });

        reportBtn.setOnAction(e -> showTripReport());

        clearDataBtn.setOnAction(e -> {
            if (currentTrip != null) {
                currentTrip = null;
                destinationListView.getItems().clear();
                expenseListView.getItems().clear();
                statusLabel.setText("Ready - No active trip");
                showAlert("Data Cleared", "Sample data has been cleared.", Alert.AlertType.INFORMATION);
                refreshDashboard();
            }
        });

        content.getChildren().addAll(header, dataBox, aboutBox);

        ScrollPane scrollPane = new ScrollPane(content);
        animatePanel(scrollPane);
        tab.setContent(scrollPane);
        return tab;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + "; "
                + "-fx-text-fill: white; "
                + "-fx-padding: 8 15; "
                + "-fx-border-radius: 5; "
                + "-fx-font-weight: bold;"
        );
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.06);
            scale.setToY(1.06);
            scale.play();
            button.setEffect(new DropShadow(14, Color.web(color)));
        });
        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
            button.setEffect(null);
        });
        return button;
    }

    private void applyCardEffect(Region card) {
        card.setEffect(new DropShadow(16, Color.rgb(0, 0, 0, 0.35)));
        card.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(180), card);
            scale.setToX(1.01);
            scale.setToY(1.01);
            scale.play();
        });
        card.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(180), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }

    private void animatePanel(Node node) {
        node.setOpacity(0);
        node.setTranslateY(16);

        FadeTransition fade = new FadeTransition(Duration.millis(420), node);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(420), node);
        slide.setToY(0);

        fade.play();
        slide.play();
    }

    private void flashStatus() {
        if (statusLabel == null) {
            return;
        }
        FadeTransition flash = new FadeTransition(Duration.millis(180), statusLabel);
        flash.setFromValue(0.35);
        flash.setToValue(1);
        flash.setCycleCount(4);
        flash.setAutoReverse(true);
        flash.play();
    }

    private double parsePositiveAmount(String rawValue, String fieldName) {
        try {
            double value = Double.parseDouble(rawValue);
            if (value <= 0) {
                showAlert("Invalid " + fieldName, fieldName + " must be greater than zero.", Alert.AlertType.ERROR);
                return -1;
            }
            return value;
        } catch (NumberFormatException error) {
            showAlert("Invalid " + fieldName, "Please enter a valid number.", Alert.AlertType.ERROR);
            return -1;
        }
    }

    private String getDashboardNotification() {
        if (currentTrip == null) {
            return "Create or load a trip to activate reminders, budget warnings, and route planning.";
        }
        double budgetUsed = currentTrip.getBudgetUtilizationPercentage();
        if (budgetUsed >= 90) {
            return "Critical budget alert: more than 90% of the trip budget is already used.";
        }
        if (budgetUsed >= 75) {
            return "Budget warning: spending has passed 75% of the planned budget.";
        }
        if (!currentTrip.getDestinations().isEmpty()) {
            return "Next action: review weather and clothing advice before visiting "
                + currentTrip.getDestinations().get(0).getName() + ".";
        }
        return "Trip is active. Add destinations to unlock planning recommendations.";
    }

    private void showBudgetWarningIfNeeded() {
        if (currentTrip == null) {
            return;
        }
        double threshold = currentTrip.getBudgetWarningThreshold();
        if (threshold == 90) {
            showAlert("Critical Budget Warning", "You have used more than 90% of your budget.", Alert.AlertType.WARNING);
        } else if (threshold == 75) {
            showAlert("Budget Warning", "You have used more than 75% of your budget.", Alert.AlertType.WARNING);
        }
    }

    private void filterDestinations(String query) {
        if (destinationListView == null || currentTrip == null) {
            return;
        }
        destinationListView.getItems().clear();
        String normalizedQuery = query == null ? "" : query.toLowerCase();
        for (Destination destination : currentTrip.getDestinations()) {
            String searchableText = (destination.getName() + " " + destination.getCategory()).toLowerCase();
            if (searchableText.contains(normalizedQuery)) {
                destinationListView.getItems().add(formatDestination(destination));
            }
        }
    }

    private void refreshDestinationList() {
        filterDestinations("");
    }

    private String formatDestination(Destination destination) {
        String status = destination.isVisited() ? "Visited" : "Pending";
        String category = destination.getCategory() == null ? "General" : destination.getCategory();
        return destination.getName() + " | " + category + " | " + status;
    }

    private void markSelectedDestinationVisited() {
        if (currentTrip == null) {
            showAlert("Error", "No active trip. Create a trip first!", Alert.AlertType.ERROR);
            return;
        }
        String selected = destinationListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select Destination", "Choose a destination to mark as visited.", Alert.AlertType.INFORMATION);
            return;
        }
        for (Destination destination : currentTrip.getDestinations()) {
            if (selected.contains(destination.getName())) {
                destination.setVisited(true);
                refreshDestinationList();
                showAlert("Destination Updated", destination.getName() + " marked as visited.", Alert.AlertType.INFORMATION);
                return;
            }
        }
    }

    private void showTripReport() {
        if (currentTrip == null) {
            showAlert("Report", "No active trip to report.", Alert.AlertType.ERROR);
            return;
        }
        StringBuilder report = new StringBuilder();
        report.append("Trip Report: ").append(currentTrip.getName()).append("\n\n");
        report.append("Duration: ").append(currentTrip.getDurationDays()).append(" days\n");
        report.append("Travelers: ").append(currentTrip.getPeopleCount()).append("\n");
        report.append("Destinations: ").append(currentTrip.getDestinations().size()).append("\n");
        report.append("Expenses: ").append(currentTrip.getExpenses().size()).append("\n");
        report.append("Budget Used: ").append(String.format("%.1f", currentTrip.getBudgetUtilizationPercentage())).append("%\n");
        report.append("Remaining Budget: Rs. ").append(String.format("%.0f", currentTrip.getRemainingBudget())).append("\n\n");
        report.append("Visited Destinations:\n");
        for (Destination destination : currentTrip.getDestinations()) {
            report.append("- ").append(destination.getName())
                .append(": ").append(destination.isVisited() ? "Visited" : "Pending").append("\n");
        }

        TextArea reportArea = new TextArea(report.toString());
        reportArea.setEditable(false);
        reportArea.setWrapText(true);
        reportArea.setPrefSize(520, 360);

        Alert reportDialog = new Alert(Alert.AlertType.INFORMATION);
        reportDialog.setTitle("Trip Report");
        reportDialog.setHeaderText("Smart Travel Companion Report");
        reportDialog.getDialogPane().setContent(reportArea);
        reportDialog.showAndWait();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showNewTripDialog() {
        tabPane.getSelectionModel().select(1);
    }

    private void saveCurrentTrip() {
        if (currentTrip != null) {
            tripDao.saveTrip(currentTrip);
            showAlert("Save", "Trip saved successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "No active trip to save!", Alert.AlertType.ERROR);
        }
    }

    private void loadTrip() {
        var trips = tripDao.findAll();
        if (!trips.isEmpty()) {
            currentTrip = trips.get(0);
            statusLabel.setText("Active trip: " + currentTrip.getName());
            showAlert("Load", "Trip loaded successfully!", Alert.AlertType.INFORMATION);
            refreshDashboard();
        } else {
            showAlert("Info", "No saved trips found. Create a new trip first.", Alert.AlertType.INFORMATION);
        }
    }

    private void showAddDestinationDialog() {
        if (currentTrip == null) {
            showAlert("Error", "No active trip. Create a trip first!", Alert.AlertType.ERROR);
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Destination");
        dialog.setHeaderText("Enter destination name");
        dialog.setContentText("Destination:");

        dialog.showAndWait().ifPresent(destName -> {
            if (!destName.isEmpty()) {
                destinationListView.getItems().add(destName);

                Destination dest = new Destination(
                    "DEST_" + System.currentTimeMillis(),
                    destName,
                    "User added destination",
                    0, 0
                );
                dest.setStayDurationHours(24);
                currentTrip.addDestination(dest);

                showAlert("Success", "Destination added!", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void refreshDashboard() {
        tabPane.getTabs().set(0, createDashboardTab());
        tabPane.getTabs().set(4, createExpenseTab());
        tabPane.getSelectionModel().select(0);
    }

    private void setupSampleData() {
        currentTrip = new Trip(
            "TRIP_SAMPLE_001",
            "Sample Manali Trip",
            LocalDate.now(),
            LocalDate.now().plusDays(3),
            25000.0
        );
        currentTrip.setVehicleType("Car");
        currentTrip.setPeopleCount(4);

        Destination manali = new Destination(
            "DEST_001",
            "Manali",
            "Beautiful hill station in Himachal Pradesh",
            32.2396, 77.1887
        );
        manali.setStayDurationHours(48);
        manali.setCategory("Hill Station");
        currentTrip.addDestination(manali);

        if (destinationListView != null) {
            refreshDestinationList();
        }
        if (statusLabel != null) {
            statusLabel.setText("Active trip: Sample Manali Trip");
        }

        refreshDashboard();
        System.out.println("Sample data loaded successfully");
    }
}
