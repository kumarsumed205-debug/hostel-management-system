package com.smarthostel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardDAO {
    public int countStudents() throws Exception { return count("SELECT COUNT(*) FROM students"); }
    public int countRooms() throws Exception { return count("SELECT COUNT(*) FROM rooms"); }
    public int countAvailableRooms() throws Exception { return count("SELECT COUNT(*) FROM rooms WHERE occupied < capacity AND status <> 'MAINTENANCE'"); }
    public int countPendingComplaints() throws Exception { return count("SELECT COUNT(*) FROM complaints WHERE status <> 'RESOLVED'"); }

    private int count(String sql) throws Exception {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
