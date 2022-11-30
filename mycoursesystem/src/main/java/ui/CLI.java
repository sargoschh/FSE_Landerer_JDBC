package ui;

import java.util.Scanner;

public class CLI {

    Scanner scan;
    public CLI() {
        this.scan = new Scanner(System.in);
    }

    public void start() {
        String input = "-";
        while(!input.equalsIgnoreCase("x")) {

            showMenue();
            input = this.scan.nextLine();
            switch (input) {
                case "1":
                    System.out.println("Kurseingabe");
                    break;
                case "2":
                    System.out.println("Alle Kurse anzeigen");
                    break;
                case "x":
                    System.out.println("Auf Wiedersehen!");
                    break;
                default:
                    inputError();
                    break;
            }
        }
        this.scan.close();
    }

    private void showMenue() {

        System.out.println("---------------------- KURSMANAGEMENT ----------------------");
        System.out.println("\t(1) Kurs eingeben\n\t(2) Alle Kurse anzeigen\n\t(x) Beenden");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menueauswahl eingeben!");
    }


}
