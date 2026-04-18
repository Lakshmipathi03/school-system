package com.example.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.school.model.Marks;
import java.util.List;
import org.springframework.ui.Model;
import com.example.school.repository.MarksRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/result")
public class ResultController {

    @Autowired
    private MarksRepository marksRepository;

    // ✅ VIEW RESULT
    @GetMapping("")
    public String viewResultByClass(@RequestParam(required = false) String className, Model model) {

        if (className != null && !className.isEmpty()) {

            List<Marks> marksList = marksRepository.findByStudent_ClassName(className);

            // PASS / FAIL
            for (Marks m : marksList) {
                boolean isPass =
                        m.getEnglish() >= 35 &&
                        m.getSanskrit() >= 35 &&
                        m.getMathas() >= 35 &&
                        m.getScience() >= 35 &&
                        m.getChemistry() >= 35 &&
                        m.getSocial() >= 35;

                m.setResult(isPass ? "PASS" : "FAIL");
            }

            // TOPPER
            int highest = -1;

            for (Marks m : marksList) {
                if ("PASS".equals(m.getResult())) {
                    if (m.getTotalMarks() > highest) {
                        highest = m.getTotalMarks();
                    }
                }
            }

            // assign topper ONLY if PASS
            for (Marks m : marksList) {
                if ("PASS".equals(m.getResult()) && m.getTotalMarks() == highest) {
                    m.setTopper(true);
                }
            }
            model.addAttribute("marksList", marksList);
            model.addAttribute("className", className);
        }

        return "result_by_class";
    }
    @GetMapping("/about")
    public String home_Student() {
    	return "about";
    }
    @GetMapping("/Departments")
    	public String home_Student1() {
    		return "Departments";
    	}
    @GetMapping("/Faculty")
    public String home_Student2() {
    	return "Faculty";
    }
    @GetMapping("/Contact")
    public String home_Student3() {
    	return "Contact";
    }
    @GetMapping("/Rancks")
    public String home_Student4() {
    	return "Rancks";
    }
    @GetMapping("/Facilities")
    public String home_Student5() {
    	return "Facilities";
    }
}