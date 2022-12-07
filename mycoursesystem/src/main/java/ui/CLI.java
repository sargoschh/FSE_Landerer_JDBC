package ui;

import java.util.Scanner;

public class CLI {

    Scanner scan;
    CLIcourse course;
    CLIstudent student;
    CLIbooking booking;
    public CLI(CLIcourse course, CLIstudent student, CLIbooking booking) {
        this.scan = new Scanner(System.in);
        this.course = course;
        this.student = student;
        this.booking = booking;
    }

    public void start() {
        String input = "-";
        while(!input.equalsIgnoreCase("x")) {

            showMenue();
            input = this.scan.nextLine();
            switch (input) {
                case "1" -> startCourse();
                case "2" -> startStudent();
                case "3" -> startBooking();
                case "x" -> System.out.println("Auf Wiedersehen!");
                default -> inputError();
            }
        }
        this.scan.close();
    }

    private void startBooking() {
        this.booking.start();
    }

    private void startCourse() {
        this.course.start();
    }

    private void startStudent() {
        this.student.start();
    }

    private void showMenue() {
        System.out.println("------------------------ MANAGEMENT ------------------------");
        System.out.println("\t(1) Kurstabelle bearbeiten\n\t(2) Studententabelle bearbeiten" +
                "\n\t(3) Buchungen bearbeiten\n\t(x) Beenden");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menueauswahl eingeben!");
    }

}
