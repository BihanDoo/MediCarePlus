package view;

import DataBase.PatientDatabase;
import model.Patient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PatientManagementForm extends JFrame {
    private PatientDatabase patientDB;

    // Form components
    private JTextField txtId, txtName, txtDob, txtGender, txtPhone, txtEmail, txtAddress;
    private JTextArea txtMedicalHistory;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch, btnRefresh;
    private JTable patientTable;
    private DefaultTableModel tableModel;

    public PatientManagementForm() {
        patientDB = new PatientDatabase();

        // Setup window
        setTitle("Patient Management - MediCare Plus");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create panels
        JPanel leftPanel = createFormPanel();
        JPanel rightPanel = createTablePanel();
        JPanel buttonPanel = createButtonPanel();

        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        refreshTable();

        // Center window on screen
        setLocationRelativeTo(null);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Patient Details"));
        panel.setPreferredSize(new Dimension(350, 500));

        // ID field (read-only for auto-generated)
        panel.add(new JLabel("Patient ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setText("Auto-generated");
        panel.add(txtId);

        // Name
        panel.add(new JLabel("Full Name*:"));
        txtName = new JTextField();
        panel.add(txtName);

        // Date of Birth
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        txtDob = new JTextField();
        panel.add(txtDob);

        // Gender
        panel.add(new JLabel("Gender:"));
        txtGender = new JTextField();
        panel.add(txtGender);

        // Phone
        panel.add(new JLabel("Phone Number:"));
        txtPhone = new JTextField();
        panel.add(txtPhone);

        // Email
        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        // Address
        panel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        panel.add(txtAddress);

        // Medical History (bigger area)
        panel.add(new JLabel("Medical History:"));
        JScrollPane scrollPane = new JScrollPane();
        txtMedicalHistory = new JTextArea(3, 20);
        scrollPane.setViewportView(txtMedicalHistory);
        panel.add(scrollPane);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Patient List"));

        // Create table
        String[] columns = {"ID", "Name", "Phone", "Email", "Gender"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add row selection listener
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedPatient();
            }
        });

        JScrollPane scrollPane = new JScrollPane(patientTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAdd = new JButton("Add New Patient");
        btnUpdate = new JButton("Update Patient");
        btnDelete = new JButton("Delete Patient");
        btnClear = new JButton("Clear Form");
        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh List");

        // Set button colors
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(33, 150, 243));
        btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(244, 67, 54));
        btnDelete.setForeground(Color.WHITE);

        // Add action listeners
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePatient();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePatient();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);
        panel.add(btnRefresh);

        return panel;
    }

    // ========== ACTION METHODS ==========

    private void addPatient() {
        if (!validateForm()) return;

        Patient patient = createPatientFromForm();
        boolean success = patientDB.addPatient(patient);

        if (success) {
            JOptionPane.showMessageDialog(this, "Patient added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add patient!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to update!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateForm()) return;

        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        Patient patient = createPatientFromForm();
        patient.setPatientId(patientId);

        boolean success = patientDB.updatePatient(patient);

        if (success) {
            JOptionPane.showMessageDialog(this, "Patient updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update patient!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        String patientName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete patient:\n" + patientName + " (ID: " + patientId + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = patientDB.deletePatient(patientId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete patient!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) return;

        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientDB.getPatientById(patientId);

        if (patient != null) {
            txtId.setText(String.valueOf(patient.getPatientId()));
            txtName.setText(patient.getName());
            txtDob.setText(patient.getDateOfBirth());
            txtGender.setText(patient.getGender());
            txtPhone.setText(patient.getPhoneNumber());
            txtEmail.setText(patient.getEmail());
            txtAddress.setText(patient.getAddress());
            txtMedicalHistory.setText(patient.getMedicalHistory());
        }
    }

    private void refreshTable() {
        // Clear table
        tableModel.setRowCount(0);

        // Get all patients
        List<Patient> patients = patientDB.getAllPatients();

        // Add to table
        for (Patient patient : patients) {
            Object[] row = {
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getPhoneNumber(),
                    patient.getEmail(),
                    patient.getGender()
            };
            tableModel.addRow(row);
        }

        // Update status
        setTitle("Patient Management - " + patients.size() + " patients");
    }

    private void clearForm() {
        txtId.setText("Auto-generated");
        txtName.setText("");
        txtDob.setText("");
        txtGender.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtMedicalHistory.setText("");
        patientTable.clearSelection();
    }

    private Patient createPatientFromForm() {
        return new Patient(
                0, // ID will be set by database
                txtName.getText().trim(),
                txtDob.getText().trim(),
                txtGender.getText().trim(),
                txtPhone.getText().trim(),
                txtEmail.getText().trim(),
                txtAddress.getText().trim(),
                txtMedicalHistory.getText().trim()
        );
    }

    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient name is required!",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return false;
        }
        return true;
    }

    // Main method to run the form
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                PatientManagementForm form = new PatientManagementForm();
                form.setVisible(true);
            }
        });
    }
}