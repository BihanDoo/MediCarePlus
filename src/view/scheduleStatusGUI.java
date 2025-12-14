package view;

import DataBase.database;
import org.bson.Document;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
/*

can you pls update and refine the code? FYI, here is an example of the use of the method used here



database DB = new database();
DB.scheduleAppointment(
                "P001",
                "D001",
                LocalDate.now(),          // today
                "Monday 09:00-11:00",
                "General checkup"
        );


and another method inside the DB, which looks like this
public List<Document> getAllAppointments() {

        MongoCollection<Document> collection = getAppointmentCollection();

        List<Document> appointments = new ArrayList<>();

        for (Document doc : collection.find()) {
            appointments.add(doc);
        }

        return appointments;
    }

update the code to best use of the following into the code below. show me where to change
* */





public class scheduleStatusGUI {
    private JTable table;
    private JComboBox comboBox1;
    private JButton button1;
    private JPanel backpanel;
    private database db;

    public scheduleStatusGUI() {
        setTableData();


        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        String currentStatus = (String) table.getValueAt(selectedRow, 5); // Column 5 is Status
                        comboBox1.setSelectedItem(currentStatus);
                    }
                }
            }
        });







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

                    // After refresh, reselect the same row if possible
                    if (selectedRow < table.getRowCount()) {
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update appointment status.");
                }
            }
        });
    }




    public static void main(String[] args) {
        JFrame frame = new JFrame("Appointment Status Manager"); // Better title
        frame.setContentPane(new scheduleStatusGUI().backpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Larger size for better visibility
        frame.setLocationRelativeTo(null); // Center the window on screen
        frame.setVisible(true);
    }

    public void setTableData() {
        try {
            db = new database();
            java.util.List<Document> appointments = db.getAllAppointments();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            // Add new rows with null safety
            for (Document doc : appointments) {
                model.addRow(new Object[]{
                        doc.getString("appointmentId") != null ? doc.getString("appointmentId") : "N/A",
                        doc.getString("patientName") != null ? doc.getString("patientName") : "N/A",
                        doc.getString("doctorName") != null ? doc.getString("doctorName") : "N/A",
                        doc.getString("timeSlot") != null ? doc.getString("timeSlot") : "N/A",
                        doc.getString("reason") != null ? doc.getString("reason") : "N/A",
                        doc.getString("status") != null ? doc.getString("status") : "Pending"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error loading appointments: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createUIComponents() {
        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object[][]{ },
                new String[]{
                        "Appointment ID", "Patient Name", "Doctor Name", "Time Slot", "Reason", "Status"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        });



        // Initialize comboBox with possible status values
        comboBox1 = new JComboBox<>(new String[]{
                "Scheduled", "Confirmed", "Completed", "Cancelled", "No-show", "Pending"
        });
        // Optional: Improve table appearance
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }





}
