package dataaccess;

import domain.Student;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySqlStudentRepository implements MyStudentRepository {

    private Connection con;

    public MySqlStudentRepository() throws SQLException, ClassNotFoundException {
        this.con = MysqlDatabaseConnection.
                getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
    }

    public void updateAndInsert(PreparedStatement preparedStatement, Student entity) {

    }

    @Override
    public Optional<Student> insert(Student entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Student> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Student> getAll() {
        return null;
    }

    @Override
    public Optional<Student> update(Student entity) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Student> findAllStudentsByFirstName(String firstName) {
        return null;
    }

    @Override
    public List<Student> findAllStudentsByLastName(String LastName) {
        return null;
    }

    @Override
    public List<Student> findAllStudentsByName(String name) {
        return null;
    }

    @Override
    public List<Student> findAllStudentsByBirthYear(String year) {
        return null;
    }

    @Override
    public List<Student> findAllStudentsBetweenTwoDates(Date dateOne, Date dateTwo) {
        return null;
    }
}
