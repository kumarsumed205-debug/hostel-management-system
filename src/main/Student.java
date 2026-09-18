package com.smarthostel.model;

public class Student {
    private String studentId;
    private String name;
    private String course;
    private int year;
    private String phone;
    private String email;

    public Student() {}

    public Student(String studentId, String name, String course, int year, String phone, String email) {
        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.year = year;
        this.phone = phone;
        this.email = email;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getCourse() { return course; }
    public int getYear() { return year; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setName(String name) { this.name = name; }
    public void setCourse(String course) { this.course = course; }
    public void setYear(int year) { this.year = year; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
}
