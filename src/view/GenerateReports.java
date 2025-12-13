package view;

import javax.swing.*;
import DataBase.database;
import org.bson.Document;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class GenerateReports extends JFrame {
    private JPanel GenerateReportsPane;
    private JTextArea textArea;
    private JButton generateBtn;

    GenerateReports(){
        setContentPane(GenerateReportsPane);
        setTitle("Generate Reports");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
        generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Document report = database.generateReportThisMonth();

                if (report != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Month: ").append(report.getString("month")).append(" ")
                            .append(report.getInteger("year")).append("\n");
                    sb.append("Total Appointments    : ").append(report.getInteger("totalAppointments")).append("\n");
                    sb.append("Completed Appointments: ").append(report.getInteger("completedAppointments")).append("\n");
                    sb.append("Best Doctor           : ").append(report.getString("bestDoctorName")).append("\n\n");


                    sb.append("Doctor Performance:\n");
                    Map<String, Integer> perf = (Map<String, Integer>) report.get("doctorPerformance");
                    for (Map.Entry<String, Integer> entry : perf.entrySet()) {
                        sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append(" completed\n");
                    }

                    textArea.setText(sb.toString());
                } else {
                    textArea.setText("No report available for this month.");
                }

            }
        });
    }

    static void main() {
    new GenerateReports();
    }
}
