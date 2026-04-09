package com.example.school.controller;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/adi")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // 🟢 Show Add Student Form
    @GetMapping("/student")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "student_form";
    }

    // 🟢 Handle Form Submission
    @PostMapping("/student")
    public String saveStudent(@ModelAttribute Student student, Model model) {
        Integer maxRoll = studentRepository.findMaxRollNumber();

        if (maxRoll == null) {
            maxRoll = 2723100; // Start from 2723101
        }

        student.setRollNumber(maxRoll + 1);
        studentRepository.save(student);

        model.addAttribute("message", "✅ Student added successfully!");
        model.addAttribute("students", studentRepository.findAll());
        return "student_list";
    }

    // 🟢 View All Students
    @GetMapping("/students")
    public String viewAllStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "student_list";
    }
 // 🟡 Show Class Selection Page
    @GetMapping("/selectClass")
    public String selectClassPage() {
        return "select_class1";
    }

 // 🟡 Show Students By Class
    @GetMapping("/studentsByClass")
    public String studentsByClass(@RequestParam String className, Model model) {
        model.addAttribute("students", studentRepository.findByClassName(className));
        model.addAttribute("className", className);
        return "student_class_list";
    }


    // 🟢 Show Edit Student Form
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElse(null);
        model.addAttribute("student", student);
        return "edit_student";
    }


    // 🟢 Update Student
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {

        Student existingStudent = studentRepository.findById(id).orElse(null);

        if (existingStudent != null) {
            existingStudent.setName(student.getName());
            existingStudent.setClassName(student.getClassName());
            studentRepository.save(existingStudent);
        }

        return "redirect:/adi/studentsByClass?className=" + existingStudent.getClassName();
    }


    // 🔴 Delete Student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {

        Student student = studentRepository.findById(id).orElse(null);

        if (student == null) {
            return "redirect:/adi/students";
        }

        String className = student.getClassName();
        studentRepository.delete(student);

        return "redirect:/adi/studentsByClass?className=" + className;
    }
 // 🔎 Search Students
 // 🟢 Show Roll Number Search Form
    @GetMapping("/searchByRoll")
    public String showSearchByRollForm() {
        return "search_by_roll";
    }

    // 🟢 Handle Search
    @GetMapping("/searchByRollResult")
    public String searchByRoll(@RequestParam Integer rollNumber, Model model) {

        Student student = studentRepository.findByRollNumber(rollNumber);

        if (student == null) {
            model.addAttribute("error", "❌ Student not found!");
            return "search_by_roll";
        }

        model.addAttribute("student", student);
        return "student_result_search";
    }
 // 🟣 Total Students Count
    @GetMapping("/totalStudents")
    public String totalStudents(Model model) {

        long total = studentRepository.count();

        model.addAttribute("totalStudents", total);

        return "total_students";
    }    
}

