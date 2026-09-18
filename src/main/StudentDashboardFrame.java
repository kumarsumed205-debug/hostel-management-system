package com.smarthostel.ui;

import com.smarthostel.model.Complaint;
import com.smarthostel.model.Room;
import com.smarthostel.model.User;
import com.smarthostel.service.AllocationService;
import com.smarthostel.service.ComplaintService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentDashboardFrame extends JFrame {
    private final User user;
    private final AllocationService allocationService = new AllocationService();
    private final ComplaintService complaintService = new ComplaintService();
    private final JLabel roomLabel = new JLabel("Loading...");
    private final DefaultTableModel complaintModel = new DefaultTableModel(new String[]{"ID", "Category", "Description", "Priority", "Status", "Created"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };

    public StudentDashboardFrame(User user) {
        this.user = user;
        setTitle("Smart Hostel Management System - Student");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("Student Portal: " + user.getStudentId()), BorderLayout.WEST);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        header.add(logout, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Room", roomPanel());
        tabs.addTab("Submit Complaint", submitComplaintPanel());
        tabs.addTab("My Complaints", complaintsPanel());
        root.add(tabs, BorderLayout.CENTER);
        add(root);
        refreshRoom(); refreshComplaints();
    }

    private JPanel roomPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        roomLabel.setFont(roomLabel.getFont().deriveFont(Font.BOLD, 20f));
        p.add(roomLabel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshRoom());
        GridBagConstraints g = new GridBagConstraints(); g.gridy = 1; g.insets = new Insets(20,0,0,0);
        p.add(refresh, g);
        return p;
    }

    private JPanel submitComplaintPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7,7,7,7); g.anchor = GridBagConstraints.WEST;
        JComboBox<String> category = new JComboBox<>(new String[]{"Electrical", "Water", "Cleaning", "Internet", "Other"});
        JComboBox<String> severity = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        JTextArea description = new JTextArea(6, 35); description.setLineWrap(true); description.setWrapStyleWord(true);
        g.gridx=0; g.gridy=0; p.add(new JLabel("Category:"), g); g.gridx=1; p.add(category, g);
        g.gridx=0; g.gridy++; p.add(new JLabel("Severity:"), g); g.gridx=1; p.add(severity, g);
        g.gridx=0; g.gridy++; g.anchor=GridBagConstraints.NORTHWEST; p.add(new JLabel("Description:"), g); g.gridx=1; p.add(new JScrollPane(description), g);
        JButton submit = new JButton("Submit Complaint");
        submit.addActionListener(e -> {
            try {
                complaintService.submit(user.getStudentId(), (String) category.getSelectedItem(), description.getText(), (String) severity.getSelectedItem());
                description.setText(""); refreshComplaints(); JOptionPane.showMessageDialog(this, "Complaint submitted successfully.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        g.gridx=1; g.gridy++; g.anchor=GridBagConstraints.CENTER; p.add(submit, g);
        return p;
    }

    private JPanel complaintsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JTable table = new JTable(complaintModel); table.setAutoCreateRowSorter(true);
        JButton refresh = new JButton("Refresh"); refresh.addActionListener(e -> refreshComplaints());
        p.add(new JScrollPane(table), BorderLayout.CENTER); p.add(refresh, BorderLayout.SOUTH);
        return p;
    }

    private void refreshRoom() {
        try {
            Room room = allocationService.getStudentRoom(user.getStudentId());
            if (room == null) roomLabel.setText("No room allocated yet.");
            else roomLabel.setText("Room " + room.getRoomNumber() + " | Block " + room.getBlockName() + " | " + room.getRoomType());
        } catch (Exception e) { roomLabel.setText("Database error: " + e.getMessage()); }
    }

    private void refreshComplaints() {
        try {
            complaintModel.setRowCount(0);
            for (Complaint c : complaintService.getByStudent(user.getStudentId()))
                complaintModel.addRow(new Object[]{c.getComplaintId(), c.getCategory(), c.getDescription(), c.getPriority(), c.getStatus(), c.getCreatedAt()});
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }
}
