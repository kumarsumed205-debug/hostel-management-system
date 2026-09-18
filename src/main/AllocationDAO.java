package com.smarthostel.dao;

import com.smarthostel.model.Room;

import java.sql.*;

public class AllocationDAO {
    public Room findStudentRoom(String studentId) throws SQLException {
        String sql = "SELECT r.* FROM allocations a JOIN rooms r ON a.room_id=r.room_id WHERE a.student_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Room(rs.getInt("room_id"), rs.getString("room_number"),
                        rs.getString("block_name"), rs.getInt("capacity"), rs.getInt("occupied"),
                        rs.getString("room_type"), rs.getString("status"));
            }
        }
        return null;
    }

    public void allocate(String studentId, int roomId) throws SQLException {
        String insert = "INSERT INTO allocations(student_id,room_id) VALUES(?,?)";
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                if (findStudentRoom(con, studentId) != null) throw new SQLException("Student already has a room.");
                new RoomDAO().updateOccupancy(con, roomId, 1);
                try (PreparedStatement ps = con.prepareStatement(insert)) {
                    ps.setString(1, studentId);
                    ps.setInt(2, roomId);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (Exception e) {
                con.rollback();
                if (e instanceof SQLException sqlEx) throw sqlEx;
                throw new SQLException(e.getMessage(), e);
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    private Room findStudentRoom(Connection con, String studentId) throws SQLException {
        String sql = "SELECT r.* FROM allocations a JOIN rooms r ON a.room_id=r.room_id WHERE a.student_id=? FOR UPDATE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Room(rs.getInt("room_id"), rs.getString("room_number"),
                        rs.getString("block_name"), rs.getInt("capacity"), rs.getInt("occupied"),
                        rs.getString("room_type"), rs.getString("status"));
            }
        }
        return null;
    }
}
