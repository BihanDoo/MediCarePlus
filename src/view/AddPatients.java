package view;

import javax.swing.*;

public class AddPatients extends JFrame{
    private JComboBox comboBox1;
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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    static void main() {
        new AddPatients();
    }
}
