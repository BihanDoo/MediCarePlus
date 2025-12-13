package view;

import DataBase.database;
import org.bson.Document;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PatientManagementForm extends JFrame {

    private JTextField txtName, txtAge, txtGender, txtDisease;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnRefresh;
    private JTable patientTable;
    private DefaultTableModel tableModel;

    public PatientManagementForm() {
        setTitle("Patient Management");
        setSize(900, 500);
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
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add/Edit Patient"));
        panel.setPreferredSize(new Dimension(300, 250));

        panel.add(new JLabel("Name:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Age:"));
        txtAge = new JTextField();
        panel.add(txtAge);

        panel.add(new JLabel("Gender:"));
        txtGender = new JTextField();
        panel.add(txtGender);

        panel.add(new JLabel("Disease:"));
        txtDisease = new JTextField();
        panel.add(txtDisease);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Patients List"));

        String[] columns = {"Patient ID", "Name", "Age", "Gender", "Disease"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // When row is selected, load data into form
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && patientTable.getSelectedRow() >= 0) {
                loadSelectedPatient();
            }
        });

        JScrollPane scrollPane = new JScrollPane(patientTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAdd = new JButton("Add Patient");
        btnUpdate = new JButton("Update Patient");
        btnDelete = new JButton("Delete Patient");
        btnClear = new JButton("Clear Form");
        btnRefresh = new JButton("Refresh Table");

        // Button actions
        btnAdd.addActionListener(this::addPatient);
        btnUpdate.addActionListener(this::updatePatient);
        btnDelete.addActionListener(this::deletePatient);
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> refreshTable());

        // Colors
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(33, 150, 243));
        btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(244, 67, 54));
        btnDelete.setForeground(Color.WHITE);


        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);
        panel.add(btnRefresh);

        return panel;
    }

    // ========== CORE METHODS ==========

    private void addPatient(ActionEvent e) {
        try {
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String gender = txtGender.getText().trim();
            String disease = txtDisease.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required!");
                return;
            }

            // Call your MongoDB method
            database.addPatient(name, age, gender, disease);
            JOptionPane.showMessageDialog(this, "Patient added successfully!");

            refreshTable();
            clearForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updatePatient(ActionEvent e) {
        int row = patientTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient from the table first!");
            return;
        }

        try {
            String patientId = (String) tableModel.getValueAt(row, 0);
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String gender = txtGender.getText().trim();
            String disease = txtDisease.getText().trim();

            // Call your MongoDB method
            database.updatePatient(patientId, name, age, gender, disease);
            JOptionPane.showMessageDialog(this, "Patient updated successfully!");

            refreshTable();
            clearForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deletePatient(ActionEvent e) {
        int row = patientTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient from the table first!");
            return;
        }

        String patientId = (String) tableModel.getValueAt(row, 0);
        String patientName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete:\n" + patientName + " (ID: " + patientId + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Call your MongoDB method
            database.deletePatient(patientId);
            JOptionPane.showMessageDialog(this, "Patient deleted successfully!");

            refreshTable();
            clearForm();
        }
    }

    private void loadSelectedPatient() {
        int row = patientTable.getSelectedRow();
        if (row == -1) return;

        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtAge.setText(tableModel.getValueAt(row, 2).toString());
        txtGender.setText((String) tableModel.getValueAt(row, 3));
        txtDisease.setText((String) tableModel.getValueAt(row, 4));
    }

    private void refreshTable() {
        try {
            // Clear table
            tableModel.setRowCount(0);

            // Get all patients from MongoDB using YOUR method
            List<Document> patients = database.getAllPatients();

            // Add to table
            for (Document patient : patients) {
                Object[] row = {
                        patient.getString("patientId"),
                        patient.getString("name"),
                        patient.getInteger("age"),
                        patient.getString("gender"),
                        patient.getString("disease")
                };
                tableModel.addRow(row);
            }

            // Show count in title
            setTitle("Patient Management - " + patients.size() + " patients");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading patients: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtAge.setText("");
        txtGender.setText("");
        txtDisease.setText("");
        patientTable.clearSelection();
    }

    // Main method to test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PatientManagementForm().setVisible(true);
        });
    }
}