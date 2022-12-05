package dataaccess;

import domain.Course;
import domain.CourseType;
import domain.Student;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlStudentRepository implements MyStudentRepository {

    private Connection con;

    public MySqlStudentRepository() throws SQLException, ClassNotFoundException {
        this.con = MysqlDatabaseConnection.
                getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
    }

    public void updateAndInsert(PreparedStatement preparedStatement, Student entity) {
        try {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setDate(3, entity.getBirthdate());
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Optional<Student> insert(Student entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `students` (`firstname`, `lastname`, `birthdate`) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            updateAndInsert(preparedStatement, entity);

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
    public Optional<Student> getById(Long id) {
        Assert.notNull(id);
        if(countStudentsInDbWithId(id)==0) {
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM `students` WHERE `id` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();

                Student student = newStudent(resultSet);
                return Optional.of(student);
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        }
    }

    public Student newStudent(ResultSet resultSet) {
        try {
            Student student = new Student(
                    resultSet.getLong("id"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getDate("birthdate")
            );
            return student;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    private int countStudentsInDbWithId(Long id) {
        try {
            String countSql = "SELECT COUNT(*) FROM `students` WHERE `id`=?";
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

    public List<Student> getStudentList(PreparedStatement preparedStatement) {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while (resultSet.next()) {
                studentList.add(newStudent(resultSet));
            }
            return studentList;
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public List<Student> getAll() {
        String sql = "SELECT * FROM `students`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            return getStudentList(preparedStatement);
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public Optional<Student> update(Student entity) {
        Assert.notNull(entity);

        String updateSql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";

        if(countStudentsInDbWithId(entity.getId())==0) {
            return Optional.empty();
        } else {
            try {
                PreparedStatement preparedStatement = con.prepareStatement(updateSql);

                updateAndInsert(preparedStatement, entity);

                preparedStatement.setLong(7, entity.getId());

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
        Assert.notNull(id);
        String deleteSql = "DELETE FROM `students` WHERE `id`=?";

        if(countStudentsInDbWithId(id)==1) {

            try {
                PreparedStatement preparedStatement = con.prepareStatement(deleteSql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }

        }
    }

    @Override
    public List<Student> findAllStudentsByFirstName(String firstName) {
        try {
            String sql = "SELECT * FROM `students` WHERE LOWER(`firstname`) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+firstName+"%");

            return getStudentList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> findAllStudentsByLastName(String lastName) {
        try {
            String sql = "SELECT * FROM `students` WHERE LOWER(`lastname`) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+lastName+"%");

            return getStudentList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> findAllStudentsByName(String name) {
        try {
            String sql = "SELECT * FROM `students` WHERE CONCAT(CONCAT(LOWER(`firstname`), ' '), LOWER(`lastname`)) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+name+"%");

            return getStudentList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> findAllStudentsByBirthYear(String year) {
        try {
            String sql = "SELECT * FROM `students` WHERE YEAR(`birthdate`) LIKE ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(year));

            return getStudentList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> findAllStudentsBetweenTwoDates(Date dateOne, Date dateTwo) {
        try {
            String sql = "SELECT * FROM `students` WHERE `birthdate` > ? AND `birthdate` < ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(dateOne));
            preparedStatement.setString(2, String.valueOf(dateTwo));

            return getStudentList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }
}
