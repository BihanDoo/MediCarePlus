package view;

import DataBase.database;
import org.bson.Document;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DoctorManagementForm extends JFrame {

    private JTextField txtName, txtSpecialty, txtAvailable;
    private JCheckBox chkAvailable;
    private JTextArea txtTimeSlots;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnRefresh;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private database DB;

    public DoctorManagementForm() {
        DB = new database(); // Initialize database connection

        setTitle("Doctor Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create panels
        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Load data on startup
        refreshTable();

        setLocationRelativeTo(null);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add/Edit Doctor"));
        panel.setPreferredSize(new Dimension(350, 400));

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        fieldsPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        fieldsPanel.add(txtName);

        fieldsPanel.add(new JLabel("Specialty:"));
        txtSpecialty = new JTextField();
        fieldsPanel.add(txtSpecialty);

        fieldsPanel.add(new JLabel("Available:"));
        chkAvailable = new JCheckBox();
        fieldsPanel.add(chkAvailable);

        fieldsPanel.add(new JLabel("Time Slots:"));
        txtAvailable = new JTextField();
        txtAvailable.setToolTipText("This field is for display only - use the text area below for editing");
        txtAvailable.setEditable(false);
        txtAvailable.setBackground(new Color(240, 240, 240));
        fieldsPanel.add(txtAvailable);

        fieldsPanel.add(new JLabel("Edit Time Slots:"));
        panel.add(fieldsPanel, BorderLayout.NORTH);

        // Time slots text area with scroll
        txtTimeSlots = new JTextArea(6, 20);
        txtTimeSlots.setLineWrap(true);
        txtTimeSlots.setWrapStyleWord(true);
        txtTimeSlots.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(txtTimeSlots);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Time Slots (one per line, e.g., Monday 09:00-11:00)"));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Info label
        JLabel infoLabel = new JLabel("Enter time slots one per line (e.g., Monday 09:00-11:00)");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        infoLabel.setForeground(Color.GRAY);
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Doctors List"));

        String[] columns = {"Doctor ID", "Name", "Specialty", "Available", "Time Slots"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return Boolean.class; // Available column
                return String.class;
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.setRowHeight(25);

        // Set column widths
        doctorTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        doctorTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        doctorTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Specialty
        doctorTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Available
        doctorTable.getColumnModel().getColumn(4).setPreferredWidth(300); // Time Slots

        // When row is selected, load data into form
        doctorTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && doctorTable.getSelectedRow() >= 0) {
                loadSelectedDoctor();
            }
        });

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAdd = new JButton("Add Doctor");
        btnUpdate = new JButton("Update Doctor");
        btnDelete = new JButton("Delete Doctor");
        btnClear = new JButton("Clear Form");
        btnRefresh = new JButton("Refresh Table");

        // Button actions
        btnAdd.addActionListener(this::addDoctor);
        btnUpdate.addActionListener(this::updateDoctor);
        btnDelete.addActionListener(this::deleteDoctor);
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> refreshTable());

        // Colors
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.BLACK);
        btnUpdate.setBackground(new Color(33, 150, 243));
        btnUpdate.setForeground(Color.BLACK);
        btnDelete.setBackground(new Color(244, 67, 54));
        btnDelete.setForeground(Color.BLACK);
        btnRefresh.setBackground(new Color(255, 193, 7));
        btnRefresh.setForeground(Color.BLACK);

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);
        panel.add(btnRefresh);

        return panel;
    }

    // ========== CORE METHODS ==========

    private void addDoctor(ActionEvent e) {
        try {
            String name = txtName.getText().trim();
            String specialty = txtSpecialty.getText().trim();
            boolean available = chkAvailable.isSelected();
            String timeSlotsText = txtTimeSlots.getText().trim();

            if (name.isEmpty() || specialty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Specialty are required!");
                return;
            }

            if (timeSlotsText.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "No time slots provided. Continue?",
                        "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Parse time slots lines
            List<String> timeSlots = parseTimeSlots(timeSlotsText);

            // Call database method - Note: database will generate the doctorId automatically
            DB.addDoctor(name, specialty, available, timeSlots);
            JOptionPane.showMessageDialog(this, "Doctor added successfully!");

            refreshTable();
            clearForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateDoctor(ActionEvent e) {
        int row = doctorTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor from the table first!");
            return;
        }

        try {
            String doctorId = (String) tableModel.getValueAt(row, 0);
            String name = txtName.getText().trim();
            String specialty = txtSpecialty.getText().trim();
            boolean available = chkAvailable.isSelected();
            String timeSlotsText = txtTimeSlots.getText().trim();

            if (name.isEmpty() || specialty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Specialty are required!");
                return;
            }

            if (timeSlotsText.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "No time slots provided. This will remove all time slots. Continue?",
                        "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Parse time slots lines
            List<String> timeSlots = parseTimeSlots(timeSlotsText);

            // Call database method
            DB.updateDoctor(doctorId, name, specialty, available, timeSlots);
            JOptionPane.showMessageDialog(this, "Doctor updated successfully!");

            refreshTable();
            clearForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteDoctor(ActionEvent e) {
        int row = doctorTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor from the table first!");
            return;
        }

        String doctorId = (String) tableModel.getValueAt(row, 0);
        String doctorName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete:\n" + doctorName + " (ID: " + doctorId + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Call database method
                DB.deleteDoctor(doctorId);
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully!");

                refreshTable();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void loadSelectedDoctor() {
        int row = doctorTable.getSelectedRow();
        if (row == -1) return;

        try {
            String doctorId = (String) tableModel.getValueAt(row, 0);

            // Fetch complete doctor document from database
            List<Document> allDoctors = DB.getAllDoctors();
            Document selectedDoctor = null;

            for (Document doc : allDoctors) {
                if (doc.getString("doctorId").equals(doctorId)) {
                    selectedDoctor = doc;
                    break;
                }
            }

            if (selectedDoctor != null) {
                txtName.setText(selectedDoctor.getString("name"));
                txtSpecialty.setText(selectedDoctor.getString("specialty"));
                chkAvailable.setSelected(selectedDoctor.getBoolean("available"));

                // Get time slots
                List<String> timeSlots = selectedDoctor.getList("timeSlots", String.class);
                StringBuilder timeSlotsText = new StringBuilder();
                for (String slot : timeSlots) {
                    timeSlotsText.append(slot).append("\n");
                }
                txtTimeSlots.setText(timeSlotsText.toString().trim());

                // Display first few time slots in the read-only field
                if (!timeSlots.isEmpty()) {
                    txtAvailable.setText(formatTimeSlotsForDisplay(timeSlots));
                } else {
                    txtAvailable.setText("No time slots");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading doctor details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        try {
            // Clear table
            tableModel.setRowCount(0);

            // Get all doctors from database
            List<Document> doctors = DB.getAllDoctors();

            // Add to table
            for (Document doctor : doctors) {
                // Get time slots for display
                List<String> timeSlots = doctor.getList("timeSlots", String.class);
                String timeSlotsDisplay = formatTimeSlotsForDisplay(timeSlots);

                Object[] row = {
                        doctor.getString("doctorId"),
                        doctor.getString("name"),
                        doctor.getString("specialty"),
                        doctor.getBoolean("available"),
                        timeSlotsDisplay
                };
                tableModel.addRow(row);
            }

            // Show count in title
            setTitle("Doctor Management - " + doctors.size() + " doctors");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading doctors: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private List<String> parseTimeSlots(String timeSlotsText) {
        // Split by newline and filter out empty lines
        return List.of(timeSlotsText.split("\\r?\\n"))
                .stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
    }

    private String formatTimeSlotsForDisplay(List<String> timeSlots) {
        if (timeSlots == null || timeSlots.isEmpty()) {
            return "No time slots";
        }

        if (timeSlots.size() <= 3) {
            return String.join("; ", timeSlots);
        }

        // Show first 3 items and count of remaining
        return timeSlots.get(0) + "; " + timeSlots.get(1) + "; " + timeSlots.get(2) +
                " (+" + (timeSlots.size() - 3) + " more)";
    }

    private void clearForm() {
        txtName.setText("");
        txtSpecialty.setText("");
        txtAvailable.setText("");
        txtTimeSlots.setText("");
        chkAvailable.setSelected(true);
        doctorTable.clearSelection();
    }

    // Main method to test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            DoctorManagementForm form = new DoctorManagementForm();
            form.setVisible(true);
        });
    }
}