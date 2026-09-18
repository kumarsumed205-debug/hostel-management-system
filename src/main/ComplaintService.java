package com.smarthostel.service;

import com.smarthostel.dao.ComplaintDAO;
import com.smarthostel.model.Complaint;
import com.smarthostel.util.Validator;

import java.util.List;

public class ComplaintService {
    private final ComplaintDAO dao = new ComplaintDAO();
    private final ComplaintPriorityService priorityService = new ComplaintPriorityService();

    public List<Complaint> getAll() throws Exception { return dao.findAll(); }
    public List<Complaint> getByStudent(String studentId) throws Exception { return dao.findByStudent(studentId); }

    public void submit(String studentId, String category, String description, String severity) throws Exception {
        Validator.requireText(studentId, "Student ID");
        Validator.requireText(category, "Category");
        Validator.requireText(description, "Description");
        Validator.requireText(severity, "Severity");
        if (description.trim().length() > 500) throw new IllegalArgumentException("Description is too long.");
        String priority = priorityService.calculatePriority(category, severity);
        dao.insert(studentId.trim(), category.trim(), description.trim(), priority);
    }

    public void updateStatus(int complaintId, String status) throws Exception {
        if (!List.of("PENDING", "IN_PROGRESS", "RESOLVED").contains(status))
            throw new IllegalArgumentException("Invalid complaint status.");
        dao.updateStatus(complaintId, status);
    }
}
