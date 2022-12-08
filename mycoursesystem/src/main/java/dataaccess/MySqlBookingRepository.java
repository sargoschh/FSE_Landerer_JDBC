package dataaccess;

import domain.Booking;
import domain.Course;
import domain.Student;
import util.Assert;

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

    public MySqlStudentRepository getStudentRepo() {
        return studentRepo;
    }

    public MySqlCourseRepository getCourseRepo() {
        return courseRepo;
    }

    @Override
    public Optional<Booking> insert(Booking entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `studentbookscourse` (`id_course`, `id_student`, `bookingdate`, `approved`) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, entity.getCourse().getId());
            preparedStatement.setLong(2, entity.getStudent().getId());
            preparedStatement.setDate(3, entity.getBookingDate());
            preparedStatement.setBoolean(4, entity.isApproved());

            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows == 0) {
                return Optional.empty();
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                return this.getById(generatedKeys.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Optional<Booking> getById(Long id) {
        Assert.notNull(id);
        if(countBookingsInDbWithId(id)==0) {
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM `studentbookscourse` WHERE `id`=?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();

                Booking booking = newBooking(resultSet);
                return Optional.of(booking);
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        }
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
        Assert.notNull(entity);

        String updateSql = "UPDATE `studentbookscourse` SET `approved` = ? WHERE `studentbookscourse`.`id` = ?";

        if(countBookingsInDbWithId(entity.getId())==0) {
            return Optional.empty();
        } else {
            try {
                PreparedStatement preparedStatement = con.prepareStatement(updateSql);

                preparedStatement.setBoolean(1, entity.isApproved());
                preparedStatement.setLong(2, entity.getId());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows==0) {
                    return Optional.empty();
                } else {
                    return this.getById(entity.getId());
                }

            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }
        }
    }

    @Override
    public void deleteById(Long id) {

    }

    private int countBookingsInDbWithId(Long id) {

        try {
            String countSql = "SELECT COUNT(*) FROM `studentbookscourse` WHERE `id`=?";
            PreparedStatement preparedStatementCount = con.prepareStatement(countSql);
            preparedStatementCount.setLong(1, id);
            ResultSet resultSetCount = preparedStatementCount.executeQuery();
            resultSetCount.next();
            int courseCount = resultSetCount.getInt(1);
            return courseCount;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
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
            Student student = getStudentIfAvailable(resultSet);
            Course course = getCourseIfAvailable(resultSet);

            Booking booking = new Booking(
                    resultSet.getLong("id"),
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

    public Student getStudentIfAvailable(ResultSet resultSet) {
        try {
            Optional<Student> student = studentRepo.getById(resultSet.getLong("id_student"));
            if (student.isPresent()) {
                return student.get();
            } else {
                throw new EntitysNotFoundException("Der gewünschte Student ist nicht verfügbar!");
            }
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }

    }

    public Course getCourseIfAvailable(ResultSet resultSet) {
        try {
            Optional<Course> course = courseRepo.getById(resultSet.getLong("id_course"));
            if (course.isPresent()) {
                return course.get();
            } else {
                throw new EntitysNotFoundException("Der gewünschte Kurs ist nicht verfügbar!");
            }
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
