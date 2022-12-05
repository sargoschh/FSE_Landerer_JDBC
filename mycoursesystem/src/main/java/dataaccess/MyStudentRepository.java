package dataaccess;

import domain.Student;

import java.util.List;

public interface MyStudentRepository extends BaseRepository<Student, Long> {

    List<Student> findAllStudentsByFirstName(String firstName);
    List<Student> findAllStudentsByLastName(String lastName);
    List<Student> findAllStudentsByName(String name);
    List<Student> findAllStudentsByBirthYear(String year);
    List<Student> findAllStudentsBetweenTwoDates(String dateOne, String dateTwo);
}
