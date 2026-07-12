package com.travelcompanion.service;

import com.travelcompanion.model.WeatherData;
import com.travelcompanion.model.ClothingAdvice;

/**
 * ClothingRecommender provides intelligent outfit suggestions based on weather.
 *
 * @author CSY2094 Student
 */
public class ClothingRecommender {

    /**
     * Generates complete clothing advice based on weather conditions.
     *
     * @param weather Current weather data
     * @param destinationType Type of destination (temple, beach, mountain, etc.)
     * @return ClothingAdvice object with recommendations
     */
    public ClothingAdvice getAdvice(WeatherData weather, String destinationType) {
        ClothingAdvice advice = new ClothingAdvice();

        double temperature = weather.getTemperature();
        double windSpeed = weather.getWindSpeed();
        int uvIndex = weather.getUvIndex();
        int rainProbability = weather.getRainProbability();

        // Temperature-based clothing recommendations
        provideTemperatureAdvice(advice, temperature);

        // Wind-based recommendations
        provideWindAdvice(advice, windSpeed);

        // Rain-based recommendations
        provideRainAdvice(advice, rainProbability);

        // UV-based recommendations
        provideUVAdvice(advice, uvIndex);

        // Religious site dress code
        provideReligiousAdvice(advice, destinationType);

        // Generate summary
        advice.setSummary(generateSummary(advice));

        return advice;
    }

    /**
     * Provides temperature-based clothing advice.
     */
    private void provideTemperatureAdvice(ClothingAdvice advice, double temperature) {
        if (temperature < 10) {
            advice.setTopLayer("Heavy winter jacket");
            advice.addEssential("Woolen scarf");
            advice.addEssential("Gloves");
            advice.addEssential("Woolen cap");
            advice.addEssential("Thermal innerwear");
            advice.setTemperatureCategory("Extreme Cold");
        } else if (temperature < 18) {
            advice.setTopLayer("Light jacket or hoodie");
            advice.addEssential("Full sleeves t-shirt");
            advice.setTemperatureCategory("Cold");
        } else if (temperature < 25) {
            advice.setTopLayer("Full sleeves t-shirt");
            advice.setTemperatureCategory("Mild");
        } else if (temperature < 32) {
            advice.setTopLayer("Half sleeves t-shirt");
            advice.addEssential("Cotton clothing");
            advice.setTemperatureCategory("Warm");
        } else {
            advice.setTopLayer("Light cotton vest");
            advice.addEssential("Cap or hat");
            advice.addEssential("Sunglasses");
            advice.setTemperatureCategory("Hot");
        }
    }

    /**
     * Provides wind-based clothing advice.
     */
    private void provideWindAdvice(ClothingAdvice advice, double windSpeed) {
        if (windSpeed > 30) {
            advice.addAlert("High winds detected. Wear windbreaker jacket.");
            advice.addEssential("Sunglasses to protect eyes from dust");
        } else if (windSpeed > 20) {
            advice.addRecommendation("Windy conditions. Light jacket recommended.");
        }
    }

    /**
     * Provides rain-based advice and alerts.
     */
    private void provideRainAdvice(ClothingAdvice advice, int rainProbability) {
        if (rainProbability >= 70) {
            advice.addEssential("Raincoat or umbrella");
            advice.addEssential("Waterproof shoes");
            advice.addUrgentAlert("High rain chance. Carry rain protection.");
        } else if (rainProbability >= 40) {
            advice.addRecommendation("Carry umbrella or light raincoat");
            advice.addAlert("Rain possible. Be prepared.");
        }
    }

    /**
     * Provides UV and sun protection advice.
     */
    private void provideUVAdvice(ClothingAdvice advice, int uvIndex) {
        if (uvIndex >= 8) {
            advice.addEssential("SPF 50+ sunscreen");
            advice.addEssential("UV protection sunglasses");
            advice.addEssential("Wide-brim hat");
            advice.addUrgentAlert("Extreme UV. Avoid outdoor exposure 11 AM to 3 PM.");
        } else if (uvIndex >= 6) {
            advice.addEssential("SPF 30+ sunscreen");
            advice.addEssential("Sunglasses");
            advice.addRecommendation("Wear a hat");
        } else if (uvIndex >= 3) {
            advice.addRecommendation("Light sunscreen recommended");
        }
    }

    /**
     * Provides religious site dress code advice.
     */
    private void provideReligiousAdvice(ClothingAdvice advice, String destinationType) {
        if (destinationType == null) {
            return;
        }

        String lowerType = destinationType.toLowerCase();

        if (lowerType.contains("temple") || lowerType.contains("hindu")) {
            advice.addReligiousRequirement("No leather items allowed");
            advice.addReligiousRequirement("Cover shoulders and knees");
            advice.addReligiousRequirement("Remove footwear before entering");
            advice.setFootwear("Canvas shoes or socks (no leather)");
        } else if (lowerType.contains("mosque")) {
            advice.addReligiousRequirement("Women cover head with scarf");
            advice.addReligiousRequirement("Remove shoes before entering");
            advice.addReligiousRequirement("Wear modest clothing");
        } else if (lowerType.contains("gurudwara")) {
            advice.addReligiousRequirement("Cover head at all times");
            advice.addReligiousRequirement("Remove shoes and wash feet");
            advice.addReligiousRequirement("No tobacco or alcohol on premises");
        }
    }

    /**
     * Generates a summary of all clothing advice.
     */
    private String generateSummary(ClothingAdvice advice) {
        StringBuilder summary = new StringBuilder();
        summary.append("CLOTHING RECOMMENDATION SUMMARY");
        summary.append("\n");

        if (!advice.getTopLayer().isEmpty()) {
            summary.append("Top Layer: ").append(advice.getTopLayer()).append("\n");
        }

        if (!advice.getFootwear().isEmpty()) {
            summary.append("Footwear: ").append(advice.getFootwear()).append("\n");
        }

        if (!advice.getEssentials().isEmpty()) {
            summary.append("Essentials: ").append(String.join(", ", advice.getEssentials())).append("\n");
        }

        if (!advice.getReligiousRequirements().isEmpty()) {
            summary.append("\nReligious Requirements:\n");
            for (String req : advice.getReligiousRequirements()) {
                summary.append("  - ").append(req).append("\n");
            }
        }

        if (!advice.getAlerts().isEmpty()) {
            summary.append("\nAlerts:\n");
            for (String alert : advice.getAlerts()) {
                summary.append("  - ").append(alert).append("\n");
            }
        }

        return summary.toString();
    }

    /**
     * Gets proactive alert for leaving hotel.
     *
     * @param weather Current weather data
     * @param destinationType Destination type
     * @return Alert message string
     */
    public String getPreDepartureAlert(WeatherData weather, String destinationType) {
        StringBuilder alert = new StringBuilder();

        if (weather.getRainProbability() >= 50) {
            alert.append("Rain expected. Take umbrella or raincoat.\n");
        }

        if (weather.getTemperature() < 15 && weather.getWindSpeed() > 20) {
            alert.append("Cold and windy outside. Wear warm clothing.\n");
        }

        if (weather.getUvIndex() >= 6) {
            alert.append("High UV index. Apply sunscreen before leaving.\n");
        }

        if (destinationType != null && destinationType.toLowerCase().contains("temple")) {
            alert.append("Temple visit: No leather items. Remove shoes before entering.\n");
        }

        if (alert.length() == 0) {
            alert.append("Weather conditions are good. Enjoy your day.");
        }

        return alert.toString();
    }
}
