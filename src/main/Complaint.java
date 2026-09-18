package com.smarthostel.model;

public class Complaint {
    private int complaintId;
    private String studentId;
    private String category;
    private String description;
    private String priority;
    private String status;
    private String createdAt;

    public Complaint() {}

    public Complaint(int complaintId, String studentId, String category, String description,
                     String priority, String status, String createdAt) {
        this.complaintId = complaintId;
        this.studentId = studentId;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getComplaintId() { return complaintId; }
    public String getStudentId() { return studentId; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
