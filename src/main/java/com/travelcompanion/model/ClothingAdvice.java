package com.travelcompanion.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clothing recommendation based on weather and trip conditions.
 *
 * @author CSY2094 Student
 */
public class ClothingAdvice {
    private String recommendation;
    private String reason;
    private boolean rainProtectionNeeded;
    private String topLayer;
    private String footwear;
    private String temperatureCategory;
    private String summary;
    private List<String> essentials;
    private List<String> recommendations;
    private List<String> alerts;
    private List<String> urgentAlerts;
    private List<String> religiousRequirements;

    public ClothingAdvice() {
        this("", "");
    }

    public ClothingAdvice(String recommendation, String reason) {
        this.recommendation = recommendation;
        this.reason = reason;
        this.topLayer = "";
        this.footwear = "";
        this.temperatureCategory = "";
        this.summary = "";
        this.essentials = new ArrayList<>();
        this.recommendations = new ArrayList<>();
        this.alerts = new ArrayList<>();
        this.urgentAlerts = new ArrayList<>();
        this.religiousRequirements = new ArrayList<>();
    }

    public void addEssential(String essential) {
        essentials.add(essential);
    }

    public void addRecommendation(String recommendation) {
        recommendations.add(recommendation);
    }

    public void addAlert(String alert) {
        alerts.add(alert);
    }

    public void addUrgentAlert(String urgentAlert) {
        urgentAlerts.add(urgentAlert);
        alerts.add(urgentAlert);
    }

    public void addReligiousRequirement(String religiousRequirement) {
        religiousRequirements.add(religiousRequirement);
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isRainProtectionNeeded() {
        return rainProtectionNeeded;
    }

    public void setRainProtectionNeeded(boolean rainProtectionNeeded) {
        this.rainProtectionNeeded = rainProtectionNeeded;
    }

    public String getTopLayer() {
        return topLayer;
    }

    public void setTopLayer(String topLayer) {
        this.topLayer = topLayer;
    }

    public String getFootwear() {
        return footwear;
    }

    public void setFootwear(String footwear) {
        this.footwear = footwear;
    }

    public String getTemperatureCategory() {
        return temperatureCategory;
    }

    public void setTemperatureCategory(String temperatureCategory) {
        this.temperatureCategory = temperatureCategory;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getEssentials() {
        return essentials;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public List<String> getAlerts() {
        return alerts;
    }

    public List<String> getUrgentAlerts() {
        return urgentAlerts;
    }

    public List<String> getReligiousRequirements() {
        return religiousRequirements;
    }

    @Override
    public String toString() {
        if (!summary.isEmpty()) {
            return summary;
        }
        return recommendation + " - " + reason;
    }
}
