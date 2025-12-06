package view;

import javax.swing.*;

public class main extends JFrame {

    private JButton assignDoctorsToPatientsButton;
    private JButton generateMonthlyReportsButton;
    private JButton sendNotificationsToPatientsButton;
    private JButton sendNotificationsToDoctorsButton;
    private JButton addButton;
    private JButton updateButton1;
    private JButton scheduleAppointmentButton;
    private JButton trackAppointmentStatusButton;
    private JPanel mainviewpane;
    private JButton removeButton;
    private JButton updateButton;
    private JButton addButton2;
    private JButton removeButton2;

    main() {
        setContentPane(mainviewpane);
        setTitle("Admin Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static void main() {
        new main();
    }
}
