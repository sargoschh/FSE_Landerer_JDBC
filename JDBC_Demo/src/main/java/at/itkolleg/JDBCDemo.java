package at.itkolleg;

import java.sql.*;

public class JDBCDemo {
    public static void main(String[] args) {
        System.out.println("JDBC Demo!");
        System.out.println();
        selectAllDemo();
        System.out.println();
        /*insertStudentDemo("Sarah Gosch", "sargosch@myimst.at");
        insertStudentDemo("Romana Gosch", "romgosch@myimst.at");
        insertStudentDemo("Daniela Hammerle", "danhammerle@myimst.at");
        insertStudentDemo("Günter Gosch", "guegosch@myimst.at");
        insertStudentDemo("Marcel Schranz", "marschranz@myimst.at");*/
        insertStudentDemo("Patrick Bayr", "patbayr@myimst.at");
        System.out.println();
        selectAllDemo();
        System.out.println();
        updateStudentDemo(3, "Daniela Gosch", "dangosch@myimst.at");
        System.out.println();
        selectAllDemo();
        System.out.println();
        deleteStudentDemo(7);
        System.out.println();
        selectAllDemo();
        findAllByNameLike("ma");
        System.out.println();
        createNewTable();
        System.out.println();
        insertKursDemo("POS", 15);
        System.out.println();
        selectAllFromKursDemo();

    }

    public static void createNewTable() {
        System.out.println("Create new Table Demo mit JDBC");
        String createNewTable = "CREATE TABLE `jdbcdemo`.`kurs` ( `id` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(200) NOT NULL , `maxstudents` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";
        try (Connection con = DriverManager.getConnection(connectionUrl, user, pwd)){
            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement preparedStatement = con.prepareStatement(createNewTable);
            try {
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Anzahl der erstellten Tabellen: " + affectedRows);
            } catch (SQLException ex) {
                System.out.println("Fehler beim Erstellen der Tabelle: \n" + ex.getMessage());
            }


        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
    }

    public static void insertKursDemo(String name, int maxStudents) {
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
                    "INSERT INTO `kurs` (`id`, `name`, `maxstudents`) VALUES (NULL, ?, ?)");
            //reagiert auf Absetzen des Statements
            try {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, maxStudents);
                int rowAffected = preparedStatement.executeUpdate();
                System.out.println(rowAffected + " Datensätze eingefügt");
            }catch (SQLException ex) {
                System.out.println("Fehler im SQL-INSERT Statement: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
    }

    public static void selectAllFromKursDemo() {
        System.out.println("Select Demo mit JDBC");
        String sqlSelectAllPersons = "SELECT * FROM `kurs`";
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
                int maxstudents = rs.getInt("maxstudents");
                System.out.println("Kurs aus der DB: [ID] " + id + " [NAME] " + name + " [MAXSTUDENTS] " + maxstudents);
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
    }

    public static void findAllByNameLike(String findName) {
        System.out.println("FindByName Demo mit JDBC");
        String sqlSelectAllPersons = "SELECT * FROM `student` WHERE `student`.`name` LIKE ?";
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";
        try (Connection con = DriverManager.getConnection(connectionUrl, user, pwd)){
            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement preparedStatement = con.prepareStatement(sqlSelectAllPersons);
            preparedStatement.setString(1, "%" + findName + "%");
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

    public static void deleteStudentDemo(int studentId) {
        System.out.println("Delete Demo mit JDBC");
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";

        try (Connection con = DriverManager.getConnection(connectionUrl, user, pwd)){
            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement preparedStatement = con.prepareStatement(
                    "DELETE FROM `student` WHERE `student`.`id` = ?");

            try {
                preparedStatement.setInt(1, studentId);
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Anzahl der gelöschten Datensätze: " + affectedRows);
            }catch (SQLException ex) {
                System.out.println("Fehler im SQL-Delete Statement: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
    }

    public static void updateStudentDemo(int studentId, String neuerName, String neueEmail) {
        System.out.println("Update Demo mit JDBC");
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";

        try (Connection con = DriverManager.getConnection(connectionUrl, user, pwd)){
            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement preparedStatement = con.prepareStatement(
                    "UPDATE `student` SET `name` = ?, `email` = ? WHERE `student`.`id` = ?");
            //reagiert auf Absetzen des Statements
            try {
                preparedStatement.setString(1, neuerName);
                preparedStatement.setString(2, neueEmail);
                preparedStatement.setInt(3, studentId);
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Anzahl der aktualisierten Datensätze: " + affectedRows);
            }catch (SQLException ex) {
                System.out.println("Fehler im SQL-Update Statement: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
    }

    public static void insertStudentDemo(String name, String email) {
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
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
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
