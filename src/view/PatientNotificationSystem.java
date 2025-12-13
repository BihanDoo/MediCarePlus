package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PatientNotificationSystem extends JFrame {

    // Define a Notification class to hold our data
    static class Notification {
        private String appointmentId;
        private String doctorName;
        private String specialty;
        private String timeSlot;
        private String status;

        // Constructor
        public Notification(String appointmentId, String doctorName,
                            String specialty, String timeSlot, String status) {
            this.appointmentId = appointmentId;
            this.doctorName = doctorName;
            this.specialty = specialty;
            this.timeSlot = timeSlot;
            this.status = status;
        }

        // Getter methods
        public String getAppointmentId() { return appointmentId; }
        public String getDoctorName() { return doctorName; }
        public String getSpecialty() { return specialty; }
        public String getTimeSlot() { return timeSlot; }
        public String getStatus() { return status; }

        // For table display
        public Object[] toTableRow() {
            return new Object[]{appointmentId, doctorName, specialty, timeSlot, status};
        }
    }

    // UI Components
    private JTextField patientIdField;
    private JButton searchButton;
    private JButton clearButton;
    private JTable notificationsTable;
    private DefaultTableModel tableModel;
    private JTextArea consoleArea;
    private JLabel statusLabel;

    // Colors for status
    private static final Color CONFIRMED_COLOR = new Color(46, 125, 50); // Green
    private static final Color PENDING_COLOR = new Color(245, 124, 0);   // Orange
    private static final Color CANCELLED_COLOR = new Color(198, 40, 40); // Red

    public PatientNotificationSystem() {
        initializeUI();
        loadInitialData();
    }

    private void initializeUI() {
        // Set up the main frame
        setTitle("Patient Notification System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Center the window

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 245, 255));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create center panel with search and table
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create console panel
        JPanel consolePanel = createConsolePanel();
        mainPanel.add(consolePanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 97, 140));

        // Title label
        JLabel titleLabel = new JLabel("🏥 Patient Notification System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Subtitle
        JLabel subtitleLabel = new JLabel("View appointment notifications and status updates", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 230, 255));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(33, 97, 140));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                "Search Patient Notifications",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(60, 60, 60)
        ));

        JLabel patientIdLabel = new JLabel("Patient ID:");
        patientIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        patientIdField = new JTextField(15);
        patientIdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        patientIdField.setText("P001");

        searchButton = new JButton("🔍 Search Notifications");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setBackground(new Color(33, 97, 140));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        clearButton = new JButton("🗑️ Clear");
        clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearButton.setBackground(new Color(100, 100, 120));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Status label
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        statusLabel.setForeground(new Color(80, 80, 80));

        searchPanel.add(patientIdLabel);
        searchPanel.add(patientIdField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(statusLabel);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                "Notifications",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(60, 60, 60)
        ));

        // Create table model
        String[] columnNames = {"Appointment ID", "Doctor Name", "Specialty", "Time Slot", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        notificationsTable = new JTable(tableModel);
        notificationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificationsTable.setRowHeight(25);
        notificationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificationsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        notificationsTable.getTableHeader().setBackground(new Color(230, 240, 255));

        // Set column widths
        notificationsTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Appointment ID
        notificationsTable.getColumnModel().getColumn(1).setPreferredWidth(180); // Doctor Name
        notificationsTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Specialty
        notificationsTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Time Slot
        notificationsTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Status

        // Custom renderer for status column
        notificationsTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(notificationsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to center panel
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        // Add action listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchNotifications();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
            }
        });

        // Add Enter key listener to patientIdField
        patientIdField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchNotifications();
            }
        });

        return centerPanel;
    }

    private JPanel createConsolePanel() {
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
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        consoleArea.setEditable(false);
        consoleArea.setBackground(new Color(250, 250, 250));
        consoleArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane consoleScroll = new JScrollPane(consoleArea);
        consoleScroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        consolePanel.add(consoleScroll, BorderLayout.CENTER);

        // Add timestamp to console
        logToConsole("System initialized. Ready to search patient notifications.");

        return consolePanel;
    }

    // Custom cell renderer for status column
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 4 && value != null) { // Status column
                String status = value.toString().toLowerCase();
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 11));

                if (status.contains("confirmed")) {
                    setForeground(Color.WHITE);
                    setBackground(CONFIRMED_COLOR);
                } else if (status.contains("pending")) {
                    setForeground(Color.WHITE);
                    setBackground(PENDING_COLOR);
                } else if (status.contains("cancelled")) {
                    setForeground(Color.WHITE);
                    setBackground(CANCELLED_COLOR);
                } else {
                    setForeground(Color.BLACK);
                    setBackground(Color.LIGHT_GRAY);
                }

                // Set border for better visibility
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(2, 5, 2, 5)
                ));

                setOpaque(true);
            } else {
                setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            }

            return c;
        }
    }

    private void searchNotifications() {
        String patientId = patientIdField.getText().trim();

        if (patientId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Patient ID",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Clear previous results
        clearTable();

        // Get notifications for the patient
        List<Notification> notifications = getPatientNotifications(patientId);

        if (notifications.isEmpty()) {
            statusLabel.setText("No notifications found for Patient ID: " + patientId);
            logToConsole("INFO: No notifications found for Patient ID: " + patientId);
            JOptionPane.showMessageDialog(this,
                    "No notifications available for Patient ID: " + patientId,
                    "No Data Found",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Add notifications to table
            for (Notification n : notifications) {
                tableModel.addRow(n.toTableRow());
            }

            statusLabel.setText("Found " + notifications.size() + " notification(s) for Patient ID: " + patientId);
            logToConsole("SUCCESS: Loaded " + notifications.size() + " notification(s) for Patient ID: " + patientId);

            // Show success message
            JOptionPane.showMessageDialog(this,
                    "Successfully loaded " + notifications.size() + " notification(s)",
                    "Search Complete",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
        statusLabel.setText("Table cleared");
        logToConsole("INFO: Notification table cleared.");
    }

    private void logToConsole(String message) {
        String timestamp = String.format("[%tT]", new java.util.Date());
        consoleArea.append(timestamp + " " + message + "\n");
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    /**
     * Retrieves notifications for a specific patient
     */
    public static List<Notification> getPatientNotifications(String patientId) {
        List<Notification> notifications = new ArrayList<>();

        // Validate input
        if (patientId == null || patientId.trim().isEmpty()) {
            return notifications;
        }

        // Simulate database query - return different data based on patient ID
        switch (patientId.toUpperCase()) {
            case "P001":
                notifications.add(new Notification(
                        "APT1001",
                        "Dr. Sarah Johnson",
                        "Cardiology",
                        "2024-01-15 10:30 AM",
                        "Confirmed"
                ));
                notifications.add(new Notification(
                        "APT1002",
                        "Dr. Michael Chen",
                        "Neurology",
                        "2024-01-16 02:00 PM",
                        "Pending"
                ));
                notifications.add(new Notification(
                        "APT1003",
                        "Dr. Emily Williams",
                        "Dermatology",
                        "2024-01-18 11:15 AM",
                        "Confirmed"
                ));
                notifications.add(new Notification(
                        "APT1004",
                        "Dr. Robert Kim",
                        "Orthopedics",
                        "2024-01-20 03:45 PM",
                        "Cancelled"
                ));
                break;

            case "P002":
                notifications.add(new Notification(
                        "APT2001",
                        "Dr. Jennifer Lee",
                        "Pediatrics",
                        "2024-01-17 09:00 AM",
                        "Confirmed"
                ));
                notifications.add(new Notification(
                        "APT2002",
                        "Dr. David Miller",
                        "General Surgery",
                        "2024-01-19 01:30 PM",
                        "Pending"
                ));
                break;

            case "P003":
                notifications.add(new Notification(
                        "APT3001",
                        "Dr. Amanda Wilson",
                        "Ophthalmology",
                        "2024-01-21 10:00 AM",
                        "Confirmed"
                ));
                break;

            case "P004":
                // Patient with no notifications
                break;

            default:
                // For any other patient ID, return generic data
                if (patientId.startsWith("P") || patientId.startsWith("p")) {
                    notifications.add(new Notification(
                            "APT" + (1000 + (int)(Math.random() * 1000)),
                            "Dr. General Practitioner",
                            "General Medicine",
                            "2024-01-" + (15 + (int)(Math.random() * 10)) + " " +
                                    (9 + (int)(Math.random() * 8)) + ":" +
                                    (new String[]{"00", "15", "30", "45"})[(int)(Math.random() * 4)] +
                                    " " + (new String[]{"AM", "PM"})[(int)(Math.random() * 2)],
                            (new String[]{"Confirmed", "Pending", "Cancelled"})[(int)(Math.random() * 3)]
                    ));
                }
                break;
        }

        return notifications;
    }

    private void loadInitialData() {
        // Load initial data for P001
        List<Notification> initialNotifications = getPatientNotifications("P001");
        for (Notification n : initialNotifications) {
            tableModel.addRow(n.toTableRow());
        }
        statusLabel.setText("Loaded " + initialNotifications.size() + " notification(s) for Patient ID: P001");
        logToConsole("INFO: Initial data loaded for Patient ID: P001");
    }

    public static void main(String[] args) {
        // Set Look and Feel to System Default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PatientNotificationSystem app = new PatientNotificationSystem();
                app.setVisible(true);
            }
        });
    }
}