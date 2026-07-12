package com.travelcompanion.model;

import java.time.LocalDateTime;

/**
 * WeatherData class for storing weather information.
 *
 * @author CSY2094 Student
 */
public class WeatherData {
    private double temperature;
    private double windSpeed;
    private int humidity;
    private int weatherCode;
    private int uvIndex;
    private int rainProbability;
    private double tempMax;
    private double tempMin;
    private LocalDateTime fetchTime;
    private boolean isFallback;

    public WeatherData() {
        this.fetchTime = LocalDateTime.now();
        this.isFallback = false;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }

    public int getRainProbability() {
        return rainProbability;
    }

    public void setRainProbability(int rainProbability) {
        this.rainProbability = rainProbability;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public LocalDateTime getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(LocalDateTime fetchTime) {
        this.fetchTime = fetchTime;
    }

    public boolean isFallback() {
        return isFallback;
    }

    public void setIsFallback(boolean isFallback) {
        this.isFallback = isFallback;
    }

    public boolean isExpired(int maxAgeMinutes) {
        return java.time.Duration.between(fetchTime, LocalDateTime.now()).toMinutes() > maxAgeMinutes;
    }

    public String getWeatherDescription() {
        switch (weatherCode) {
            case 0:
                return "Clear sky";
            case 1:
            case 2:
            case 3:
                return "Partly cloudy";
            case 45:
            case 48:
                return "Foggy";
            case 51:
            case 53:
            case 55:
                return "Drizzle";
            case 61:
            case 63:
            case 65:
                return "Rain";
            case 71:
            case 73:
            case 75:
                return "Snow";
            default:
                return "Unknown";
        }
    }

    @Override
    public String toString() {
        return String.format("Temperature: %.1f C, Wind: %.1f km/h, Rain: %d%%",
            temperature, windSpeed, rainProbability);
    }
}
