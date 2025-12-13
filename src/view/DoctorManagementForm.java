package view;

import DataBase.database;
import org.bson.Document;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DoctorManagementForm extends JFrame {

    private JTextField txtName, txtSpecialization, txtAvailable;
    private JCheckBox chkActive;
    private JTextArea txtSchedule;
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

        fieldsPanel.add(new JLabel("Specialization:"));
        txtSpecialization = new JTextField();
        fieldsPanel.add(txtSpecialization);

        fieldsPanel.add(new JLabel("Active:"));
        chkActive = new JCheckBox();
        fieldsPanel.add(chkActive);

        fieldsPanel.add(new JLabel("Available Days/Hours:"));
        txtAvailable = new JTextField();
        txtAvailable.setToolTipText("e.g., Monday 09:00-17:00, Tuesday 10:00-16:00");
        fieldsPanel.add(txtAvailable);

        fieldsPanel.add(new JLabel("Schedule:"));
        panel.add(fieldsPanel, BorderLayout.NORTH);

        // Schedule text area with scroll
        txtSchedule = new JTextArea(6, 20);
        txtSchedule.setLineWrap(true);
        txtSchedule.setWrapStyleWord(true);
        txtSchedule.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(txtSchedule);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Schedule (one per line)"));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Info label
        JLabel infoLabel = new JLabel("Enter schedule items one per line (e.g., Monday 09:00-11:00)");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        infoLabel.setForeground(Color.GRAY);
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Doctors List"));

        String[] columns = {"Doctor ID", "Name", "Specialization", "Active", "Schedule"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return Boolean.class; // Active column
                return String.class;
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.setRowHeight(25);

        // Set column widths
        doctorTable.getColumnModel().getColumn(0).setPreferredWidth(80); // ID
        doctorTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        doctorTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Specialization
        doctorTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Active
        doctorTable.getColumnModel().getColumn(4).setPreferredWidth(250); // Schedule

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
            String specialization = txtSpecialization.getText().trim();
            boolean active = chkActive.isSelected();
            String available = txtAvailable.getText().trim();
            String scheduleText = txtSchedule.getText().trim();

            if (name.isEmpty() || specialization.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Specialization are required!");
                return;
            }

            // Parse schedule lines
            List<String> schedule = parseSchedule(scheduleText);

            // Call database method
            DB.addDoctor(name, specialization, active, schedule);
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
            String specialization = txtSpecialization.getText().trim();
            boolean active = chkActive.isSelected();
            String available = txtAvailable.getText().trim();
            String scheduleText = txtSchedule.getText().trim();

            if (name.isEmpty() || specialization.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Specialization are required!");
                return;
            }

            // Parse schedule lines
            List<String> schedule = parseSchedule(scheduleText);

            // Call database method
            DB.updateDoctor(doctorId, name, specialization, active, schedule);
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
                // Call database method - Note: You need to implement deleteDoctor in your database class
                // DB.deleteDoctor(doctorId);
                JOptionPane.showMessageDialog(this, "Delete functionality needs to be implemented in database class!");

                // For now, just refresh
                refreshTable();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void loadSelectedDoctor() {
        int row = doctorTable.getSelectedRow();
        if (row == -1) return;

        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtSpecialization.setText((String) tableModel.getValueAt(row, 2));
        chkActive.setSelected((Boolean) tableModel.getValueAt(row, 3));

        // Get schedule from table (it might be truncated in the table view)
        String scheduleCell = (String) tableModel.getValueAt(row, 4);
        txtSchedule.setText(scheduleCell);

        // Note: Available field might not be in the table, you might need to fetch the full document
        // For now, clear it or you could store it in a hidden field
        txtAvailable.setText("");
    }

    private void refreshTable() {
        try {
            // Clear table
            tableModel.setRowCount(0);

            // Get all doctors from database
            List<Document> doctors = DB.getAllDoctors();

            // Add to table
            for (Document doctor : doctors) {
                // Format schedule for display (show first 2 items or truncated)
                List<String> scheduleList = doctor.getList("schedule", String.class);
                String scheduleDisplay = formatScheduleForDisplay(scheduleList);

                Object[] row = {
                        doctor.getString("doctorId"),
                        doctor.getString("name"),
                        doctor.getString("specialization"),
                        doctor.getBoolean("active"),
                        scheduleDisplay
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

    private List<String> parseSchedule(String scheduleText) {
        // Split by newline and filter out empty lines
        return List.of(scheduleText.split("\\r?\\n"))
                .stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
    }

    private String formatScheduleForDisplay(List<String> schedule) {
        if (schedule == null || schedule.isEmpty()) {
            return "No schedule";
        }

        if (schedule.size() <= 2) {
            return String.join("; ", schedule);
        }

        // Show first 2 items and count of remaining
        return schedule.get(0) + "; " + schedule.get(1) +
                " (+" + (schedule.size() - 2) + " more)";
    }

    private void clearForm() {
        txtName.setText("");
        txtSpecialization.setText("");
        txtAvailable.setText("");
        txtSchedule.setText("");
        chkActive.setSelected(true);
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