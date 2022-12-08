package ui;

import dataaccess.DatabaseException;
import dataaccess.MyStudentRepository;
import domain.InvalidValueException;
import domain.Student;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLIstudent {

    Scanner scan;
    MyStudentRepository repo;

    public CLIstudent(MyStudentRepository repo) {
        this.scan = new Scanner(System.in);
        this.repo = repo;
    }

    public void start() {
        String input = "-";
        while(!input.equalsIgnoreCase("x")) {

            showMenue();
            input = this.scan.nextLine();
            switch (input) {
                case "1" -> addStudents();
                case "2" -> showAllStudents();
                case "3" -> showStudentsDetails();
                case "4" -> updateStudentDetails();
                case "5" -> deleteStudent();
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
                case "1" -> studentSearchByFirstName();
                case "2" -> studentSearchByLastName();
                case "3" -> studentSearch();
                case "4" -> studentSearchByBirthYear();
                case "5" -> studentSearchBetweenTwoDates();
                case "x" -> System.out.println("");
                default -> inputError();
            }
        }
    }

    private void studentSearchByFirstName() {

        try {
            System.out.println("Geben Sie den gewünschten Vornamen an: ");
            String searchString = scan.nextLine();
            List<Student> studentList;
            studentList = repo.findAllStudentsByFirstName(searchString);
            for(Student s : studentList) {
                System.out.println(s);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Studentensuche mit Vornamen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Studentensuche mit Vornamen: " + e.getMessage());
        }
    }

    private void studentSearchByLastName() {

        try {
            System.out.println("Geben Sie den gewünschten Nachnamen an: ");
            String searchString = scan.nextLine();
            List<Student> studentList;
            studentList = repo.findAllStudentsByLastName(searchString);
            for(Student s : studentList) {
                System.out.println(s);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Studentensuche mit Nachnamen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Studentensuche mit Nachnamen: " + e.getMessage());
        }
    }

    private void studentSearch() {

        try {
            System.out.println("Geben Sie einen Namen an: ");
            String searchString = scan.nextLine();
            List<Student> studentList;
            studentList = repo.findAllStudentsByName(searchString);
            for(Student s : studentList) {
                System.out.println(s);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Studentensuche mit Namen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Studentensuche mit Namen: " + e.getMessage());
        }
    }

    private void studentSearchByBirthYear() {

        try {
            System.out.println("Geben Sie eine Jahreszahl ein: ");
            String searchString = scan.nextLine();
            List<Student> studentList;
            studentList = repo.findAllStudentsByBirthYear(searchString);
            for(Student s : studentList) {
                System.out.println(s);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Studentensuche mit Jahreszahl: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Studentensuche mit Jahreszahl: " + e.getMessage());
        }
    }

    private void studentSearchBetweenTwoDates() {

        try {
            System.out.println("Geben Sie das gewünschte Startdatum an (YYYY-MM-DD): ");
            String searchString = scan.nextLine();
            System.out.println("Geben Sie das gewünschte Enddatum an (YYYY-MM-DD): ");
            String searchString1 = scan.nextLine();
            List<Student> studentList;
            studentList = repo.findAllStudentsBetweenTwoDates(searchString, searchString1);
            for(Student s : studentList) {
                System.out.println(s);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Studentensuche mit Datum: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Studentensuche mit Datum: " + e.getMessage());
        }
    }



    private void deleteStudent() {

        System.out.println("Welchen Studenten möchten Sie löschen? Bitte ID eingeben:");

        try {
            Long studentIdToDelete = Long.parseLong(scan.nextLine());
            repo.deleteById(studentIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }

    }

    private void updateStudentDetails() {
        System.out.println("Für welche Studenten-ID möchten Sie die Studentendetails ändern?");

        try {
            Long studentId = Long.parseLong(scan.nextLine());
            Optional<Student> studentOptional = repo.getById(studentId);
            if(studentOptional.isEmpty()) {
                System.out.println("Student mit der angegebenen ID nicht in der Datenbank!");
            } else {
                Student student = studentOptional.get();

                System.out.println("Änderungen für folgenden Studenten: ");
                System.out.println(student);

                String firstName, lastName, birthDate;

                System.out.println("Bitte neue Studentendaten angeben (Enter, falls keine Änderung gewünscht ist):");

                System.out.println("Vorname: ");
                firstName = scan.nextLine();

                System.out.println("Nachname: ");
                lastName = scan.nextLine();

                System.out.println("Geburtsdatum (YYYY-MM-DD): ");
                birthDate = scan.nextLine();

                Optional<Student> optionalStudentUpdated = repo.update(
                        new Student(
                                student.getId(),
                                firstName.equals("") ? student.getFirstName() : firstName,
                                lastName.equals("") ? student.getLastName() : lastName,
                                birthDate.equals("") ? student.getBirthdate() : Date.valueOf(birthDate)
                        )
                );

                optionalStudentUpdated.ifPresentOrElse(
                        (c) -> System.out.println("Student aktualisiert: " + c),
                        () -> System.out.println("Student konnte nicht aktualisiert werden!")
                );
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Studentendaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
    }

    private void addStudents() {

        String firstName, lastName;
        Date birthDate;

        try {
            System.out.println("Bitte alle Studentendaten angeben: ");

            System.out.println("Vorname: ");
            firstName = scan.nextLine();
            if(firstName.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");

            System.out.println("Nachname: ");
            lastName = scan.nextLine();
            if(lastName.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");

            System.out.println("Geburtsdatum (YYYY-MM-DD): ");
            birthDate = Date.valueOf(scan.nextLine());

            Optional<Student> studentOptional = repo.insert(
                    new Student(firstName, lastName, birthDate)
            );

            if(studentOptional.isPresent()) {
                System.out.println("Student angelegt: " + studentOptional.get());
            } else {
                System.out.println("Student konnte nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Studentendaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
    }

    private void showStudentsDetails() {
        System.out.println("Für welchen Studenten möchten Sie die Kursdetails anzeigen?");
        try {
            Long studentId = Long.parseLong(scan.nextLine());
            Optional<Student> studentOptional = repo.getById(studentId);
            if(studentOptional.isPresent()) {
                System.out.println(studentOptional.get());
            } else {
                System.out.println("Kurs mit der ID " + studentId + " nicht gefunden!");
            }
        } catch (DatabaseException e) {
            System.out.println("Datenbankfehler bei Studenten-Detailanzeige: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei Studenten-Detailanzeige: " + e.getMessage());
        }
    }

    private void showAllStudents() {
        List<Student> list = null;

        try {
            list = repo.getAll();
            if (list.size() > 0) {
                for (Student s : list) {
                    System.out.println(s);
                }
            } else {
                System.out.println("Studentenliste leer!");
            }
        } catch (DatabaseException e) {
            System.out.println("Datenbankfehler bei Anzeige aller Studenten: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei Anzeige aller Studenten: " + e.getMessage());
        }
    }

    private void showMenue() {

        System.out.println("------------------- STUDENTENMANAGEMENT --------------------");
        System.out.println("\t(1) Student eingeben\n\t(2) Alle Studenten anzeigen\n\t(3) Studentendetails anzeigen\n\t" +
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
