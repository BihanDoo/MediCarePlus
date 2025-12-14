package view;

import javax.swing.*;
import DataBase.database;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;



public class sendNotificationsToDoctors extends JFrame{
    private JPanel Title;
    private JLabel Doctor;
    private JComboBox DoctorComboBox;
    private JLabel NotiType;
    private JComboBox TypeComboBox;
    private JLabel Prority;
    private JComboBox PriorityComboBox;
    private JLabel Message;
    private JTextArea msgTxtArea;
    private JScrollPane Scroll;
    private JButton SendBttn;
    private JButton ClearBttn;
    private JLabel firsttxt;


    public sendNotificationsToDoctors() {

        // Bind GUI panel
        setContentPane(Title);
        setTitle("Send Notifications to Doctors");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setVisible(true);

        // Populate combo boxes
        loadDoctors();           // Temporary hardcoded doctor list
        loadNotificationTypes(); // Hardcoded notification types
        loadPriorities();        // Hardcoded priorities

        // Button actions
        SendBttn.addActionListener(e -> sendNotification());
        ClearBttn.addActionListener(e -> msgTxtArea.setText(""));
    }
    private void loadDoctors() {

        DoctorComboBox.removeAllItems();
        database DB = new database();
        List<Document> doctors = DB.getAllDoctors();

        if (doctors.isEmpty()) {
            DoctorComboBox.addItem("No doctors available");
            return;
        }

        for (Document d : doctors) {

            String name = d.getString("name");
            String doctorId = d.getString("doctorId");

            DoctorComboBox.addItem(name + " (ID: " + doctorId + ")");
        }
    }
    // Hardcoded Notification Types
    private void loadNotificationTypes() {
        TypeComboBox.removeAllItems();
        TypeComboBox.addItem("New Appointment");
        TypeComboBox.addItem("Cancellation");
        TypeComboBox.addItem("Schedule Change");
    }

    // Hardcoded Priority
    private void loadPriorities() {
        PriorityComboBox.removeAllItems();
        PriorityComboBox.addItem("Normal");
        PriorityComboBox.addItem("High");
        PriorityComboBox.addItem("Urgent");
    }
    // Send notification action
    private void sendNotification() {
        String doctor = (String) DoctorComboBox.getSelectedItem();
        String type = (String) TypeComboBox.getSelectedItem();
        String priority = (String) PriorityComboBox.getSelectedItem();
        String message = msgTxtArea.getText();

        if (message.isEmpty()) {
            // Use 'this' as parentComponent to attach dialog to the frame
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a message",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // For now, simulate sending notification
        System.out.println("Notification Sent:");
        System.out.println("Doctor: " + doctor);
        System.out.println("Type: " + type);
        System.out.println("Priority: " + priority);
        System.out.println("Message: " + message);

        JOptionPane.showMessageDialog(
                this,
                "Notification sent to " + doctor,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        msgTxtArea.setText(""); // clear message after sending
    }

    // Main method to run the form
    public static void main(String[] args) {
        SwingUtilities.invokeLater(sendNotificationsToDoctors::new);
        database DB = new database();

        List<Document> doctors = DB.getAllDoctors();

        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            for (Document d : doctors) {
                System.out.println("Doctor ID   : " + d.getString("doctorId"));
                System.out.println("Name        : " + d.getString("name"));
                System.out.println("Specialty   : " + d.getString("specialty"));
                System.out.println("Available   : " + d.getBoolean("available"));
                System.out.println("Time Slots  : " + d.get("timeSlots"));
                System.out.println("----------------------------------");
            }
        }

    }
}