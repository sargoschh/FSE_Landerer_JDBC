package at.itkolleg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCDemo {
    public static void main(String[] args) {
        System.out.println("JDBC Demo!");
        //INSERT INTO `student` (`id`, `name`, `email`) VALUES (NULL, 'Sarah Gosch', 'sargosch@myimst.at'), (NULL, 'Marcel Schranz', 'marschranz@myimst.at');

        selectAllDemo();

    }

    public static void selectAllDemo() {
        System.out.println("Select Demo mit JDBC");
        String sqlSelectAllPersons = "SELECT * FROM `student`";
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        try (Connection con = DriverManager.getConnection(connectionUrl, "root", "")){
            System.out.println("Verbindung zur DB hergestellt!");
        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: " + e.getMessage());
        }
    }
}
