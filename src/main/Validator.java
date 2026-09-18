package com.smarthostel.util;

import java.util.regex.Pattern;

public final class Validator {
    private static final Pattern PHONE = Pattern.compile("[0-9]{10}");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private Validator() {}

    public static void requireText(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required.");
    }

    public static void student(String id, String name, String course, String year, String phone, String email) {
        requireText(id, "Student ID");
        requireText(name, "Name");
        requireText(course, "Course");
        requireText(year, "Year");
        requireText(phone, "Phone");
        requireText(email, "Email");
        int y;
        try { y = Integer.parseInt(year); } catch (NumberFormatException e) { throw new IllegalArgumentException("Year must be a number."); }
        if (y < 1 || y > 6) throw new IllegalArgumentException("Year must be between 1 and 6.");
        if (!PHONE.matcher(phone).matches()) throw new IllegalArgumentException("Phone must contain exactly 10 digits.");
        if (!EMAIL.matcher(email).matches()) throw new IllegalArgumentException("Enter a valid email.");
    }

    public static void room(String roomNumber, String block, String capacity) {
        requireText(roomNumber, "Room number");
        requireText(block, "Block");
        requireText(capacity, "Capacity");
        int cap;
        try { cap = Integer.parseInt(capacity); } catch (NumberFormatException e) { throw new IllegalArgumentException("Capacity must be a number."); }
        if (cap < 1 || cap > 8) throw new IllegalArgumentException("Capacity must be between 1 and 8.");
    }
}
