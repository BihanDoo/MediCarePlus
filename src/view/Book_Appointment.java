package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import DataBase.database;
import org.bson.Document;
import java.util.List;

public class Book_Appointment extends JFrame {
    private JPanel bookappointmentPane;
    private JComboBox<String> patientCombo;
    private JComboBox<String> doctorCombo;
    private JTextField date;
    private JComboBox<String> timeCombo; // editable so user can type a custom time if needed
    private JTextField reason;
    private JButton bookButton;

    public Book_Appointment() {
        setTitle("Book appointment");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);

        // Manually create the UI to avoid NullPointerException if .form binding fails
        bookappointmentPane = new JPanel();
        bookappointmentPane.setLayout(new BoxLayout(bookappointmentPane, BoxLayout.Y_AXIS));
        bookappointmentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(bookappointmentPane);

        // Initialize components
        patientCombo = new JComboBox<>();
        doctorCombo = new JComboBox<>();
        date = new JTextField();
        timeCombo = new JComboBox<>();
        timeCombo.setEditable(true); // allow typing if needed
        reason = new JTextField();
        // small visible row height
        patientCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        doctorCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        timeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        bookButton = new JButton("Book");

        // Add components to panel
        addFormItem("Patient ID:", patientCombo);
        addFormItem("Doctor ID:", doctorCombo);
        addFormItem("Date (YYYY-MM-DD):", date);
        addFormItem("Time:", timeCombo);
        addFormItem("Reason:", reason);

        // Add button
        bookappointmentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookappointmentPane.add(bookButton);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleAppointment();
            }
        });

        // Load patients and doctors into combos
        loadPatients();
        loadDoctors();

        // If available, select the first patient and doctor to make the form ready-to-use
        if (patientCombo.getItemCount() > 0) {
            patientCombo.setSelectedIndex(0);
        }

        if (doctorCombo.getItemCount() > 0) {
            doctorCombo.setSelectedIndex(0);
            // load time slots for the initially selected doctor
            String sel = (String) doctorCombo.getSelectedItem();
            if (sel != null) {
                loadTimeSlots(extractIdFromComboItem(sel));
            }
        }

        // When a doctor is selected, load available time slots
        doctorCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String sel = (String) doctorCombo.getSelectedItem();
                    if (sel != null) {
                        String doctorId = extractIdFromComboItem(sel);
                        loadTimeSlots(doctorId);
                    }
                }
            }
        });

        setVisible(true);
    }

    // Accept any component (JTextField, JComboBox, etc.) so we can reuse this helper
    private void addFormItem(String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        bookappointmentPane.add(label);
        bookappointmentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        bookappointmentPane.add(field);
        bookappointmentPane.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private void scheduleAppointment() {
        try {
            String patientSel = (String) patientCombo.getSelectedItem();
            String doctorSel = (String) doctorCombo.getSelectedItem();

            if (patientSel == null || doctorSel == null) {
                JOptionPane.showMessageDialog(Book_Appointment.this, "Please select both Patient and Doctor.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String pId = extractIdFromComboItem(patientSel);
            String dId = extractIdFromComboItem(doctorSel);
            String dateStr = date.getText();
            String timeStr = (timeCombo.getEditor() != null) ? (String) timeCombo.getEditor().getItem() : (String) timeCombo.getSelectedItem();
            String reasonStr = reason.getText();

            // Parse date (YYYY-MM-DD)
            LocalDate appointmentDate = LocalDate.parse(dateStr);

            // Call backend
            database.scheduleAppointment(pId, dId, appointmentDate, timeStr, reasonStr);

            // Success message
            JOptionPane.showMessageDialog(Book_Appointment.this, "Appointment Scheduled!");

        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(Book_Appointment.this, "Invalid date format. Please use YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Book_Appointment.this, "Error scheduling appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper: load patients from DB into patientCombo. Items shown as "P001 - Name" but ID extraction only returns the left part.
    private void loadPatients() {
        try {
            List<Document> patients = database.getAllPatients();
            patientCombo.removeAllItems();
            for (Document p : patients) {
                String id = p.getString("patientId");
                String name = p.getString("name");
                String item = id + (name != null ? " - " + name : "");
                patientCombo.addItem(item);
            }
        } catch (Exception ex) {
            System.out.println("Failed to load patients: " + ex.getMessage());
        }
    }

    // Helper: load doctors from DB into doctorCombo. Items shown as "D001 - Name".
    private void loadDoctors() {
        try {
            List<Document> doctors = database.getAllDoctors();
            doctorCombo.removeAllItems();
            for (Document d : doctors) {
                String id = d.getString("doctorId");
                String name = d.getString("name");
                String item = id + (name != null ? " - " + name : "");
                doctorCombo.addItem(item);
            }
        } catch (Exception ex) {
            System.out.println("Failed to load doctors: " + ex.getMessage());
        }
    }

    // Load time slots for a given doctorId into timeCombo
    private void loadTimeSlots(String doctorId) {
        try {
            timeCombo.removeAllItems();
            List<String> slots = database.getAvailableTimeSlots(doctorId);
            for (String s : slots) {
                timeCombo.addItem(s);
            }
        } catch (Exception ex) {
            System.out.println("Failed to load time slots: " + ex.getMessage());
        }
    }

    // Extract ID portion from combo item like "P001 - Name" -> "P001"
    private String extractIdFromComboItem(String comboItem) {
        if (comboItem == null) return null;
        int idx = comboItem.indexOf(" - ");
        if (idx > 0) return comboItem.substring(0, idx);
        return comboItem;
    }

    public static void main(String[] args) {
        new Book_Appointment();
    }
}
