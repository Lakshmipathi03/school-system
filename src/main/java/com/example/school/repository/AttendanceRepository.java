package com.example.school.repository;

import com.example.school.model.Attendance;
import com.example.school.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;   // ⭐ add this import

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // existing method
    List<Attendance> findByStudent_ClassName(String className);

    // ⭐ monthly attendance
    List<Attendance> findByStudent_ClassNameAndDateBetween(
            String className,
            LocalDate startDate,
            LocalDate endDate
    );


    // ⭐ filter by date
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByStudentAndDateBetween(Student student,
            LocalDate start,
            LocalDate end);
}