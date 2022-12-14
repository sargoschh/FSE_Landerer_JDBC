package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLIcourse {

    Scanner scan;
    MyCourseRepository repo;

    public CLIcourse(MyCourseRepository repo) {
        this.scan = new Scanner(System.in);
        this.repo = repo;
    }

    public void start() {
        String input = "-";
        while(!input.equalsIgnoreCase("x")) {

            showMenue();
            input = this.scan.nextLine();
            switch (input) {
                case "1" -> addCourse();
                case "2" -> showAllCourses();
                case "3" -> showCourseDetails();
                case "4" -> updateCourseDetails();
                case "5" -> deleteCourse();
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
                case "1" -> courseSearchByName();
                case "2" -> courseSearchByDescription();
                case "3" -> courseSearch();
                case "4" -> courseSearchByCoursetype();
                case "5" -> courseSearchByStartdate();
                case "6" -> runningCourses();
                case "x" -> System.out.println("");
                default -> inputError();
            }
        }
    }

    private void courseSearchByName() {
        System.out.println("Geben Sie den gewünschten Namen an: ");
        try {
            String searchString = scan.nextLine();
            List<Course> courseList;
            courseList = repo.findAllCoursesByName(searchString);
            for(Course c : courseList) {
                System.out.println(c);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kurssuche mit Namen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Kurssuche mit Namen: " + e.getMessage());
        }
    }

    private void courseSearchByDescription() {
        System.out.println("Geben Sie einen Suchbegriff aus der Beschreibung an: ");
        try {
            String searchString = scan.nextLine();
            List<Course> courseList;
            courseList = repo.findAllCoursesByDescription(searchString);
            for(Course c : courseList) {
                System.out.println(c);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Kurssuche: " + e.getMessage());
        }
    }

    private void courseSearchByCoursetype() {
        System.out.println("Geben Sie einen Kurstyp an: ");
        try {
            String searchString = scan.nextLine();

            List<Course> courseList;
            courseList = repo.findAllCoursesByCourseType(CourseType.valueOf(searchString.toUpperCase()));
            for(Course c : courseList) {
                System.out.println(c);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Kurssuche: " + e.getMessage());
        }
    }

    private void courseSearchByStartdate() {
        System.out.println("Geben Sie das gewünschte Startdatum an: ");
        try {
            String searchString = scan.nextLine();
            List<Course> courseList;
            courseList = repo.findAllCoursesByStartDate(Date.valueOf(searchString));
            for(Course c : courseList) {
                System.out.println(c);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Kurssuche: " + e.getMessage());
        }
    }

    private void runningCourses() {
        System.out.println("Laufende Kurse: ");
        List<Course> list;
        try {
            list = repo.findAllRunningCourses();
            for (Course c : list) {
                System.out.println(c);
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Kurs-Anzeige für laufende Kurse: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei Kurs-Anzeige für laufende Kurse: " + e.getMessage());
        }
    }

    private void courseSearch() {
        System.out.println("Geben Sie einen Suchbegriff an: ");
        try {
            String searchString = scan.nextLine();
            List<Course> courseList;
            courseList = repo.findAllCoursesByNameOrDescription(searchString);
            for(Course c : courseList) {
                System.out.println(c);
            }
        }catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Kurssuche: " + e.getMessage());
        }
    }

    private void deleteCourse() {

        System.out.println("Welchen Kurs möchten Sie löschen? Bitte ID eingeben:");

        try {
            Long courseIdToDelete = Long.parseLong(scan.nextLine());
            repo.deleteById(courseIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }

    }

    private void updateCourseDetails() {
        System.out.println("Für welche Kurs-ID möchten Sie die Kursdetails ändern?");

        try {
            Long courseId = Long.parseLong(scan.nextLine());
            Optional<Course> courseOptional = repo.getById(courseId);
            if(courseOptional.isEmpty()) {
                System.out.println("Kurs mit der angegebenen ID nicht in der Datenbank!");
            } else {
                Course course = courseOptional.get();

                System.out.println("Änderungen für folgenden Kurs: ");
                System.out.println(course);

                String name, description, hours, dateFrom, dateTo, courseType;

                System.out.println("Bitte neue Kursdaten angeben (Enter, falls keine Änderung gewünscht ist):");

                System.out.println("Name: ");
                name = scan.nextLine();

                System.out.println("Beschreibung: ");
                description = scan.nextLine();

                System.out.println("Stundenanzahl: ");
                hours = scan.nextLine();

                System.out.println("Startdatum (YYYY-MM-DD): ");
                dateFrom = scan.nextLine();

                System.out.println("Enddatum (YYYY-MM-DD): ");
                dateTo = scan.nextLine();

                System.out.println("Kurstyp (ZA/BF/FF/OE): ");
                courseType = scan.nextLine();

                Optional<Course> optionalCourseUpdated = repo.update(
                        new Course(
                                course.getId(),
                                name.equals("") ? course.getName() : name,
                                description.equals("") ? course.getDescription() : description,
                                hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                                dateFrom.equals("") ? course.getBeginDate() : Date.valueOf(dateFrom),
                                dateTo.equals("") ? course.getEndDate() : Date.valueOf(dateTo),
                                courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                        )
                );

                optionalCourseUpdated.ifPresentOrElse(
                        (c) -> System.out.println("Kurs aktualisiert: " + c),
                        () -> System.out.println("Kurs konnte nicht aktualisiert werden!")
                );
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
    }

    private void addCourse() {

        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseType courseType;

        try {
            System.out.println("Bitte alle Kursdaten angeben: ");

            System.out.println("Name: ");
            name = scan.nextLine();
            if(name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");

            System.out.println("Beschreibung: ");
            description = scan.nextLine();
            if(description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");

            System.out.println("Stundenanzahl: ");
            hours = Integer.parseInt(scan.nextLine());

            System.out.println("Startdatum (YYYY-MM-DD): ");
            dateFrom = Date.valueOf(scan.nextLine());

            System.out.println("Enddatum (YYYY-MM-DD): ");
            dateTo = Date.valueOf(scan.nextLine());

            System.out.println("Kurstyp (ZA/BF/FF/OE): ");
            courseType = CourseType.valueOf(scan.nextLine());

            Optional<Course> optionalCourse = repo.insert(
                    new Course(name, description, hours, dateFrom, dateTo, courseType)
            );

            if(optionalCourse.isPresent()) {
                System.out.println("Kurs angelegt: " + optionalCourse.get());
            } else {
                System.out.println("Kurs konnte nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
    }

    private void showCourseDetails() {
        System.out.println("Für welchen Kurs möchten Sie die Kursdetails anzeigen?");
        try {
            Long courseId = Long.parseLong(scan.nextLine());
            Optional<Course> courseOptional = repo.getById(courseId);
            if(courseOptional.isPresent()) {
                System.out.println(courseOptional.get());
            } else {
                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden!");
            }
        } catch (DatabaseException e) {
            System.out.println("Datenbankfehler bei Kurs-Detailanzeige: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeige: " + e.getMessage());
        }
    }

    private void showAllCourses() {
        List<Course> list = null;

        try {
            list = repo.getAll();
            if (list.size() > 0) {
                for (Course c : list) {
                    System.out.println(c);
                }
            } else {
                System.out.println("Kursliste leer!");
            }
        } catch (DatabaseException e) {
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei Anzeige aller Kurse: " + e.getMessage());
        }
    }

    private void showMenue() {

        System.out.println("---------------------- KURSMANAGEMENT ----------------------");
        System.out.println("\t(1) Kurs eingeben\n\t(2) Alle Kurse anzeigen\n\t(3) Kursdetails anzeigen\n\t" +
                "(4) Kursdetails ändern\n\t(5) Kurs löschen\n\t(6) Kurssuche\n\t(x) Beenden");
    }

    private void showSearchMenue() {
        System.out.println("\t(1) Suche mit Kursnamen\n\t(2) Suche mit Kursbeschreibung\n\t" +
                "(3) Suche mit Kursname oder -beschreibung\n\t(4) Suche mit Kurstyp\n\t" +
                "(5) Suche mit Startdatum\n\t(6) Suche alle laufenden Kurse\n\t(x) Zurück zum Management");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menueauswahl eingeben!");
    }
}
