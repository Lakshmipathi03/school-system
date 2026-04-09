package com.example.school.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.school.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // 🔎 Search Teacher
    List<Teacher> findByNameContainingIgnoreCase(String name);

    // 📚 View by Subject
    List<Teacher> findBySubject(String subject);
}