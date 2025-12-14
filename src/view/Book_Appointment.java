package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import DataBase.database;

public class Book_Appointment extends JFrame {
    private JPanel bookappointmentPane;
    private JTextField patientid;
    private JTextField docid;
    private JTextField date;
    private JTextField time;
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
        patientid = new JTextField();
        docid = new JTextField();
        date = new JTextField();
        time = new JTextField();
        reason = new JTextField();
        bookButton = new JButton("Book");

        // Add components to panel
        addFormItem("Patient ID:", patientid);
        addFormItem("Doctor ID:", docid);
        addFormItem("Date (YYYY-MM-DD):", date);
        addFormItem("Time:", time);
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

        setVisible(true);
    }

    private void addFormItem(String labelText, JTextField field) {
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
            String pId = patientid.getText();
            String dId = docid.getText();
            String dateStr = date.getText();
            String timeStr = time.getText();
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

    public static void main(String[] args) {
        new Book_Appointment();
    }
}
