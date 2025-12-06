package view;

import javax.swing.*;

public class TrackAppointment extends JFrame {
    private JPanel trackappointmentPane;

    TrackAppointment(){
        setContentPane(trackappointmentPane);
        setTitle("Track Appointment");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static void main() {

    }
}
