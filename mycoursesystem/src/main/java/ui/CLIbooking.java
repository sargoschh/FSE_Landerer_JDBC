package ui;

import dataaccess.DatabaseException;
import dataaccess.EntitysNotFoundException;
import dataaccess.MySqlBookingRepository;
import domain.Booking;
import domain.InvalidValueException;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLIbooking {

    Scanner scan;
    MySqlBookingRepository repo;

    public CLIbooking(MySqlBookingRepository repo) {
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
                case "3" -> showBookingDetails();
                case "4" -> updateBookingDetails();
                case "5" -> deleteBooking();
                case "6" -> search();
                case "x" -> System.out.println("");
                default -> inputError();
            }
        }
    }

    private void search() {
        String input = "-";
        while(!input.equalsIgnoreCase("x")) {

            showSearchMenue();
            input = this.scan.nextLine();
            switch (input) {
                case "1" -> bookingSearchByStudentId();
                case "2" -> bookingSearchByCourseId();
                case "3" -> bookingSearchByBookingDate();
                case "4" -> bookingDateBySpecialDate();
                case "5" -> bookingSearchByApproval();
                case "x" -> System.out.println("");
                default -> inputError();
            }
        }

    }

    private void bookingSearchByStudentId() {
        try {
            System.out.println("Geben Sie die gewünschte Studenten-ID an: ");
            Long studentId = Long.parseLong(scan.nextLine());
            List<Booking> bookingList;
            bookingList = repo.findAllBookingsByStudentId(studentId);
            for(Booking b : bookingList) {
                System.out.println(b);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Buchungssuche mit Studenten-ID: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Buchungssuche mit Studenten-ID: " + e.getMessage());
        }
    }

    private void bookingSearchByCourseId() {
        try {
            System.out.println("Geben Sie die gewünschte Kurs-ID an: ");
            Long courseId = Long.parseLong(scan.nextLine());
            List<Booking> bookingList;
            bookingList = repo.findAllBookingsByCourseId(courseId);
            for(Booking b : bookingList) {
                System.out.println(b);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Buchungssuche mit Kurs-ID: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Buchungssuche mit Kurs-ID: " + e.getMessage());
        }
    }

    private void bookingSearchByBookingDate() {
        try {
            System.out.println("Geben Sie das gewünschte Buchungsdatum an (YYYY-MM-DD): ");
            String bookingDate = scan.nextLine();
            List<Booking> bookingList;
            bookingList = repo.findAllBookingsByBookingDate(Date.valueOf(bookingDate));
            for(Booking b : bookingList) {
                System.out.println(b);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Buchungssuche nach dem Buchungsdatum: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Buchungssuche nach dem Buchungsdatum: " + e.getMessage());
        }
    }

    private void bookingDateBySpecialDate() {
        try {
            System.out.println("Geben Sie das gewünschte Datum an (YYYY-MM-DD): ");
            String date = scan.nextLine();
            List<Booking> bookingList;
            bookingList = repo.findAllBookingsBeforeDate(Date.valueOf(date));
            for(Booking b : bookingList) {
                System.out.println(b);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Buchungssuche bevor einem bestimmten Datum: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Buchungssuche bevor einem bestimmten Datum: " + e.getMessage());
        }
    }

    private void bookingSearchByApproval() {
        try {
            System.out.println("Möchten Sie die bestätigten oder die nicht-bestätigten Buchungen ausgeben? " +
                    "\nGeben Sie 1 für die bestätigten und 0 für die nicht-bestätigten Buchungen ein: ");
            int i = Integer.parseInt(scan.nextLine());
            List<Booking> bookingList;
            if(i==1) {
                bookingList = repo.findAllBookingsByApproval(true);
            } else if (i==0) {
                bookingList = repo.findAllBookingsByApproval(false);
            } else {
                throw new IllegalArgumentException("Eingabefehler!");
            }

            for(Booking b : bookingList) {
                System.out.println(b);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Buchungssuche nach Bestätigungsstatus: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Buchungssuche nach Bestätigungsstatus: " + e.getMessage());
        }
    }

    private void deleteBooking() {
        System.out.println("Welche Buchung möchten Sie löschen? Bitte ID eingeben: ");
        try {
            Long bookingId = Long.parseLong(scan.nextLine());
            repo.deleteById(bookingId);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }
    }

    private void updateBookingDetails() {
        System.out.println("Für welche Buchungs-ID möchten Sie Änderungen vornehmen?");

        try {
            Long buchungId = Long.parseLong(scan.nextLine());
            String s = "-";
            Optional<Booking> bookingOptional = repo.getById(buchungId);
            if(bookingOptional.isEmpty()) {
                System.out.println("Keine Buchung mit der ID " + buchungId + " vorhanden!");
            } else {
                Booking booking = bookingOptional.get();
                if(booking.isApproved()) {
                    System.out.println("Der Kurs ist bereits bestätigt. " +
                            "Möchten Sie die Bestätigung wieder rückgängig machen? (j/n)");
                    s = scan.nextLine();
                    if (s.equalsIgnoreCase("j")) {
                        booking.setApproved(false);
                    } else if (s.equalsIgnoreCase("n")) {
                        booking.setApproved(true);
                    } else {
                        throw new IllegalArgumentException("Eingabefehler!");
                    }
                } else {
                    System.out.println("Der Kurs ist noch nicht bestätigt. " +
                            "Möchten Sie den Kurs nun bestätigen? (j/n)");
                    s = scan.nextLine();
                    if (s.equalsIgnoreCase("j")) {
                        booking.setApproved(true);
                    } else if (s.equalsIgnoreCase("n")) {
                        booking.setApproved(false);
                    } else {
                        throw new IllegalArgumentException("Eingabefehler!");
                    }
                }

                Optional<Booking> optionalBookingUpdated = repo.update(booking);

                optionalBookingUpdated.ifPresentOrElse(
                        (c) -> System.out.println("Buchung wurde aktualisiert!"),
                        () -> System.out.println("Buchung konnte nicht aktualisiert werden!")
                );
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + e.getMessage());
        }
    }

    private void showBookingDetails() {
        System.out.println("Für welche Buchung möchten Sie die Buchungsdetails anzeigen? (Geben Sie die ID ein)");
        Long id = Long.parseLong(scan.nextLine());
        try {
            Optional<Booking> bookingOptional = repo.getById(id);
            if(bookingOptional.isPresent()) {
                System.out.println(bookingOptional.get());
            } else {
                System.out.println("Es ist keine Buchung für die Buchungs-ID " + id + " vorhanden!");
            }
        } catch (DatabaseException e) {
            System.out.println("Datenbankfehler bei Buchungen-Detailanzeige: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei Buchungen-Detailanzeige: " + e.getMessage());
        }
    }

    private void addBookings() {
        Long id_course, id_student;

        try {
            System.out.println("Bitte alle Buchungsdetails angeben: ");

            System.out.println("Kurs-ID: ");
            id_course = Long.parseLong(scan.nextLine());
            if(repo.getCourseRepo().countCoursesInDbWithId(id_course)==0)
                throw new EntitysNotFoundException("Kurs mit der ID " + id_course + " nicht verfügbar!");

            System.out.println("Studenten-ID: ");
            id_student = Long.parseLong(scan.nextLine());
            if(repo.getStudentRepo().countStudentsInDbWithId(id_student)==0)
                throw new EntitysNotFoundException("Student mit der ID " + id_student + " nicht verfügbar!");

            Date currentDate = new Date(System.currentTimeMillis());

            Optional<Booking> bookingOptional = repo.insert(
                    new Booking(repo.getCourseRepo().getById(id_course).get(),
                            repo.getStudentRepo().getById(id_student).get(),
                            currentDate,
                            false)
            );

            if(bookingOptional.isPresent()) {
                System.out.println("Buchung erstellt: " + bookingOptional.get());
            } else {
                System.out.println("Buchung konnte nicht erstellt werden!");
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Buchungsdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
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
        System.out.println("\t(1) Kurs buchen\n\t(2) Alle Buchungen anzeigen\n\t(3) Buchungsdetails anzeigen\n\t" +
                "(4) Buchungsstatus ändern\n\t(5) Buchung löschen\n\t(6) Buchungssuche\n\t(x) Beenden");
    }

    private void showSearchMenue() {
        System.out.println("\t(1) Suche mit Studenten-ID\n\t(2) Suche mit Kurs-ID\n\t" +
                "(3) Suche mit Buchungsdatum\n\t(4) Suche Buchungen vor bestimmtem Datum\n\t" +
                "(5) Suche mit Bestätigungsstatus\n\t(x) Zurück zum Management");

    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menueauswahl eingeben!");
    }
}
