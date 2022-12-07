package domain;

import java.sql.Date;

public class Booking {

    private Student student;
    private Course course;
    private Date bookingDate;
    private boolean approved;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        if(student!=null){
            this.student = student;
        } else {
            throw new InvalidValueException("Student darf nicht null / leer sein!");
        }
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        if(course!=null){
            this.course = course;
        } else {
            throw new InvalidValueException("Kurs darf nicht null / leer sein");
        }
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        if(bookingDate!=null) {
            Date now = new Date(System.currentTimeMillis());
            if(bookingDate.before(now)) {
                this.bookingDate = bookingDate;
            } else {
                throw new InvalidValueException("Buchungsdatum darf nicht nach dem aktuellen Datum liegen!");
            }
        } else {
            throw new InvalidValueException("Buchungsdatum darf nicht null / leer sein!");
        }
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "student=" + student +
                ", course=" + course +
                ", bookingDate=" + bookingDate +
                '}';
    }
}
