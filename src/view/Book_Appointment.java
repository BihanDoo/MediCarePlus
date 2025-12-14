package view;

import javax.swing.*;
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

    public Book_Appointment(){
        if (bookappointmentPane == null) {

            bookappointmentPane = new JPanel();
            bookappointmentPane.setLayout(new BoxLayout(bookappointmentPane, BoxLayout.Y_AXIS));

            patientid = new JTextField();
            docid = new JTextField();
            date = new JTextField();
            time = new JTextField();
            reason = new JTextField();
            bookButton = new JButton("Book ");

            bookappointmentPane.add(new JLabel("Patient ID:"));
            bookappointmentPane.add(patientid);
            bookappointmentPane.add(new JLabel("Doctor ID:"));
            bookappointmentPane.add(docid);
            bookappointmentPane.add(new JLabel("Date (YYYY-MM-DD):"));
            bookappointmentPane.add(date);
            bookappointmentPane.add(new JLabel("Time: "));
            bookappointmentPane.add(time);
            bookappointmentPane.add(new JLabel("Reason:"));
            bookappointmentPane.add(reason);
            bookappointmentPane.add(bookButton);
        }

        setContentPane(bookappointmentPane);
        setTitle("Book appointment");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });
    }

    public static void main(String[] args) {
        System.out.println("Starting Book_Appointment...");
        try {
            new Book_Appointment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
