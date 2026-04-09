package com.example.school.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer rollNumber;
    private String className;

    public Student() {}

    public Student(String name, Integer rollNumber, String className) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.className = className;
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getRollNumber() { return rollNumber; }
    public void setRollNumber(Integer rollNumber) { this.rollNumber = rollNumber; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    // ✅ Relationship
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendanceList;

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }
}