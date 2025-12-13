package view;

import DataBase.database;
import org.bson.Document;
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
    private database DB; // Database instance

    // Colors for status
    private static final Color CONFIRMED_COLOR = new Color(46, 125, 50); // Green
    private static final Color PENDING_COLOR = new Color(245, 124, 0);   // Orange
    private static final Color CANCELLED_COLOR = new Color(198, 40, 40); // Red

    public PatientNotificationSystem() {
        // Initialize UI first
        initializeUI();

        // Then initialize database
        try {
            // Initialize database connection
            DB = new database();
            logToConsole("INFO: Database connection initialized successfully.");
        } catch (Exception e) {
            logToConsole("ERROR: Failed to initialize database connection: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Failed to connect to database. Please check your connection.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }

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
        JLabel titleLabel = new JLabel("Patient Notification System", SwingConstants.CENTER);
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

        searchButton = new JButton(" Search Notifications");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setBackground(new Color(33, 97, 140));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        clearButton = new JButton(" Clear");
        clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearButton.setBackground(new Color(100, 100, 120));
        clearButton.setForeground(Color.BLACK);
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

        // Check if database is connected
        if (DB == null) {
            logToConsole("ERROR: Database connection is not available.");
            JOptionPane.showMessageDialog(this,
                    "Database connection is not available. Please restart the application.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get notifications for the patient from database
        List<Notification> notifications = getPatientNotificationsFromDB(patientId);

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

    /**
     * Retrieves notifications for a specific patient from the database
     */
    private List<Notification> getPatientNotificationsFromDB(String patientId) {
        List<Notification> notifications = new ArrayList<>();

        try {
            // Call the database method to get notifications
            List<Document> notificationDocuments = DB.getPatientNotifications(patientId);

            if (notificationDocuments.isEmpty()) {
                logToConsole("INFO: Database returned empty result for Patient ID: " + patientId);
            } else {
                // Convert Document objects to Notification objects
                for (Document doc : notificationDocuments) {
                    String appointmentId = doc.getString("appointmentId");
                    String doctorName = doc.getString("doctorName");
                    String specialty = doc.getString("specialty");
                    String timeSlot = doc.getString("timeSlot");
                    String status = doc.getString("status");

                    // Create Notification object and add to list
                    Notification notification = new Notification(
                            appointmentId,
                            doctorName,
                            specialty,
                            timeSlot,
                            status
                    );
                    notifications.add(notification);

                    // Log the notification to console (optional)
                    logToConsole("DEBUG: Retrieved - Appointment ID: " + appointmentId +
                            ", Doctor: " + doctorName + ", Status: " + status);
                }
            }
        } catch (Exception e) {
            logToConsole("ERROR: Failed to retrieve notifications from database: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error retrieving data from database: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return notifications;
    }

    private void clearTable() {
        tableModel.setRowCount(0);
        statusLabel.setText("Table cleared");
        logToConsole("INFO: Notification table cleared.");
    }

    private void logToConsole(String message) {
        if (consoleArea != null) {
            String timestamp = String.format("[%tT]", new java.util.Date());
            consoleArea.append(timestamp + " " + message + "\n");
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        } else {
            // Fallback to System.out if consoleArea is not initialized yet
            System.out.println(message);
        }
    }

    private void loadInitialData() {
        // Load initial data for default patient ID (P001)
        String defaultPatientId = "P001";

        // Only load data if database is connected
        if (DB != null) {
            List<Notification> initialNotifications = getPatientNotificationsFromDB(defaultPatientId);

            if (!initialNotifications.isEmpty()) {
                for (Notification n : initialNotifications) {
                    tableModel.addRow(n.toTableRow());
                }
                statusLabel.setText("Loaded " + initialNotifications.size() + " notification(s) for Patient ID: " + defaultPatientId);
                logToConsole("INFO: Initial data loaded for Patient ID: " + defaultPatientId);
            } else {
                statusLabel.setText("No notifications found for Patient ID: " + defaultPatientId);
                logToConsole("INFO: No initial data found for Patient ID: " + defaultPatientId);
            }
        } else {
            statusLabel.setText("Database not connected. Please check connection.");
            logToConsole("WARNING: Database not connected. Cannot load initial data.");
        }
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