package com.example.school.controller;

import com.example.school.model.Marks;
import com.example.school.model.Student;
import com.example.school.repository.MarksRepository;
import com.example.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/marks")
public class MarksController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MarksRepository marksRepository;

    // ✅ 1. Load class selection page
    @GetMapping("/select-class")
    public String selectClassPage() {
        return "select-class";
    }

    // ✅ 2. Show students based on selected class
    @GetMapping("/students")
    public String getStudentsByClass(@RequestParam String className, Model model) {
        List<Student> students = studentRepository.findByClassName(className);
        model.addAttribute("students", students);
        model.addAttribute("className", className);
        return "enter-marks"; // this Thymeleaf page will display the form
    }

    // ✅ 3. Save marks
    @PostMapping("/save")
    public String saveMarks(@ModelAttribute Marks marks, @RequestParam Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            marks.setStudent(student);
            marksRepository.save(marks);
        }
        return "redirect:/marks/select-class";
    }
 // ✅ 4. View All Marks List
    @GetMapping("/list")
    public String viewAllMarks(Model model) {
        List<Marks> marksList = marksRepository.findAll();
        model.addAttribute("marksList", marksList);
        return "marks_list";
    }
 // ✅ View marks by class
    @GetMapping("/view-by-class")
    public String viewByClass(@RequestParam(required = false) String className, Model model) {

        if (className != null) {
            List<Marks> marksList = marksRepository.findByStudent_ClassName(className);
            model.addAttribute("marksList", marksList);
            model.addAttribute("className", className);
        }

        return "marks_by_class";
    }
    @GetMapping("/view-class-marks")
    public String viewClassMarks(@RequestParam (required = false) String className, Model model) {

        if(className != null) {
        	List<Marks> marksList = marksRepository.findByStudent_ClassName(className);
            model.addAttribute("marksList", marksList);
            model.addAttribute("className", className);
        }

        return "marksclass"; // same page reuse
    }


    // ✅ Edit Marks Page
    @GetMapping("/edit/{id}")
    public String editMarks(@PathVariable Long id, Model model) {
        Marks marks = marksRepository.findById(id).orElse(null);
        model.addAttribute("marks", marks);
        return "edit_marks";
    }


    // ✅ Update Marks
    @PostMapping("/update/{id}")
    public String updateMarks(@PathVariable Long id, @ModelAttribute Marks marks) {
        Marks existingMarks = marksRepository.findById(id).orElse(null);

        if (existingMarks != null) {
            existingMarks.setEnglish(marks.getEnglish());
            existingMarks.setSanskrit(marks.getSanskrit());
            existingMarks.setMathas(marks.getMathas());
            existingMarks.setScience(marks.getScience());
            existingMarks.setChemistry(marks.getChemistry());
            existingMarks.setSocial(marks.getSocial());

            marksRepository.save(existingMarks);
        }

        return "redirect:/marks/view-by-class?className=" + existingMarks.getStudent().getClassName();
    }


    // ✅ Delete Marks
    @GetMapping("/delete/{id}")
    public String deleteMarks(@PathVariable Long id) {

        Marks marks = marksRepository.findById(id).orElse(null);

        if (marks != null) {
            String className = marks.getStudent().getClassName();
            marksRepository.deleteById(id);
            return "redirect:/marks/view-by-class?className=" + className;
        }

        return "redirect:/marks/select-class";
    }
 
}
