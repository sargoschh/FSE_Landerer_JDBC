package ui;

import dataaccess.DatabaseException;
import dataaccess.MyBookingRepository;
import domain.Booking;

import java.util.List;
import java.util.Scanner;

public class CLIbooking {

    Scanner scan;
    MyBookingRepository repo;

    public CLIbooking(MyBookingRepository repo) {
        this.scan = new Scanner(System.in);
        this.repo = repo;
    }

    public void start() {
        String input = "-";
        while(!input.equalsIgnoreCase("x")) {

            showMenue();
            input = this.scan.nextLine();
            switch (input) {
                case "1" -> addBookings();
                case "2" -> showAllBookings();

                case "x" -> System.out.println("");
                default -> inputError();
            }
        }
    }

    private void addBookings() {
    }

    private void showAllBookings() {
        List<Booking> list = null;

        try {
            list = repo.getAll();
            if(list.size() > 0) {
                for (Booking b : list) {
                    System.out.println(b);
                }
            } else {
                System.out.println("Keine Buchungen vorhanden!");
            }
        } catch (DatabaseException e) {
            System.out.println("Datenbankfehler bei Anzeige aller Buchungen: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei Anzeige aller Buchungen: " + e.getMessage());
        }
    }

    private void showMenue() {

        System.out.println("------------------- BUCHUNGSMANAGEMENT ---------------------");
        System.out.println("\t(1) Kurs buchen\n\t(2) Alle Buchungen anzeigen\n\t(3) Studentendetails anzeigen\n\t" +
                "(4) Studentendetails ändern\n\t(5) Student löschen\n\t(6) Studentensuche\n\t(x) Beenden");
    }

    private void showSearchMenue() {
        System.out.println("\t(1) Suche mit Vorname\n\t(2) Suche mit Nachname\n\t" +
                "(3) Suche mit Name\n\t(4) Suche mit Jahreszahl\n\t" +
                "(5) Suche mit Geburtsdatum zwischen zwei Daten\n\t(x) Zurück zum Management");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menueauswahl eingeben!");
    }
}
