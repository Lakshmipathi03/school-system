package com.example.school.model;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // keep old field (DO NOT remove)
    private Integer rollNumber;
    private String className;

    // 🆕 New Fields
    private String fullName;
    private String fatherName;
    private String motherName;
    private String address;
    @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
    private String mobileNumber;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "\\d{12}", message = "Aadhar must be 12 digits")
    private String studentAadhar;

    @Pattern(regexp = "\\d{12}", message = "Aadhar must be 12 digits")
    private String fatherAadhar;

    @Pattern(regexp = "^[a-z0-9._%+-]+@gmail\\.com$", message = "Invalid Gmail")
    private String email;

    public Student() {}

    public Student(String name, Integer rollNumber, String className) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.className = className;
    }
    // ✅ Existing Getters/Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getRollNumber() { return rollNumber; }
    public void setRollNumber(Integer rollNumber) { this.rollNumber = rollNumber; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    // 🆕 New Getters/Setters

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStudentAadhar() { return studentAadhar; }
    public void setStudentAadhar(String studentAadhar) { this.studentAadhar = studentAadhar; }

    public String getFatherAadhar() { return fatherAadhar; }
    public void setFatherAadhar(String fatherAadhar) { this.fatherAadhar = fatherAadhar; }

    // ✅ Relationship (unchanged)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendanceList;

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Marks> marksList;

    public List<Marks> getMarksList() {
        return marksList;
    }

    public void setMarksList(List<Marks> marksList) {
        this.marksList = marksList;
    }
}