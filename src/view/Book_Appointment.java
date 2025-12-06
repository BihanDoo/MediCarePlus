package view;

import javax.swing.*;

public class Book_Appointment extends JFrame {
    private JPanel bookappointmentPane;

    Book_Appointment(){
        setContentPane(bookappointmentPane);
        setTitle("Book appointment");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static void main() {

    }
}
