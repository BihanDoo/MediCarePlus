package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class main extends JFrame {

    private JButton AssignDocBtn;
    private JButton generateMonthlyReportsButton;
    private JButton sendNotificPatBtn;
    private JButton sendNotificDocBtn;
    private JButton DocUpdtBtn;
    private JButton scheduleAppointmentButton;
    private JButton trackAppointmentStatusButton;
    private JPanel mainviewpane;

    private JButton patientsManagementButton;
    private JButton doctorsManagementButton;

    main() {
        setContentPane(mainviewpane);
        setTitle("Admin Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
        JButton patientButton = new JButton("Patients Management");
        patientButton.addActionListener(e -> {
            PatientManagementForm patientForm = new PatientManagementForm();
            patientForm.setVisible(true);
        });




        trackAppointmentStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TrackAppointment().setVisible(true);
            }
        });
        AssignDocBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AssignDoctors().setVisible(true);
            }
        });
        generateMonthlyReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GenerateReports().setVisible(true);
            }
        });
        sendNotificDocBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SendNotificationsToPatients().setVisible(true);
            }
        });
        patientsManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PatientManagementForm().setVisible(true);

            }
        });
        doctorsManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DoctorManagementForm().setVisible(true);
            }
        });
        sendNotificPatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PatientNotificationSystem().setVisible(true);
            }
        });
        scheduleAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new scheduleStatusGUI().setVisible(true);
            }
        });
    }

    static void main() {
        new main();
    }
}
