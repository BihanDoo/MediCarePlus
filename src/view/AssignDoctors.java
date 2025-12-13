package view;

import javax.swing.*;

public class AssignDoctors extends JFrame {
    private JPanel assignDocPane;

    public AssignDoctors(){
        setTitle("Assign Doctors");
        setContentPane(assignDocPane);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    static void main() {
        new AssignDoctors();
    }
}
