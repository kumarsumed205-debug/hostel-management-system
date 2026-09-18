package com.smarthostel.service;

public class ComplaintPriorityService {
    public String calculatePriority(String category, String severity) {
        String c = category == null ? "" : category.trim().toLowerCase();
        String s = severity == null ? "" : severity.trim().toLowerCase();

        if (s.equals("high")) return "HIGH";
        if (c.equals("electrical") || c.equals("water")) return "HIGH";
        if (c.equals("internet")) return "MEDIUM";
        if (c.equals("cleaning")) return "LOW";
        return "MEDIUM";
    }
}
