package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class main extends JFrame {

    private JButton AssignDocBtn;
    private JButton generateMonthlyReportsButton;
    private JButton sendNotificPatBtn;
    private JButton sendNotificDocBtn;
    private JButton patientAddBtn;
    private JButton DocUpdtBtn;
    private JButton scheduleAppointmentButton;
    private JButton trackAppointmentStatusButton;
    private JPanel mainviewpane;
    private JButton patientRemoveButton;
    private JButton patientUpdtBtn;
    private JButton DocAddBtn;
    private JButton DocRemvBtn;

    main() {
        setContentPane(mainviewpane);
        setTitle("Admin Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
        patientAddBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddPatients().setVisible(true);
            }
        });
        patientUpdtBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddPatients().setVisible(true);
            }
        });
        patientRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddPatients().setVisible(true);
            }
        });
        DocAddBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AssignDoctors().setVisible(true);
            }
        });
        DocUpdtBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AssignDoctors().setVisible(true);
            }
        });
        DocRemvBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AssignDoctors().setVisible(true);
            }
        });
        scheduleAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Book_Appointment().setVisible(true);
            }
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
        sendNotificPatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SendNotifications().setVisible(true);
            }
        });
        sendNotificDocBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SendNotifications().setVisible(true);
            }
        });
    }

    static void main() {
        new main();
    }
}
