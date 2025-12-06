package view;

import javax.swing.*;

public class SendNotifications extends JFrame {
    private JPanel SendNotificationsPane;

    SendNotifications(){
        setContentPane(SendNotificationsPane);
        setTitle("Send Notifications");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static void main() {

    }
}
