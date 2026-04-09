package com.example.school.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    // LOGIN PAGE
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	//LOGIN FOR PRINCEPAL
    @GetMapping("/home_princ")
    public String home_princ() {
      return "home_princ";
    }
    //Login for Teacher
    @GetMapping("/home_Teacher")
    public String home_teacher() {
    	return "home_Teacher";
    }
    //Login for Student
    @GetMapping("/home_Student")
    public String home_Student() {
    	return "home_Student";
    }
    //Login for Parents
    @GetMapping("/home_Parent")
    public String home_Parents() {
    	return "home_Parent";
    }
    @GetMapping("/home")
    	public String homePage() {
    		return "home";
    	}

    // DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // FORGOT PASSWORD PAGE
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    // RESET PASSWORD
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String newPassword,
                                RedirectAttributes ra) {

        // DEMO ONLY
        System.out.println("Username: " + username);
        System.out.println("New Password: " + newPassword);

        ra.addFlashAttribute("msg", "Password changed successfully!");
        return "redirect:/login";
    }
}
