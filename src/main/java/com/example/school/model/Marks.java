package com.example.school.model;

import jakarta.persistence.*;

@Entity
public class Marks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Relationship with Student
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // ✅ Subject Marks
    private int english;
    private int sanskrit;
    private int mathas;
    private int science;
    private int chemistry;
    private int social;
    
    @Transient
    private String result;

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getEnglish() {
        return english;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public int getSanskrit() {
        return sanskrit;
    }

    public void setSanskrit(int sanskrit) {
        this.sanskrit = sanskrit;
    }

    public int getMathas() {
        return mathas;
    }

    public void setMathas(int mathas) {
        this.mathas = mathas;
    }

    public int getScience() {
        return science;
    }

    public void setScience(int science) {
        this.science = science;
    }

    public int getChemistry() {
        return chemistry;
    }

    public void setChemistry(int chemistry) {
        this.chemistry = chemistry;
    }

    public int getSocial() {
        return social;
    }

    public void setSocial(int social) {
        this.social = social;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    @Transient
    public String getGrade() {
        int total = getTotalMarks();
        double percentage = total / 6.0;

        if (percentage >= 75) return "A";
        else if (percentage >= 60) return "B";
        else if (percentage >= 50) return "C";
        else return "FAIL";
    }
    @Transient
    private boolean topper;

    public boolean isTopper() {
        return topper;
    }

    public void setTopper(boolean topper) {
        this.topper = topper;
    }

    // ✅ Optional total marks calculation
    public int getTotalMarks() {
        return english + sanskrit + mathas + science + chemistry + social;
    }
}
