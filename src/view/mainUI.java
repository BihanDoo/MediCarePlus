package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Constructor;

public class mainUI extends JFrame {

    // Change these class names to match your real forms/panels if different
    private static final String PATIENTS_UI   = "view.PatientManagementForm";
    private static final String DOCTORS_UI    = "view.DoctorManagementForm";
    private static final String SCHEDULE_UI   = "view.Book_Appointment";
    private static final String TRACK_UI      = "view.scheduleStatusGUI";
    private static final String ASSIGN_UI     = "view.AssignDoctors";
    private static final String REPORTS_UI    = "view.GenerateReports";
    private static final String NOTIFY_PAT_UI = "view.PatientNotificationSystem";
    private static final String NOTIFY_DOC_UI = "view.DoctorNotificationSystem";

    public mainUI() {
        setTitle("Admin Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 520);
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        setContentPane(buildRoot());
    }

    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(18, 18, 18, 18));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildButtonGrid(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        return root;
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(0, 0, 14, 0));

        JLabel title = new JLabel("MediCare Plus — Admin Dashboard");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        JLabel subtitle = new JLabel("Choose a module to manage patients, doctors, appointments, reports, and notifications.");
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 13f));
        subtitle.setForeground(new Color(90, 90, 90));

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setOpaque(false);
        text.add(title);
        text.add(Box.createVerticalStrut(6));
        text.add(subtitle);

        header.add(text, BorderLayout.CENTER);

        return header;
    }

    private JComponent buildButtonGrid() {
        JPanel gridWrap = new JPanel(new GridBagLayout());
        gridWrap.setOpaque(false);

        JPanel grid = new JPanel(new GridLayout(4, 2, 12, 12));
        grid.setOpaque(false);

        JButton btnPatients = createBigButton("Patients Management", "Add / update / remove patient records");
        btnPatients.addActionListener(e -> {
            new PatientManagementForm().main();
        });

        JButton btnDoctors = createBigButton("Doctors Management", "Manage doctors and available time slots");
        btnDoctors.addActionListener(e -> openOrWarn(DOCTORS_UI, "Doctors Management"));

        JButton btnSchedule = createBigButton("Schedule Appointment", "Create new appointments for patients");
        btnSchedule.addActionListener(e -> openOrWarn(SCHEDULE_UI, "Schedule Appointment"));

        JButton btnTrack = createBigButton("Track Appointment Status", "View appointment progress and status");
        btnTrack.addActionListener(e -> openOrWarn(TRACK_UI, "Track Appointment Status"));

        JButton btnAssign = createBigButton("Assign Doctors to Patients", "Link a doctor to a patient for care");
        btnAssign.addActionListener(e -> openOrWarn(ASSIGN_UI, "Assign Doctors to Patients"));

        JButton btnReports = createBigButton("Generate Monthly Reports", "Generate summaries for admin review");
        btnReports.addActionListener(e -> openOrWarn(REPORTS_UI, "Generate Monthly Reports"));

        JButton btnNotifyPatients = createBigButton("Send Notifications to Patients", "Send reminders and updates");
        btnNotifyPatients.addActionListener(e -> openOrWarn(NOTIFY_PAT_UI, "Send Notifications to Patients"));

        JButton btnNotifyDoctors = createBigButton("Send Notifications to Doctors", "Send schedules and alerts");
        btnNotifyDoctors.addActionListener(e -> openOrWarn(NOTIFY_DOC_UI, "Send Notifications to Doctors"));

        grid.add(btnPatients);
        grid.add(btnDoctors);
        grid.add(btnSchedule);
        grid.add(btnTrack);
        grid.add(btnAssign);
        grid.add(btnReports);
        grid.add(btnNotifyPatients);
        grid.add(btnNotifyDoctors);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        gridWrap.add(grid, gbc);
        return gridWrap;
    }

    private JComponent buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(14, 0, 0, 0));
        footer.setOpaque(false);



        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> dispose());

        footer.add(exit, BorderLayout.EAST);
        return footer;
    }

    private JButton createBigButton(String title, String subtitle) {
        JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.setPreferredSize(new Dimension(320, 86));
        b.setFocusPainted(false);

        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 15f));

        JLabel s = new JLabel(subtitle);
        s.setFont(s.getFont().deriveFont(Font.PLAIN, 12f));
        s.setForeground(new Color(90, 90, 90));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setBorder(new EmptyBorder(10, 12, 10, 12));
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(t);
        text.add(Box.createVerticalStrut(4));
        text.add(s);

        b.add(text, BorderLayout.CENTER);
        return b;
    }

    /**
     * Opens an existing Swing window class if it exists.
     * If it doesn't exist yet, it shows a message (so this file still runs).
     *
     * Supported targets:
     * - JFrame (new Frame())
     * - JDialog (new Dialog(this, true/false) OR no-arg)
     * - JPanel (wrapped inside a new JFrame)
     */
    private void openOrWarn(String className, String screenTitle) {
        try {
            Class<?> clazz = Class.forName(className);
            Object obj = instantiate(clazz);

            if (obj instanceof JFrame frame) {
                frame.setLocationRelativeTo(this);
                frame.setVisible(true);
                return;
            }

            if (obj instanceof JDialog dialog) {
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
                return;
            }

            if (obj instanceof JPanel panel) {
                JFrame f = new JFrame(screenTitle);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setContentPane(panel);
                f.pack();
                f.setSize(900, 600);
                f.setLocationRelativeTo(this);
                f.setVisible(true);
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Class found, but it's not a JFrame/JDialog/JPanel:\n" + className,
                    "Cannot open screen",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Screen not found yet:\n" + className + "\n\nCreate this class or rename it in mainUI.java.",
                    "Not ready",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to open screen:\n" + className + "\n\nReason: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Object instantiate(Class<?> clazz) throws Exception {
        // Prefer a no-argument constructor if available
        try {
            Constructor<?> c = clazz.getDeclaredConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (NoSuchMethodException ignored) {
            // If it's a JDialog, try (Frame owner, boolean modal)
            if (JDialog.class.isAssignableFrom(clazz)) {
                try {
                    Constructor<?> c = clazz.getDeclaredConstructor(Frame.class, boolean.class);
                    c.setAccessible(true);
                    return c.newInstance(this, true);
                } catch (NoSuchMethodException ignored2) {
                    // fall through
                }
            }
            // Otherwise: just try the first constructor with default null/false/0 values
            Constructor<?>[] all = clazz.getDeclaredConstructors();
            if (all.length == 0) throw new IllegalStateException("No constructor found.");
            Constructor<?> c = all[0];
            c.setAccessible(true);

            Class<?>[] types = c.getParameterTypes();
            Object[] args = new Object[types.length];
            for (int i = 0; i < types.length; i++) {
                args[i] = defaultValue(types[i]);
            }
            return c.newInstance(args);
        }
    }

    private Object defaultValue(Class<?> t) {
        if (!t.isPrimitive()) return null;
        if (t == boolean.class) return false;
        if (t == byte.class) return (byte) 0;
        if (t == short.class) return (short) 0;
        if (t == int.class) return 0;
        if (t == long.class) return 0L;
        if (t == float.class) return 0f;
        if (t == double.class) return 0d;
        if (t == char.class) return '\0';
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new mainUI().setVisible(true);
        });
    }
}
