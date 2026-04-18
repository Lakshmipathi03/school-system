package com.example.school.repository;

import com.example.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find students by class
    List<Student> findByClassName(String className);

    Student findByRollNumber(Integer rollNumber);

    long count();

    // Get max roll number for auto increment
    @Query("SELECT MAX(s.rollNumber) FROM Student s")
    Integer findMaxRollNumber();

    // 🔎 Search without changing old methods
    @Query("SELECT s FROM Student s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:className IS NULL OR LOWER(s.className) LIKE LOWER(CONCAT('%', :className, '%')))")
    List<Student> searchStudents(@Param("name") String name,
                                @Param("className") String className);

    // ✅ ADD THIS LINE (only this, nothing else changed)
    boolean existsByStudentAadhar(String studentAadhar);
}