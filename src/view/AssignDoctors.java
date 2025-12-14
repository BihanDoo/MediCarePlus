package view;

import javax.swing.*;
import java.time.LocalDate;
import DataBase.database;

public class AssignDoctors extends JFrame {
    private JPanel assignDocPane;
    private JList AvailableList;
    private JTextField textField1;
    private JComboBox specialityComboBox;
    private JComboBox comboboxDD;
    private JComboBox comboboxMM;
    private JComboBox comboboxYYYY;
    private JTextArea reasonTextInput;
    private JButton scheduleButton;
    private JComboBox timeSlots;
    private JComboBox selectedDocComboBox;


    private database DB;

    public AssignDoctors(){
        setTitle("Assign Doctors");
        setContentPane(assignDocPane);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        initDateComboBoxes();
        initSpecialtyComboBox();
        initTimeSlots();
        DB = new database();


        // Populate AvailableList when specialty is selected
        specialityComboBox.addActionListener(e -> {
            String selectedSpecialty = (String) specialityComboBox.getSelectedItem();
            if (selectedSpecialty != null) {
                loadDoctorsBySpecialty(selectedSpecialty);
            }
        });



        scheduleButton.addActionListener(e -> assignDoctorFromUI());

        setVisible(true);
        // Load the first specialty by default
        if (specialityComboBox.getItemCount() > 0) {
            loadDoctorsBySpecialty((String) specialityComboBox.getItemAt(0));
        }


        // When a doctor in the list is clicked/selected
        AvailableList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Object selected = AvailableList.getSelectedValue();
                if (selected != null && !selected.toString().contains("No doctors") &&
                        !selected.toString().startsWith("Error")) {

                    // Update selectedDocComboBox
                    selectedDocComboBox.removeAllItems();
                    selectedDocComboBox.addItem(selected.toString());
                    selectedDocComboBox.setSelectedIndex(0);

                    // Extract doctor ID from the selected string
                    String doctorId = selected.toString().contains("(ID:") ?
                            selected.toString().split("\\(ID:")[1].split("\\)")[0].trim() : null;

                    if (doctorId != null) {
                        loadAvailableTimeSlots(doctorId); // <-- load slots dynamically
                    }
                }
            }
        });





    }

    private void initDateComboBoxes() {
        comboboxDD.removeAllItems();
        for (int d = 1; d <= 30; d++) comboboxDD.addItem(d);

        comboboxMM.removeAllItems();
        for (int m = 1; m <= 12; m++) comboboxMM.addItem(String.format("%02d", m));

        comboboxYYYY.removeAllItems();
        for (int y = 2024; y <= 2030; y++) comboboxYYYY.addItem(y);
    }


    private void loadAvailableTimeSlots(String doctorId) {
        timeSlots.removeAllItems();
        try {
            java.util.List<String> slots = DB.getAvailableTimeSlots(doctorId);

            if (slots.isEmpty()) {
                timeSlots.addItem("No available slots");
            } else {
                for (String slot : slots) {
                    timeSlots.addItem(slot);
                }
            }

        } catch (Exception ex) {
            timeSlots.addItem("Error loading slots");
            ex.printStackTrace();
        }
    }



    private void initSpecialtyComboBox() {
        specialityComboBox.removeAllItems();
        specialityComboBox.addItem("General Physician");
        specialityComboBox.addItem("Pediatrician");
        specialityComboBox.addItem("Cardiologist");
        specialityComboBox.addItem("Dermatologist");
        specialityComboBox.addItem("Orthopedic Surgeon");
        specialityComboBox.addItem("Neurologist");
        specialityComboBox.addItem("Psychiatrist");
        specialityComboBox.addItem("Gynecologist");
        specialityComboBox.addItem("ENT Specialist");
        specialityComboBox.addItem("Ophthalmologist");
        specialityComboBox.addItem("Dentist");
    }

    private void initTimeSlots() {
        timeSlots.removeAllItems();
        timeSlots.addItem("Monday 09:00-11:00");
        timeSlots.addItem("Monday 13:00-15:00");
        timeSlots.addItem("Tuesday 09:00-11:00");
        timeSlots.addItem("Wednesday 14:00-16:00");
        timeSlots.addItem("Thursday 10:00-12:00");
        timeSlots.addItem("Friday 13:00-15:00");
    }

    public LocalDate getSelectedDate() {
        int day = (int) comboboxDD.getSelectedItem();
        int month = Integer.parseInt(comboboxMM.getSelectedItem().toString());
        int year = (int) comboboxYYYY.getSelectedItem();
        return LocalDate.of(year, month, day);
    }


    private void assignDoctorFromUI() {
        try {
            String patientId = textField1.getText().trim();
            String selectedDoctor = (String) selectedDocComboBox.getSelectedItem();
            String specialty = (String) specialityComboBox.getSelectedItem(); // <-- get specialty
            LocalDate date = getSelectedDate();
            String timeSlot = (String) timeSlots.getSelectedItem();
            String reason = reasonTextInput.getText().trim();

            if (patientId.isEmpty() || selectedDoctor == null || specialty == null || timeSlot == null || reason.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields and select a doctor.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Safely parse doctor name
            String doctorName = selectedDoctor.contains("(ID:") ?
                    selectedDoctor.split(" \\(ID:")[0].trim() : selectedDoctor;

            // Call DB and catch possible runtime errors
            try {
                DB.assignDoctor(patientId, specialty, date, timeSlot, reason); // <-- pass specialty
                JOptionPane.showMessageDialog(this,
                        "Doctor assigned successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception dbEx) {
                JOptionPane.showMessageDialog(this,
                        "Failed to assign doctor: " + dbEx.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                dbEx.printStackTrace();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unexpected error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }





    private void loadDoctorsBySpecialty(String specialty) {
        DefaultListModel<String> model = new DefaultListModel<>();
        try {
            java.util.List<org.bson.Document> doctors = DB.getDoctorsBySpecialty(specialty);

            if (doctors.isEmpty()) {
                model.addElement("No doctors found for this specialty.");
            } else {
                for (org.bson.Document d : doctors) {
                    boolean available = d.getBoolean("available", false);
                    String info = d.getString("name") + " (ID: " + d.getString("doctorId") + ")"
                            + " - Available: " + available;
                    model.addElement(info);
                }
            }

        } catch (Exception ex) {
            model.addElement("Error loading doctors: " + ex.getMessage());
            ex.printStackTrace();
        }

        AvailableList.setModel(model);

        // Clear selectedDocComboBox when specialty changes
        selectedDocComboBox.removeAllItems();
    }







    static void main() {
        SwingUtilities.invokeLater(AssignDoctors::new);
        //new AssignDoctors();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
