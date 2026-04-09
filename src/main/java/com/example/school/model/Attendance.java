package com.example.school.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ ONLY ONE mapping
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private LocalDate date;
    private String status; // Present / Absent

    // Constructors
    public Attendance() {}

    public Attendance(Student student, String status, LocalDate date) {
        this.student = student;
        this.status = status;
        this.date = date;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}