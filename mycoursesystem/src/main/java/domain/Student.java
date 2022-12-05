package domain;

import java.sql.Date;

public class Student extends BaseEntity {

    private String firstName;
    private String lastName;
    private Date birthdate;


    public Student(Long id, String firstName, String lastName, Date birthdate) {
        super(id);
        setFirstName(firstName);
        setLastName(lastName);
        setBirthdate(birthdate);
    }

    public Student(String firstName, String lastName, Date birthdate) {
        super(null);
        setFirstName(firstName);
        setLastName(lastName);
        setBirthdate(birthdate);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(firstName!=null && firstName.length()>1) {
            this.firstName = firstName;
        } else {
            throw new InvalidValueException("Vorname muss mindestens 2 Zeichen lang sein!");
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if(lastName!=null && lastName.length()>1) {
            this.lastName = lastName;
        } else {
            throw new InvalidValueException("Nachname muss mindestens 2 Zeichen lang sein!");
        }
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        if(birthdate!=null) {
            Date now = new Date(System.currentTimeMillis());
            if(birthdate.before(now)) {
                this.birthdate = birthdate;
            } else {
                throw new InvalidValueException("Geburtsdatum darf nicht nach dem aktuellen Datum liegen!");
            }
        } else {
            throw new InvalidValueException("Geburtsdatum darf nicht null / leer sein!");
        }

    }
}
