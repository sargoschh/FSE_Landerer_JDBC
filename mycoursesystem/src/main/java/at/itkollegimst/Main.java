package at.itkollegimst;

import dataaccess.MySqlBookingRepository;
import dataaccess.MySqlCourseRepository;
import dataaccess.MySqlStudentRepository;
import ui.CLI;
import ui.CLIbooking;
import ui.CLIcourse;
import ui.CLIstudent;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {


        try {
            CLIcourse course = new CLIcourse(new MySqlCourseRepository());
            CLIstudent student = new CLIstudent(new MySqlStudentRepository());
            CLIbooking booking = new CLIbooking(new MySqlBookingRepository());
            CLI myCli = new CLI(course, student, booking);
            myCli.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: " + e.getMessage() + " SQL State: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbankfehler: " + e.getMessage());
        }


    }
}