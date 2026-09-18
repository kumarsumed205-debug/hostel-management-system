package com.smarthostel.ui;

import com.smarthostel.dao.UserDAO;
import com.smarthostel.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField usernameField = new JTextField( 18);
    private final JPasswordField passwordField = new JPasswordField( 18);
    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        setTitle("Smart Hostel Management System - Login");
        setSize(430, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel outer = new JPanel(new GridBagLayout());
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Login"),
                BorderFactory.createEmptyBorder(10, 15, 15, 15)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 7, 7, 7);
        g.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("SMART HOSTEL MANAGEMENT SYSTEM");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 17f));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        card.add(title, g);

        g.gridwidth = 1; g.gridy++;
        card.add(new JLabel("Username:"), g);
        g.gridx = 1; card.add(usernameField, g);
        g.gridx = 0; g.gridy++;
        card.add(new JLabel("Password:"), g);
        g.gridx = 1; card.add(passwordField, g);

        JButton login = new JButton("Login");
        login.addActionListener(e -> authenticate());
        g.gridx = 0; g.gridy++; g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        card.add(login, g);

        outer.add(card);
        add(outer);
        getRootPane().setDefaultButton(login);
    }

    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.");
            return;
        }
        try {
            User user = userDAO.authenticate(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dispose();
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                new AdminDashboardFrame(user).setVisible(true);
            } else {
                new StudentDashboardFrame(user).setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to connect to database:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
