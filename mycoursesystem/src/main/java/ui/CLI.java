package ui;

import java.util.Scanner;

public class CLI {

    Scanner scan;
    CLIcourse course;
    CLIstudent student;
    public CLI(CLIcourse course, CLIstudent student) {
        this.scan = new Scanner(System.in);
        this.course = course;
        this.student = student;
    }

    public void start() {
        String input = "-";
        while(!input.equalsIgnoreCase("x")) {

            showMenue();
            input = this.scan.nextLine();
            switch (input) {
                case "1" -> startCourse();
                case "2" -> startStudent();
                case "x" -> System.out.println("Auf Wiedersehen!");
                default -> inputError();
            }
        }
        this.scan.close();
    }

    private void startCourse() {
        this.course.start();
    }

    private void startStudent() {
        System.out.println("Student");
    }

    private void showMenue() {
        System.out.println("------------------------ MANAGEMENT ------------------------");
        System.out.println("\t(1) Kurstabelle bearbeiten\n\t(2) Studententabelle bearbeiten\n\t(x) Beenden");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menueauswahl eingeben!");
    }

}
