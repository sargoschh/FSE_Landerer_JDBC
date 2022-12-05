package dataaccess;

import domain.Course;
import domain.CourseType;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlCourseRepository implements MyCourseRepository {

    private Connection con;

    public MySqlCourseRepository() throws SQLException, ClassNotFoundException {
        this.con = MysqlDatabaseConnection.
                getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
    }

    public void updateAndInsert(PreparedStatement preparedStatement, Course entity) {
        try {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getHours());
            preparedStatement.setDate(4, entity.getBeginDate());
            preparedStatement.setDate(5, entity.getEndDate());
            preparedStatement.setString(6, entity.getCourseType().toString());
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    @Override
    public Optional<Course> insert(Course entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `courses` (`name`, `description`, `hours`, `begindate`, `enddate`, `coursetype`) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

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
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    @Override
    public Optional<Course> getById(Long id) {
        Assert.notNull(id);
        if(countCoursesInDbWithId(id)==0) {
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM `courses` WHERE `id` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();

                Course course = new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                );
                return Optional.of(course);
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        }

    }

    private int countCoursesInDbWithId(Long id) {
        try {
            String countSql = "SELECT COUNT(*) FROM `courses` WHERE `id`=?";
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

    public List<Course> getCourseList(PreparedStatement preparedStatement) {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();
            while (resultSet.next()) {
                courseList.add(new Course(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getInt("hours"),
                                resultSet.getDate("begindate"),
                                resultSet.getDate("enddate"),
                                CourseType.valueOf(resultSet.getString("coursetype"))
                        )
                );
            }
            return courseList;
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public List<Course> getAll() {
        String sql = "SELECT * FROM `courses`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            return getCourseList(preparedStatement);
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public Optional<Course> update(Course entity) {
        Assert.notNull(entity);

        String updateSql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";

        if(countCoursesInDbWithId(entity.getId())==0) {
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
        String deleteSql = "DELETE FROM `courses` WHERE `id`=?";

        if(countCoursesInDbWithId(id)==1) {

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
    public List<Course> findAllCoursesByName(String name) {
        try {
            String sql = "SELECT * FROM `courses` WHERE LOWER(`name`) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+name+"%");

            return getCourseList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Course> findAllCoursesByDescription(String description) {
        try {
            String sql = "SELECT * FROM `courses` WHERE LOWER(`description`) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+description+"%");

            return getCourseList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Course> findAllCoursesByNameOrDescription(String searchText) {

        try {
            String sql = "SELECT * FROM `courses` WHERE LOWER(`description`) LIKE LOWER(?) OR LOWER(`name`) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+searchText+"%");
            preparedStatement.setString(2, "%"+searchText+"%");

            return getCourseList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }

    }

    @Override
    public List<Course> findAllCoursesByCourseType(CourseType courseType) {
        try {
            String sql = "SELECT * FROM `courses` WHERE LOWER(`coursetype`) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+ courseType +"%");

            return getCourseList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Course> findAllCoursesByStartDate(Date startDate) {
        try {
            String sql = "SELECT * FROM `courses` WHERE `begindate` LIKE ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(startDate));

            return getCourseList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Course> findAllRunningCourses() {

        String sql = "SELECT * FROM `courses` WHERE NOW()<`enddate` AND NOW()>`begindate`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            return getCourseList(preparedStatement);
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }

    }
}
