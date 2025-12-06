package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddPatients extends JFrame{
    public JComboBox comboBox1;
    private JPanel AddPatientsPane;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton cancelButton;
    private JButton addButton;

    AddPatients(){
        setContentPane(AddPatientsPane);
        setTitle("Add/Update Patients");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    static void main() {
        new AddPatients();
    }


}
