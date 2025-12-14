package view;

import DataBase.database;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class scheduleStatusGUI {
    private JTable table;
    private JComboBox comboBox1;
    private JButton button1;
    private JPanel backpanel;
    private database db;

    public scheduleStatusGUI() {
        setTableData();

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStatus = (String) comboBox1.getSelectedItem();
                int selectedRow = table.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an appointment to update.");
                    return;
                }

                String appointmentId = (String) table.getValueAt(selectedRow, 0);

                db = new database();
                boolean isUpdated = database.updateAppointment(appointmentId, selectedStatus);
                if (isUpdated) {
                    JOptionPane.showMessageDialog(null, "Appointment status updated successfully.");
                    setTableData(); // Refresh table data
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update appointment status.");
                }
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("mongo");
        frame.setContentPane(new scheduleStatusGUI().backpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    public void setTableData() {
        db = new database();
        java.util.List<Document> appointments = db.getAllAppointments();

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        // Add new rows
        for (Document doc : appointments) {
            model.addRow(new Object[]{
                    doc.getString("appointmentId"),
                    doc.getString("patientName"),
                    doc.getString("doctorName"),
                    doc.getString("timeSlot"),
                    doc.getString("reason"),
                    doc.getString("status")
            });
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object[][]{ },
                new String[]{
                        "Appointment ID", "Patient Name", "Doctor Name", "Time Slot", "Reason", "Status"
                }
        ));
    }
}
