package view;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DataBase.database;



public class Manage_Doctors extends JFrame {
    private JLabel lblname;
    private JTextField txtname,txtavailable,txtspecialty;
    private JLabel lblspecialty ,lblavailable,lbltimeslots;
    private JComboBox timesslotscombobox;
    private JPanel ManageDoctorsPane;
    private database DB;

    Manage_Doctors(){
        setContentPane(ManageDoctorsPane);
        setTitle("Manage Doctors");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    static void main() {
        new Manage_Doctors();
        database DBDB = new database();
        DBDB.addPatient( "Sunil Perera", 35, "Male", "Fever");
    }
}
