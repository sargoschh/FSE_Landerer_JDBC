package dataaccess;

import domain.Booking;
import domain.Course;
import domain.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlBookingRepository implements MyBookingRepository {

    private Connection con;
    MySqlStudentRepository studentRepo = new MySqlStudentRepository();
    MySqlCourseRepository courseRepo = new MySqlCourseRepository();

    public MySqlBookingRepository() throws SQLException, ClassNotFoundException {
        this.con = MysqlDatabaseConnection.
                getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
    }

    @Override
    public Optional<Booking> insert(Booking entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Booking> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Booking> getAll() {
        String sql = "SELECT * FROM `studentbookscourse`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            return getBookingList(preparedStatement);
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public Optional<Booking> update(Booking entity) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    public List<Booking> getBookingList(PreparedStatement preparedStatement) {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Booking> bookingList = new ArrayList<>();
            while (resultSet.next()) {
                bookingList.add(newBooking(resultSet));
            }
            return bookingList;
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    public Booking newBooking(ResultSet resultSet) {
        try {
            Student student = null;
            Optional<Student> student1 = studentRepo.getById(resultSet.getLong("id_student"));
            if (student1.isPresent()) {
                student = student1.get();
            } else {
                throw new EntitysNotFoundException("Der gewünschte Student ist nicht verfügbar!");
            }
            Course course = null;
            Optional<Course> course1 = courseRepo.getById(resultSet.getLong("id_course"));
            if(course1.isPresent()) {
                course = course1.get();
            } else {
                throw new EntitysNotFoundException("Der gewünschte Kurs ist nicht verfügbar!");
            }

            Booking booking = new Booking(
                    course,
                    student,
                    resultSet.getDate("bookingdate"),
                    resultSet.getBoolean("approved")
            );
            return booking;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Booking> findAllBookingsByStudentId(Long studentID) {
        return null;
    }

    @Override
    public List<Booking> findAllBookingsByCourseId(Long courseID) {
        return null;
    }

    @Override
    public List<Booking> findAllBookingsByBookingDate(Date bookingDate) {
        return null;
    }

    @Override
    public List<Booking> findAllBookingsBeforeDate(Date date) {
        return null;
    }

    @Override
    public List<Booking> findAllBookingsByApproval(Boolean approved) {
        return null;
    }
}
