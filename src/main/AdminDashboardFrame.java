package com.smarthostel.ui;

import com.smarthostel.dao.DashboardDAO;
import com.smarthostel.dao.ComplaintDAO;
import com.smarthostel.dao.RoomDAO;
import com.smarthostel.dao.StudentDAO;
import com.smarthostel.model.Complaint;
import com.smarthostel.model.Room;
import com.smarthostel.model.Student;
import com.smarthostel.model.User;
import com.smarthostel.service.AllocationService;
import com.smarthostel.service.ComplaintService;
import com.smarthostel.service.RoomRecommendationService;
import com.smarthostel.service.RoomService;
import com.smarthostel.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardFrame extends JFrame {
    private final StudentService studentService = new StudentService();
    private final RoomService roomService = new RoomService();
    private final AllocationService allocationService = new AllocationService();
    private final ComplaintService complaintService = new ComplaintService();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    private final DefaultTableModel studentModel = model("ID", "Name", "Course", "Year", "Phone", "Email");
    private final DefaultTableModel roomModel = model("ID", "Room", "Block", "Capacity", "Occupied", "Type", "Status");
    private final DefaultTableModel complaintModel = model("ID", "Student", "Category", "Description", "Priority", "Status", "Created");
    private final JLabel statsLabel = new JLabel();

    private final JTextField sid = new JTextField();
    private final JTextField sname = new JTextField();
    private final JTextField scourse = new JTextField();
    private final JTextField syear = new JTextField();
    private final JTextField sphone = new JTextField();
    private final JTextField semail = new JTextField();

    private final JTextField roomNumber = new JTextField();
    private final JTextField roomBlock = new JTextField();
    private final JTextField roomCapacity = new JTextField("2");
    private final JComboBox<String> roomType = new JComboBox<>(new String[]{"2-Seater", "3-Seater", "4-Seater"});

    public AdminDashboardFrame(User user) {
        setTitle("Smart Hostel Management System - Admin");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        root.add(header(user), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", dashboardPanel());
        tabs.addTab("Students", studentPanel());
        tabs.addTab("Rooms & Allocation", roomPanel());
        tabs.addTab("Complaints", complaintPanel());
        root.add(tabs, BorderLayout.CENTER);

        add(root);
        refreshStudents();
        refreshRooms();
        refreshComplaints();
        refreshDashboard();
    }

    private JPanel header(User user) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel l = new JLabel("Logged in as: " + user.getUsername() + " (ADMIN)");
        l.setFont(l.getFont().deriveFont(Font.BOLD, 14f));
        p.add(l, BorderLayout.WEST);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        p.add(logout, BorderLayout.EAST);
        return p;
    }

    private JPanel dashboardPanel() {
        JPanel p = new JPanel(new BorderLayout());
        statsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statsLabel.setFont(statsLabel.getFont().deriveFont(Font.BOLD, 18f));
        p.add(statsLabel, BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh Dashboard");
        refresh.addActionListener(e -> refreshDashboard());
        p.add(refresh, BorderLayout.SOUTH);
        return p;
    }

    private JPanel studentPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 6, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Student Details"));
        addField(form, "Student ID", sid);
        addField(form, "Name", sname);
        addField(form, "Course", scourse);
        addField(form, "Year", syear);
        addField(form, "Phone", sphone);
        addField(form, "Email", semail);

        JPanel buttons = new JPanel();
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton clear = new JButton("Clear");
        add.addActionListener(e -> studentAction("add"));
        update.addActionListener(e -> studentAction("update"));
        delete.addActionListener(e -> studentAction("delete"));
        clear.addActionListener(e -> clearStudentForm());
        buttons.add(add); buttons.add(update); buttons.add(delete); buttons.add(clear);

        JTable table = new JTable(studentModel);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int r = table.convertRowIndexToModel(table.getSelectedRow());
                sid.setText(studentModel.getValueAt(r, 0).toString());
                sname.setText(studentModel.getValueAt(r, 1).toString());
                scourse.setText(studentModel.getValueAt(r, 2).toString());
                syear.setText(studentModel.getValueAt(r, 3).toString());
                sphone.setText(studentModel.getValueAt(r, 4).toString());
                semail.setText(studentModel.getValueAt(r, 5).toString());
            }
        });

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER); north.add(buttons, BorderLayout.SOUTH);
        p.add(north, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JPanel roomPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JPanel addRoom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRoom.setBorder(BorderFactory.createTitledBorder("Add Room"));
        addRoom.add(new JLabel("Room:")); addRoom.add(roomNumber);
        addRoom.add(new JLabel("Block:")); addRoom.add(roomBlock);
        addRoom.add(new JLabel("Capacity:")); addRoom.add(roomCapacity);
        addRoom.add(new JLabel("Type:")); addRoom.add(roomType);
        JButton add = new JButton("Add Room");
        JButton delete = new JButton("Delete Selected Room");
        add.addActionListener(e -> addRoom());

        JTable table = new JTable(roomModel);
        table.setAutoCreateRowSorter(true);
        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { info("Select a room first."); return; }
            int modelRow = table.convertRowIndexToModel(r);
            int id = Integer.parseInt(roomModel.getValueAt(modelRow, 0).toString());
            try { roomService.delete(id); refreshRooms(); refreshDashboard(); info("Room deleted."); }
            catch (Exception ex) { error(ex); }
        });
        addRoom.add(add); addRoom.add(delete);

        JPanel allocation = new JPanel(new FlowLayout(FlowLayout.LEFT));
        allocation.setBorder(BorderFactory.createTitledBorder("Smart Room Allocation"));
        JTextField allocationStudent = new JTextField(10);
        JTextField preferredBlock = new JTextField(4);
        JComboBox<String> preferredType = new JComboBox<>(new String[]{"2-Seater", "3-Seater", "4-Seater"});
        JComboBox<String> recommendations = new JComboBox<>();
        JButton recommend = new JButton("Recommend");
        JButton allocate = new JButton("Allocate Selected Recommendation");

        recommend.addActionListener(e -> {
            try {
                List<RoomRecommendationService.Recommendation> recs = roomService.recommend(preferredBlock.getText(), (String) preferredType.getSelectedItem());
                recommendations.removeAllItems();
                for (var rec : recs) recommendations.addItem(rec.room().getRoomId() + " - " + rec.room().getRoomNumber() + " (Score " + rec.score() + ")");
                if (recs.isEmpty()) info("No suitable rooms available.");
            } catch (Exception ex) { error(ex); }
        });
        allocate.addActionListener(e -> {
            if (recommendations.getSelectedIndex() < 0) { info("Generate and select a recommendation first."); return; }
            try {
                String item = recommendations.getSelectedItem().toString();
                int roomId = Integer.parseInt(item.substring(0, item.indexOf(" -")));
                allocationService.allocate(allocationStudent.getText().trim(), roomId);
                refreshRooms(); refreshDashboard();
                info("Room allocated successfully.");
            } catch (Exception ex) { error(ex); }
        });
        allocation.add(new JLabel("Student ID:")); allocation.add(allocationStudent);
        allocation.add(new JLabel("Preferred Block:")); allocation.add(preferredBlock);
        allocation.add(new JLabel("Type:")); allocation.add(preferredType);
        allocation.add(recommend); allocation.add(recommendations); allocation.add(allocate);

        JPanel north = new JPanel(new BorderLayout());
        north.add(addRoom, BorderLayout.NORTH); north.add(allocation, BorderLayout.SOUTH);
        p.add(north, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JPanel complaintPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JTable table = new JTable(complaintModel);
        table.setAutoCreateRowSorter(true);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> status = new JComboBox<>(new String[]{"PENDING", "IN_PROGRESS", "RESOLVED"});
        JButton update = new JButton("Update Selected Complaint");
        JButton refresh = new JButton("Refresh");
        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { info("Select a complaint first."); return; }
            int mr = table.convertRowIndexToModel(r);
            int id = Integer.parseInt(complaintModel.getValueAt(mr, 0).toString());
            try { complaintService.updateStatus(id, (String) status.getSelectedItem()); refreshComplaints(); refreshDashboard(); info("Complaint updated."); }
            catch (Exception ex) { error(ex); }
        });
        refresh.addActionListener(e -> refreshComplaints());
        controls.add(new JLabel("New Status:")); controls.add(status); controls.add(update); controls.add(refresh);
        p.add(controls, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void studentAction(String action) {
        try {
            if ("delete".equals(action)) {
                studentService.delete(sid.getText()); clearStudentForm(); refreshStudents(); refreshDashboard(); info("Student deleted."); return;
            }
            if ("add".equals(action)) studentService.add(sid.getText(), sname.getText(), scourse.getText(), syear.getText(), sphone.getText(), semail.getText());
            else studentService.update(sid.getText(), sname.getText(), scourse.getText(), syear.getText(), sphone.getText(), semail.getText());
            refreshStudents(); refreshDashboard(); info("Student " + action + "ed successfully.");
        } catch (Exception ex) { error(ex); }
    }

    private void addRoom() {
        try {
            roomService.add(roomNumber.getText(), roomBlock.getText(), roomCapacity.getText(), (String) roomType.getSelectedItem());
            refreshRooms(); refreshDashboard(); roomNumber.setText(""); roomBlock.setText(""); roomCapacity.setText("2"); info("Room added.");
        } catch (Exception ex) { error(ex); }
    }

    private void refreshStudents() {
        try { setStudents(studentService.getAll()); } catch (Exception ex) { error(ex); }
    }
    private void setStudents(List<Student> students) {
        studentModel.setRowCount(0);
        students.forEach(s -> studentModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getCourse(), s.getYear(), s.getPhone(), s.getEmail()}));
    }
    private void refreshRooms() {
        try {
            roomModel.setRowCount(0);
            for (Room r : roomService.getAll()) roomModel.addRow(new Object[]{r.getRoomId(), r.getRoomNumber(), r.getBlockName(), r.getCapacity(), r.getOccupied(), r.getRoomType(), r.getStatus()});
        } catch (Exception ex) { error(ex); }
    }
    private void refreshComplaints() {
        try {
            complaintModel.setRowCount(0);
            for (Complaint c : complaintService.getAll()) complaintModel.addRow(new Object[]{c.getComplaintId(), c.getStudentId(), c.getCategory(), c.getDescription(), c.getPriority(), c.getStatus(), c.getCreatedAt()});
        } catch (Exception ex) { error(ex); }
    }
    private void refreshDashboard() {
        try {
            statsLabel.setText("<html><div style='text-align:center'>HOSTEL DASHBOARD<br><br>" +
                    "Students: " + dashboardDAO.countStudents() + " &nbsp;&nbsp; | &nbsp;&nbsp; Rooms: " + dashboardDAO.countRooms() +
                    " &nbsp;&nbsp; | &nbsp;&nbsp; Available Rooms: " + dashboardDAO.countAvailableRooms() +
                    "<br><br>Pending / In-progress Complaints: " + dashboardDAO.countPendingComplaints() + "</div></html>");
        } catch (Exception ex) { error(ex); }
    }

    private void clearStudentForm() { sid.setText(""); sname.setText(""); scourse.setText(""); syear.setText(""); sphone.setText(""); semail.setText(""); }
    private static DefaultTableModel model(String... columns) { return new DefaultTableModel(columns, 0) { public boolean isCellEditable(int r, int c) { return false; } }; }
    private static void addField(JPanel p, String label, JTextField field) { p.add(new JLabel(label)); p.add(field); }
    private void info(String msg) { JOptionPane.showMessageDialog(this, msg); }
    private void error(Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
}
