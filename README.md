# FSE_Landerer_JDBC Dokumentation

Du musst neue Konzepte verstehen und erklären können und diese selbst dokumentieren
(Markdown), dazu zählen:
* MySQL-Datenbankserver einreichten
* DB-Server Adminkonsole einreichten und verwenden können (insb. auch SQL-Statements
absetzen können)
* Java-Maven-Projekt erstellen
* Dependency für die Verbindung einer MySQL-Datenbank in der pom.xml hinzufügen
* Verbindung zur Datenbank aufbauen
* Prepared-Statement für die Abfrage von Daten aus der DB verwenden
* Prepared-Statement für die Änderung von Daten in der DB verwenden
* Abgefragte Daten aus der DB mit ResultSet und Schleifen verarbeiten
* Exceptions verwenden und verarbeiten, die auftreten können
* Debugging von JDBC-Applikationen (SQL-Statements prüfen, Exceptions richtig interpretieren,
Debugger verwenden)

## MySQL-Datenbankserver einrichten

Um auf dem eigenen PC mit einem Datenbankserver arbeiten zu können, wird in der Praxis XAMPP oder Docker verwendet. Da wir im letzten Jahr bereits mit XAMPP gearbeitet haben und dies bereits eingerichtet und konfiguriert ist, wird dies auch nun genutzt. 

![XAMPP](Bilder/XAMPP.jpg)

Ist der Dienst Apache und MySQL gestartet, kann der lokale Rechner auf den Webserver und den SQL Server zugreifen.

## DB-Server Adminkonsole einreichten und verwenden

Auf phMyAdmin können nun die Datenbanken des eigenen Servers verwaltet werden. Eine neue Datenbank kann entweder über ein SQL Statement oder über die GUI erstellt werden. 

![NeueDBAnlegen](Bilder/NeueDB.jpg)

Ist die neue Datenbank erstellt, so ist diese zunächst leer und muss mit Tabellen befüllt werden. Dies kann wieder mit einem SQL-Befehl oder über die GUI geschehen.

![DBohneTab](Bilder/DBohneTab.jpg)

Dabei muss unter anderem der Tabellenname sowie die Spaltennamen und deren Typen angegeben werden. 

![NeueTabelle](Bilder/NeueTabelle.jpg)

Um Datenbanken mit Java zu verwalten und aktualisieren, müssen SQL Statements verwendet werden. 

![SQL-Befehl](Bilder/SQL-Befehl.jpg)

## Java-Maven-Projekt erstellen & Dependency für die Verbindung einer MySQL-Datenbank in der pom.xml hinzufügen

Ein Mavenprojekt kann über IntelliJ ganz einfach erstellt werden. Dabei wird eine pom.xml generiert, die um beliebige Dependencies und Plugins erweitert werden kann.

![pom.xml](Bilder/IntelliJ_pomxml.jpg)

Die Dependency für die Verbindung mit einer MySQL-Datenbank wurde hier schon eingefügt.

## Verbindung zur Datenbank aufbauen

````java
String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String user = "root";
        String pwd = "";
        try (Connection con = DriverManager.getConnection(connectionUrl, user, pwd)){
            System.out.println("Verbindung zur DB hergestellt!");

        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
````

Möchte man nun mit Java eine Verbindung mit der Datenbank herstellen, muss zunächst ein String mit der URL, die zur gewünschten DB führt, erstellt werden. Arbeitet man mit XAMPP, lautet dieser zumeist "jdbc:mysql://localhost:3306/dbname" - außerdem ist der Username standardmäßig "root" und das Passwort leer.

Den eigentlichen Verbindungsaufbau übernimmt dann der DriverManager - mit diesem Service können JDBC-Driver verwaltet werden. DriverManager.getConnection() übergibt man die zuvor angelegte URL, Username und Passwort - sobald die Verbindung hergestellt ist, speichert der DriverManager diese in ein Connection Objekt. Wurde die Verbindung erfolgreich hergestellt, könnte die DB nun nach ````System.out.println("Verbindung zur DB hergestellt!");```` mit SQL-Statements bearbeitet werden.
Normalerweise müsste die Verbindung mit der DB nach beenden der Aufgabe wieder geschlossen werden - schreibt man aber die Verbindung in den Head des try-Blocks, so kann man auf con.close verzichten, da die Verbindung automatisch geschlossen wird, sobald der try-Block abgearbeitet ist.

## Prepared-Statement für die Abfrage von Daten aus der DB verwenden

````java
String sqlSelectAllPersons = "SELECT * FROM `student`";
try (Connection con = DriverManager.getConnection(connectionUrl, user, pwd)){
            System.out.println("Verbindung zur DB hergestellt!");

            PreparedStatement preparedStatement = con.prepareStatement(sqlSelectAllPersons);
            ResultSet rs =  preparedStatement.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("Student aus der DB: [ID] " + id + " [NAME] " + name + " [EMAIL] " + email);
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Aufbau der Verbindung zur DB: \n" + e.getMessage());
        }
````
Mit einem SQL-Statement können nun z.B. alle Daten aus der Datenbank geholt und weiter verarbeitet werden. In dem oben angeführten Beispiel werden alle Datensätze aus der Tabelle "student" herausgeholt, jeweils in Variablen gespeichert und anschließend auf der Kommandozeile ausgegeben.

![Kommandozeilenausgabe](Bilder/IntelliJ_sout.jpg)

## Prepared-Statement für die Änderung von Daten in der DB verwenden

Da sich Daten mit der Zeit ändern können, können Datensätze im nachhinein verändert werden. Dies wird mit dem SQL-Befehl "UPDATE" umgesetzt.

````java
PreparedStatement preparedStatement = con.prepareStatement(
                    "UPDATE `student` SET `name` = ?, `email` = ? WHERE `student`.`id` = ?");
            //reagiert auf Absetzen des Statements
            try {
                preparedStatement.setString(1, neuerName);
                preparedStatement.setString(2, neueEmail);
                preparedStatement.setInt(3, studentId);
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Anzahl der aktualisierten Datensätze: " + affectedRows);
            }catch (SQLException ex) {
                System.out.println("Fehler im SQL-Update Statement: " + ex.getMessage());
            }
````
Der Methode, in der das Update vollzogen wird, werden die neuen Datensätze mitgegeben, welche dann dem SQL-Statement mitgegeben werden. In diesem Beispiel wurde der Datensatz mit der ID 3 verändert, da sich der Name nach eine Hochzeit geändert hat.

![Kommandozeilenausgabe_aktualisiert](Bilder/IntelliJ_sout_aktualisiert.jpg)