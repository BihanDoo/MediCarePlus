package view;

import javax.swing.*;

public class Manage_Doctors extends JFrame {
    private JPanel ManageDoctorsPane;

    Manage_Doctors(){
        setContentPane(ManageDoctorsPane);
        setTitle("Manage Doctors");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static void main() {

    }
}
