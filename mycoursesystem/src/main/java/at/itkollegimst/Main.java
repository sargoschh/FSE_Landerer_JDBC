package at.itkollegimst;

import dataaccess.MySqlCourseRepository;
import dataaccess.MysqlDatabaseConnection;
import ui.CLI;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {


        try {
            CLI myCli = new CLI(new MySqlCourseRepository());
            myCli.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: " + e.getMessage() + " SQL State: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbankfehler: " + e.getMessage());
        }


    }
}