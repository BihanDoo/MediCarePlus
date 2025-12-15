package view;

import DataBase.database;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

public class MonthlyReportUI extends JFrame {

    // Constants for consistent styling
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font MONOSPACED_FONT = new Font("Monospaced", Font.PLAIN, 12);

    private JLabel monthLabel;
    private JLabel totalLabel;
    private JLabel completedLabel;
    private JLabel bestDoctorLabel;
    private JTextArea performanceArea;
    private JButton generateBtn;
    private JPanel infoPanel;

    public MonthlyReportUI() {
        setTitle("Monthly Appointment Report");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set application icon if available
        // try {
        //     ImageIcon icon = new ImageIcon(getClass().getResource("/icon.png"));
        //     setIconImage(icon.getImage());
        // } catch (Exception e) {
        //     // Icon not found, continue without it
        // }

        initUI();
        applyStyling();
    }

    private void initUI() {
        // Set background color
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center info panel
        infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // Doctor performance panel
        JPanel performancePanel = createPerformancePanel();
        mainPanel.add(performancePanel, BorderLayout.SOUTH);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel title = new JLabel("Monthly Report", JLabel.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));

        headerPanel.add(title, BorderLayout.CENTER);

        // Optional: Add subtitle
        JLabel subtitle = new JLabel("Comprehensive appointment analysis", JLabel.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitle.setForeground(Color.GRAY);
        subtitle.setBorder(new EmptyBorder(0, 0, 5, 0));
        headerPanel.add(subtitle, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        monthLabel = createStyledLabel("Month: -");
        totalLabel = createStyledLabel("Total Appointments: -");
        completedLabel = createStyledLabel("Completed Appointments: -");
        bestDoctorLabel = createStyledLabel("Best Doctor: -");

        panel.add(monthLabel);
        panel.add(totalLabel);
        panel.add(completedLabel);
        panel.add(bestDoctorLabel);

        return panel;
    }

    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(BACKGROUND_COLOR);

        performanceArea = new JTextArea(8, 30);
        performanceArea.setEditable(false);
        performanceArea.setFont(MONOSPACED_FONT);
        performanceArea.setLineWrap(true);
        performanceArea.setWrapStyleWord(true);
        performanceArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(performanceArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                new LineBorder(PRIMARY_COLOR, 1, true),
                "Doctor Performance",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
        );
        scrollPane.setBorder(titledBorder);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Add stats label
        JLabel statsLabel = new JLabel("Statistics will appear here after generating report");
        statsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        statsLabel.setForeground(Color.GRAY);
        statsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statsLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        panel.add(statsLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);

        generateBtn = new JButton("Generate This Month's Report");
        generateBtn.addActionListener(e -> loadReport());

        // Add a refresh button for convenience
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadReport());

        // Add exit button
        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> dispose());

        panel.add(generateBtn);
        panel.add(refreshBtn);
        panel.add(exitBtn);

        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.DARK_GRAY);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        return label;
    }

    private void applyStyling() {
        // Style the generate button
        generateBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        generateBtn.setBackground(SECONDARY_COLOR);
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFocusPainted(false);
        generateBtn.setBorderPainted(false);
        generateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        generateBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                generateBtn.setBackground(PRIMARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                generateBtn.setBackground(SECONDARY_COLOR);
            }
        });
    }

    private void loadReport() {
        // Show loading indicator
        generateBtn.setEnabled(false);
        generateBtn.setText("Generating...");

        // Run database operation in background thread to keep UI responsive
        SwingWorker<Document, Void> worker = new SwingWorker<Document, Void>() {
            @Override
            protected Document doInBackground() throws Exception {
                database DB = new database();
                return DB.generateReportThisMonth();
            }

            @Override
            protected void done() {
                try {
                    Document report = get();

                    if (report == null) {
                        JOptionPane.showMessageDialog(MonthlyReportUI.this,
                                "No report data available for the current month.",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        resetLabels();
                        return;
                    }

                    updateUIWithReport(report);

                } catch (Exception e) {
                    handleError(e);
                } finally {
                    generateBtn.setEnabled(true);
                    generateBtn.setText("Generate This Month's Report");
                }
            }
        };

        worker.execute();
    }

    private void updateUIWithReport(Document report) {
        try {
            // Update month label
            String month = report.getString("month");
            Integer year = report.getInteger("year");
            monthLabel.setText(String.format("Month: %s %d", month, year));
            monthLabel.setForeground(PRIMARY_COLOR);
            monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

            // Update total appointments
            Integer total = report.getInteger("totalAppointments");
            totalLabel.setText(String.format("Total Appointments: %d", total));

            // Update completed appointments
            Integer completed = report.getInteger("completedAppointments");
            completedLabel.setText(String.format("Completed Appointments: %d", completed));

            // Calculate and display completion rate
            if (total > 0) {
                double completionRate = (completed * 100.0) / total;
                completedLabel.setText(String.format("Completed Appointments: %d (%.1f%%)",
                        completed, completionRate));

                // Color code based on completion rate
                if (completionRate >= 90) {
                    completedLabel.setForeground(new Color(46, 204, 113)); // Green
                } else if (completionRate >= 70) {
                    completedLabel.setForeground(new Color(241, 196, 15)); // Yellow
                } else {
                    completedLabel.setForeground(new Color(231, 76, 60)); // Red
                }
            }

            // Update best doctor
            String bestDoctor = report.getString("bestDoctorName");
            bestDoctorLabel.setText(String.format("Best Doctor: %s", bestDoctor));
            bestDoctorLabel.setForeground(new Color(155, 89, 182)); // Purple
            bestDoctorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

            // Update performance area
            performanceArea.setText("");
            @SuppressWarnings("unchecked")
            Map<String, Integer> perf = (Map<String, Integer>) report.get("doctorPerformance");

            if (perf != null && !perf.isEmpty()) {
                // Add header
                performanceArea.append(String.format("%-25s %-15s%n", "Doctor Name", "Completed"));
                performanceArea.append(String.format("%s%n", "-".repeat(40)));

                // Sort doctors by performance (highest first)
                perf.entrySet().stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .forEach(entry -> {
                            String line = String.format("%-25s %-15d%n",
                                    entry.getKey(), entry.getValue());
                            performanceArea.append(line);
                        });

                // Add summary
                performanceArea.append(String.format("%nTotal Doctors: %d", perf.size()));
            } else {
                performanceArea.setText("No performance data available.");
            }

            // Force repaint
            infoPanel.revalidate();
            infoPanel.repaint();

        } catch (Exception e) {
            handleError(e);
        }
    }

    private void resetLabels() {
        monthLabel.setText("Month: -");
        totalLabel.setText("Total Appointments: -");
        completedLabel.setText("Completed Appointments: -");
        bestDoctorLabel.setText("Best Doctor: -");
        performanceArea.setText("");

        // Reset colors
        monthLabel.setForeground(Color.DARK_GRAY);
        completedLabel.setForeground(Color.DARK_GRAY);
        bestDoctorLabel.setForeground(Color.DARK_GRAY);
        monthLabel.setFont(LABEL_FONT);
        bestDoctorLabel.setFont(LABEL_FONT);
    }

    private void handleError(Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error generating report: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }

        SwingUtilities.invokeLater(() -> {
            MonthlyReportUI frame = new MonthlyReportUI();
            frame.setVisible(true);
        });
    }
}