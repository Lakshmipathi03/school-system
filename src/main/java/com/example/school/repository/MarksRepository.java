package com.example.school.repository;

import com.example.school.model.Marks;
import com.example.school.model.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MarksRepository extends JpaRepository<Marks, Long> {
	List<Marks> findByStudent_ClassName(String className);
}
