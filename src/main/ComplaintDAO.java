package com.smarthostel.dao;

import com.smarthostel.model.Complaint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {
    public List<Complaint> findAll() throws SQLException {
        String sql = "SELECT complaint_id,student_id,category,description,priority,status,created_at FROM complaints ORDER BY " +
                "FIELD(priority,'HIGH','MEDIUM','LOW'), complaint_id DESC";
        return query(sql, null);
    }

    public List<Complaint> findByStudent(String studentId) throws SQLException {
        String sql = "SELECT complaint_id,student_id,category,description,priority,status,created_at FROM complaints WHERE student_id=? ORDER BY complaint_id DESC";
        return query(sql, studentId);
    }

    public void insert(String studentId, String category, String description, String priority) throws SQLException {
        String sql = "INSERT INTO complaints(student_id,category,description,priority) VALUES(?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, category);
            ps.setString(3, description);
            ps.setString(4, priority);
            ps.executeUpdate();
        }
    }

    public void updateStatus(int complaintId, String status) throws SQLException {
        String sql = "UPDATE complaints SET status=?, resolved_at=CASE WHEN ?='RESOLVED' THEN CURRENT_TIMESTAMP ELSE resolved_at END WHERE complaint_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, status);
            ps.setInt(3, complaintId);
            ps.executeUpdate();
        }
    }

    private List<Complaint> query(String sql, String studentId) throws SQLException {
        List<Complaint> result = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (studentId != null) ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(new Complaint(rs.getInt("complaint_id"), rs.getString("student_id"),
                        rs.getString("category"), rs.getString("description"), rs.getString("priority"),
                        rs.getString("status"), rs.getString("created_at")));
            }
        }
        return result;
    }
}
