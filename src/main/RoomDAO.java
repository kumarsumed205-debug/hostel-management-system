package com.smarthostel.dao;

import com.smarthostel.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    public List<Room> findAll() throws SQLException {
        String sql = "SELECT * FROM rooms ORDER BY block_name, room_number";
        List<Room> result = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(map(rs));
        }
        return result;
    }

    public List<Room> findAvailable(String preferredBlock, String roomType) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE status <> 'FULL' AND status <> 'MAINTENANCE' AND occupied < capacity";
        List<Room> result = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(map(rs));
        }
        return result;
    }

    public void insert(Room r) throws SQLException {
        String sql = "INSERT INTO rooms(room_number,block_name,capacity,occupied,room_type,status) VALUES(?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getRoomNumber());
            ps.setString(2, r.getBlockName());
            ps.setInt(3, r.getCapacity());
            ps.setInt(4, 0);
            ps.setString(5, r.getRoomType());
            ps.setString(6, "AVAILABLE");
            ps.executeUpdate();
        }
    }

    public void delete(int roomId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE room_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.executeUpdate();
        }
    }

    public void updateOccupancy(Connection con, int roomId, int delta) throws SQLException {
       String sql = "UPDATE rooms SET occupied=occupied+?, " +
        "status=CASE WHEN occupied >= capacity THEN 'FULL' " +
        "WHEN occupied > 0 THEN 'PARTIALLY_OCCUPIED' " +
        "ELSE 'AVAILABLE' END " +
        "WHERE room_id=? AND occupied+? BETWEEN 0 AND capacity";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setInt(2, roomId);
            ps.setInt(3, delta);
            int updated = ps.executeUpdate();
            if (updated == 0) throw new SQLException("Room is full or occupancy would become invalid.");
        }
    }

    public Room findById(int roomId) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE room_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    private Room map(ResultSet rs) throws SQLException {
        return new Room(rs.getInt("room_id"), rs.getString("room_number"), rs.getString("block_name"),
                rs.getInt("capacity"), rs.getInt("occupied"), rs.getString("room_type"), rs.getString("status"));
    }
}
