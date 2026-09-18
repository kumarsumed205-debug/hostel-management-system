package com.smarthostel.dao;

import com.smarthostel.model.User;
import com.smarthostel.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public User authenticate(String username, String password) throws Exception {
        String sql = "SELECT user_id, username, role, student_id FROM users WHERE username=? AND password_hash=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, PasswordUtil.sha256(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("username"),
                            rs.getString("role"), rs.getString("student_id"));
                }
            }
        }
        return null;
    }
}
