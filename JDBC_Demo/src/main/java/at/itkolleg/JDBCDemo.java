package at.itkolleg;

import java.sql.*;

public class JDBCDemo {
    public static void main(String[] args) {
        System.out.println("JDBC Demo!");
        System.out.println();
        selectAllDemo();
        System.out.println();
        //insertStudentDemo();
        //System.out.println();
        //selectAllDemo();
        updateStudentDemo();
        System.out.println();
        selectAllDemo();

    }

    public static void updateStudentDemo() {
        System.out.println("Update Demo mit JDBC");
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";

        try (Connection con = DriverManager.getConnection(connectionUrl, user, pwd)){
            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement preparedStatement = con.prepareStatement(
                    "UPDATE `student` SET `name` = ?, `email` = ? WHERE `student`.`id` = 5");
            //reagiert auf Absetzen des Statements
            try {
                preparedStatement.setString(1, "Jutta Hammerle");
                preparedStatement.setString(2, "juthammerle@myimst.at");
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Anzahl der aktualisierten Datensätze: " + affectedRows);
            }catch (SQLException ex) {
                System.out.println("Fehler im SQL-Update Statement: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
    }

    public static void insertStudentDemo() {
        System.out.println("Insert Demo mit JDBC");
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";
        /*
         * reagiert auf Verbindung mit Datenbank
         *
         * Schreibt man die Verbindung in den Head des try-Blocks,
         * so kann man auf con.close verzichten, da die Verbindung
         * automatisch geschlossen wird, sobald der try-Block abgearbeitet
         * ist.
         */
        try (Connection con = DriverManager.getConnection(connectionUrl, user, pwd)){
            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO `student` (`id`, `name`, `email`) VALUES (NULL, ?, ?)");
            //reagiert auf Absetzen des Statements
            try {
                preparedStatement.setString(1, "Romana Gosch");
                preparedStatement.setString(2, "romgosch@myimst.at");
                int rowAffected = preparedStatement.executeUpdate();
                System.out.println(rowAffected + " Datensätze eingefügt");
            }catch (SQLException ex) {
                System.out.println("Fehler im SQL-INSERT Statement: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
    }

    public static void selectAllDemo() {
        System.out.println("Select Demo mit JDBC");
        String sqlSelectAllPersons = "SELECT * FROM `student`";
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";
        try (Connection con = DriverManager.getConnection(connectionUrl, user, pwd)){
            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement preparedStatement = con.prepareStatement(sqlSelectAllPersons);
            ResultSet rs =  preparedStatement.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("Student aus der DB: [ID] " + id + " [NAME] " + name + " [EMAIL] " + email);
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
    }
}
