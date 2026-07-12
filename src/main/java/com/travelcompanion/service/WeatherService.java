package com.travelcompanion.service;

import com.travelcompanion.model.WeatherData;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * WeatherService fetches real-time weather data from Open-Meteo API.
 * This API is free and requires no API key.
 *
 * @author CSY2094 Student
 */
public class WeatherService {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";

    /**
     * Fetches current weather data for given coordinates.
     *
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @return WeatherData object with current conditions
     */
    public WeatherData getCurrentWeather(double latitude, double longitude) {
        WeatherData weather = new WeatherData();

        try {
            // Build the API URL with required parameters
            String urlString = BASE_URL + "?"
                + "latitude=" + latitude
                + "&longitude=" + longitude
                + "&current_weather=true"
                + "&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m,precipitation_probability"
                + "&daily=uv_index_max"
                + "&timezone=auto";

            // Make HTTP request
            URL url = new URI(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONObject json = new JSONObject(response.toString());

            // Parse current weather
            if (json.has("current_weather")) {
                JSONObject current = json.getJSONObject("current_weather");
                weather.setTemperature(current.getDouble("temperature"));
                weather.setWindSpeed(current.getDouble("windspeed"));
                weather.setWeatherCode(current.getInt("weathercode"));
            }

            // Parse hourly data for humidity and rain probability
            if (json.has("hourly")) {
                JSONObject hourly = json.getJSONObject("hourly");
                if (hourly.has("relative_humidity_2m")) {
                    var humidityArray = hourly.getJSONArray("relative_humidity_2m");
                    if (humidityArray.length() > 0) {
                        weather.setHumidity(humidityArray.getInt(0));
                    }
                }
                if (hourly.has("precipitation_probability")) {
                    var rainArray = hourly.getJSONArray("precipitation_probability");
                    if (rainArray.length() > 0) {
                        weather.setRainProbability(rainArray.getInt(0));
                    }
                }
            }

            // Parse daily data for UV index
            if (json.has("daily")) {
                JSONObject daily = json.getJSONObject("daily");
                if (daily.has("uv_index_max")) {
                    var uvArray = daily.getJSONArray("uv_index_max");
                    if (uvArray.length() > 0) {
                        weather.setUvIndex((int) Math.round(uvArray.getDouble(0)));
                    }
                }
            }

            weather.setFetchTime(LocalDateTime.now());
            weather.setIsFallback(false);

            System.out.println("Weather data fetched for coordinates: " + latitude + ", " + longitude);

        } catch (Exception error) {
            System.err.println("Failed to fetch weather data: " + error.getMessage());
            // Return fallback data
            weather.setTemperature(25.0);
            weather.setWindSpeed(10.0);
            weather.setHumidity(60);
            weather.setRainProbability(20);
            weather.setWeatherCode(1);
            weather.setUvIndex(5);
            weather.setFetchTime(LocalDateTime.now());
            weather.setIsFallback(true);
        }

        return weather;
    }

    /**
     * Converts weather code to human readable description.
     *
     * @param weatherCode WMO weather code
     * @return Description string
     */
    public String getWeatherDescription(int weatherCode) {
        switch (weatherCode) {
            case 0:
                return "Clear sky";
            case 1:
                return "Mainly clear";
            case 2:
                return "Partly cloudy";
            case 3:
                return "Overcast";
            case 45:
                return "Foggy";
            case 48:
                return "Depositing rime fog";
            case 51:
                return "Light drizzle";
            case 53:
                return "Moderate drizzle";
            case 55:
                return "Dense drizzle";
            case 61:
                return "Slight rain";
            case 63:
                return "Moderate rain";
            case 65:
                return "Heavy rain";
            case 71:
                return "Slight snow";
            case 73:
                return "Moderate snow";
            case 75:
                return "Heavy snow";
            case 95:
                return "Thunderstorm";
            default:
                return "Unknown";
        }
    }

    /**
     * Gets clothing recommendation based on temperature.
     *
     * @param temperature Current temperature in Celsius
     * @return Clothing recommendation string
     */
    public String getTemperatureClothingAdvice(double temperature) {
        if (temperature < 10) {
            return "Wear heavy winter jacket, gloves, scarf, and woolen cap";
        } else if (temperature < 18) {
            return "Wear light jacket or hoodie with full sleeves";
        } else if (temperature < 25) {
            return "Wear full sleeves t-shirt or light sweater";
        } else if (temperature < 32) {
            return "Wear half sleeves t-shirt and light pants";
        } else {
            return "Wear light cotton clothes, cap, and sunglasses";
        }
    }

    /**
     * Gets rain alert message based on rain probability.
     *
     * @param rainProbability Probability percentage (0-100)
     * @return Alert message
     */
    public String getRainAlertMessage(int rainProbability) {
        if (rainProbability >= 70) {
            return "High chance of rain. Carry raincoat and umbrella. Avoid open areas.";
        } else if (rainProbability >= 40) {
            return "Moderate chance of rain. Keep umbrella handy.";
        } else if (rainProbability >= 20) {
            return "Low chance of rain. Still carry a light raincoat.";
        } else {
            return "No rain expected. Enjoy your day!";
        }
    }

    /**
     * Gets UV safety recommendation.
     *
     * @param uvIndex UV index value
     * @return Safety recommendation
     */
    public String getUVAdvice(int uvIndex) {
        if (uvIndex >= 8) {
            return "Extreme UV. Avoid sun exposure 11 AM to 3 PM. Use SPF 50+ sunscreen.";
        } else if (uvIndex >= 6) {
            return "High UV. Wear sunscreen, sunglasses, and hat. Seek shade at noon.";
        } else if (uvIndex >= 3) {
            return "Moderate UV. Use sunscreen if outdoors for long periods.";
        } else {
            return "Low UV. Safe to be outdoors without protection.";
        }
    }
}
