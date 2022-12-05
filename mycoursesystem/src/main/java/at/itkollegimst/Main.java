package at.itkollegimst;

import dataaccess.MySqlCourseRepository;
import dataaccess.MysqlDatabaseConnection;
import ui.CLI;
import ui.CLIcourse;
import ui.CLIstudent;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {


        try {
            CLIcourse course = new CLIcourse(new MySqlCourseRepository());
            CLIstudent student = new CLIstudent();
            CLI myCli = new CLI(course, student);
            myCli.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: " + e.getMessage() + " SQL State: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbankfehler: " + e.getMessage());
        }


    }
}