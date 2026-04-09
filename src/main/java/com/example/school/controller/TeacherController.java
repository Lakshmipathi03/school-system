package com.example.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.school.model.Teacher;
import com.example.school.repository.TeacherRepository;

import java.util.List;

@Controller
@RequestMapping("/adi")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    // 🟢 Show Add Teacher Form
    @GetMapping("/teacher")
    public String showForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "teacher_form";
    }

    // 🟢 Save Teacher
    @PostMapping("/teacher")
    public String saveTeacher(@ModelAttribute Teacher teacher, Model model) {
        teacherRepository.save(teacher);
        model.addAttribute("teachers", teacherRepository.findAll());
        return "teacher_list";
    }

    // 🟢 View All Teachers
    @GetMapping("/teachers")
    public String listTeachers(Model model) {
        model.addAttribute("teachers", teacherRepository.findAll());
        return "teacher_list";
    }

    // ✏ Edit Teacher Form
    @GetMapping("/edit-teacher/{id}")
    public String editTeacher(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        model.addAttribute("teacher", teacher);
        return "edit_teacher";
    }

    // 🔄 Update Teacher
    @PostMapping("/update-teacher/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute Teacher teacher) {

        Teacher existingTeacher = teacherRepository.findById(id).orElse(null);

        if (existingTeacher != null) {
            existingTeacher.setName(teacher.getName());
            existingTeacher.setSubject(teacher.getSubject());
            existingTeacher.setEmail(teacher.getEmail());
            existingTeacher.setPassword(teacher.getPassword());

            teacherRepository.save(existingTeacher);
        }

        return "redirect:/adi/teachers";
    }

    // ❌ Delete Teacher
    @GetMapping("/delete-teacher/{id}")
    public String deleteTeacher(@PathVariable Long id) {

        teacherRepository.deleteById(id);

        return "redirect:/adi/teachers";
    }

    // 🔎 Search Teacher
    @GetMapping("/search-teacher")
    public String searchTeacher(@RequestParam String name, Model model) {

        List<Teacher> teachers = teacherRepository.findByNameContainingIgnoreCase(name);

        model.addAttribute("teachers", teachers);
        return "teacher_list";
    }

    // 📚 View Teachers By Subject
    @GetMapping("/teachers-by-subject")
    public String teachersBySubject(@RequestParam String subject, Model model) {

        List<Teacher> teachers = teacherRepository.findBySubject(subject);

        model.addAttribute("teachers", teachers);
        model.addAttribute("subject", subject);

        return "teacher_list";
    }
}