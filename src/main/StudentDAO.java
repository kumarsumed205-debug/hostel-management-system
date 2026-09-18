package com.smarthostel.dao;

import com.smarthostel.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public List<Student> findAll() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY student_id";
        List<Student> result = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(map(rs));
        }
        return result;
    }

    public Student findById(String id) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public void insert(Student s) throws SQLException {
        String sql = "INSERT INTO students(student_id,name,course,year,phone,email) VALUES(?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            fill(ps, s);
            ps.executeUpdate();
        }
    }

    public void update(Student s) throws SQLException {
        String sql = "UPDATE students SET name=?,course=?,year=?,phone=?,email=? WHERE student_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getCourse());
            ps.setInt(3, s.getYear());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getEmail());
            ps.setString(6, s.getStudentId());
            ps.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(rs.getString("student_id"), rs.getString("name"),
                rs.getString("course"), rs.getInt("year"), rs.getString("phone"), rs.getString("email"));
    }

    private void fill(PreparedStatement ps, Student s) throws SQLException {
        ps.setString(1, s.getStudentId());
        ps.setString(2, s.getName());
        ps.setString(3, s.getCourse());
        ps.setInt(4, s.getYear());
        ps.setString(5, s.getPhone());
        ps.setString(6, s.getEmail());
    }
}
