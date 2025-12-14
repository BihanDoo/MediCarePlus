package view;

import DataBase.database;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorNotificationSystem extends JFrame {

    // Appointment class to hold data
    static class Appointment {
        private String appointmentId;
        private String patientId;
        private String patientName;
        private String reason;
        private String timeSlot;
        private String status;

        public Appointment(String appointmentId, String patientId, String patientName,
                           String reason, String timeSlot, String status) {
            this.appointmentId = appointmentId;
            this.patientId = patientId;
            this.patientName = patientName;
            this.reason = reason;
            this.timeSlot = timeSlot;
            this.status = status;
        }

        public Object[] toTableRow() {
            return new Object[]{patientId, patientName, reason, status, timeSlot};
        }
    }

    // UI Components
    private JTextField doctorIdField;
    private JButton searchButton;
    private JButton clearButton;
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JTextArea consoleArea;
    private JLabel statusLabel;
    private database DB;

    // Status colors
    private static final Color SCHEDULED_COLOR = new Color(46, 125, 50); // Green
    private static final Color PENDING_COLOR = new Color(245, 124, 0);   // Orange
    private static final Color CANCELLED_COLOR = new Color(198, 40, 40); // Red

    public DoctorNotificationSystem() {
        initializeUI();

        try {
            DB = new database();
            logToConsole("INFO: Database connection initialized successfully.");
        } catch (Exception e) {
            logToConsole("ERROR: Failed to initialize database connection: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Failed to connect to database. Please check your connection.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() {
        setTitle("Doctor Notification System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 245, 255));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 97, 140));
        JLabel titleLabel = new JLabel("Doctor Notification System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                "Search Appointments by Doctor ID",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(60, 60, 60)
        ));

        doctorIdField = new JTextField(15);
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");

        searchPanel.add(new JLabel("Doctor ID:"));
        searchPanel.add(doctorIdField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Patient ID", "Patient Name", "Reason", "Status", "Time Slot"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setRowHeight(25);
        appointmentsTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Console
        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                "System Log",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(60, 60, 60)
        ));
        consoleArea = new JTextArea(5, 50);
        consoleArea.setEditable(false);
        consolePanel.add(new JScrollPane(consoleArea), BorderLayout.CENTER);
        mainPanel.add(consolePanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Actions
        searchButton.addActionListener(e -> searchAppointments());
        clearButton.addActionListener(e -> tableModel.setRowCount(0));
    }

    private void searchAppointments() {
        String doctorId = doctorIdField.getText().trim();
        if (doctorId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Doctor ID", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0); // Clear table

        if (DB == null) {
            logToConsole("ERROR: Database not connected");
            return;
        }

        List<Document> appointments = DB.getAppointmentsByDoctorId(doctorId);

        if (appointments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointments found for Doctor ID: " + doctorId, "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Document doc : appointments) {
            String patientId = doc.getString("patientId");
            String patientName = DB.getPatientNameById(patientId);
            String reason = doc.getString("reason");
            String status = doc.getString("status");
            String timeSlot = doc.getString("timeSlot");

            tableModel.addRow(new Appointment("", patientId, patientName, reason, timeSlot, status).toTableRow());
        }

        logToConsole("Loaded " + appointments.size() + " appointment(s) for Doctor ID: " + doctorId);
    }

    // Status column color
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null) {
                String status = value.toString().toLowerCase();
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                switch (status) {
                    case "scheduled", "confirmed" -> { setBackground(SCHEDULED_COLOR); setForeground(Color.WHITE); }
                    case "pending" -> { setBackground(PENDING_COLOR); setForeground(Color.WHITE); }
                    case "cancelled" -> { setBackground(CANCELLED_COLOR); setForeground(Color.WHITE); }
                    default -> { setBackground(Color.LIGHT_GRAY); setForeground(Color.BLACK); }
                }
                setOpaque(true);
            }
            return c;
        }
    }

    private void logToConsole(String message) {
        String timestamp = String.format("[%tT]", new java.util.Date());
        consoleArea.append(timestamp + " " + message + "\n");
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorNotificationSystem().setVisible(true));
    }
}
