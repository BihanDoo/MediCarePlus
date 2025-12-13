package view;

import DataBase.database;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class Manage_Doctors extends JFrame {
    private JLabel lblname;
    private JTextField txtname,txtspecialty;
    private JLabel lblspecialty ,lblavailable,lbltimeslots;
    private JPanel ManageDoctorsPane;
    private JList list1;
    private JButton btnadd;
    private JComboBox comboxavailable;
    private JCheckBox monday090011CheckBox;
    private JCheckBox monday110013CheckBox;
    private JCheckBox monday140016CheckBox;
    private JCheckBox tuesday090011CheckBox;
    private JCheckBox tuesday110013CheckBox;
    private JCheckBox tuesday140016CheckBox;
    private JCheckBox wednesday090011CheckBox;
    private JCheckBox wednesday110013CheckBox;
    private JCheckBox wednesday140016CheckBox;
    private JCheckBox thursday090011CheckBox;
    private JCheckBox thursday110013CheckBox;
    private JCheckBox thursday140016CheckBox;
    private JCheckBox friday090011CheckBox;
    private JCheckBox friday090011CheckBox1;
    private JCheckBox friday140016CheckBox;
    private database DB;

    Manage_Doctors(){
        setContentPane(ManageDoctorsPane);
        setTitle("Manage Doctors");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);

        btnadd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = (String) comboxavailable.getSelectedItem();
                DB.addDoctor(
                        txtname.getText(),
                        txtspecialty.getText(),
                        Boolean.parseBoolean(selectedValue),
                        List.of()
                );

            }
        });
    }



    static void main() {
        database DB = new database();
        new Manage_Doctors();


    }
}
