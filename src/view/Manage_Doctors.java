package view;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;


public class Manage_Doctors extends JFrame {
    private JPanel ManageDoctorsPane;

    Manage_Doctors(){
        setContentPane(ManageDoctorsPane);
        setTitle("Manage Doctors");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(600,550);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    static void main() {
        String url = "jdbc:mysql://localhost:3306;databaseName=YourDB";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 1")) {
            System.out.println("Connected Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
