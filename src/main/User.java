package com.smarthostel.model;

public class User {
    private final int userId;
    private final String username;
    private final String role;
    private final String studentId;

    public User(int userId, String username, String role, String studentId) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.studentId = studentId;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getStudentId() { return studentId; }
}
