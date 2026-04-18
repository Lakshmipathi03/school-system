package com.example.school.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.school.model.Student;
import com.example.school.model.Fee;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.FeeRepository;

@Controller
@RequestMapping("/adi/fees")
public class FeeController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FeeRepository feeRepository;

    // 🔎 Search Page
    @GetMapping("/search")
    public String searchPage() {
        return "fee_search";
    }

    // 🔎 Search Student
    @GetMapping("/searchResult")
    public String searchStudent(@RequestParam Integer rollNumber, Model model) {

        Student student = studentRepository.findByRollNumber(rollNumber);

        if (student == null) {
            model.addAttribute("error", "❌ Student not found!");
            return "fee_search";
        }

        // ✅ Calculate total fee
        double totalFee = 0;

        switch (student.getClassName()) {
            case "5th": totalFee = 65000; break;
            case "6th": totalFee = 65000; break;
            case "7th": totalFee = 75000; break;
            case "8th": totalFee = 85000; break;
            case "9th": totalFee = 95000; break;
            case "10th": totalFee = 100000; break;
        }

        // ✅ Paid amount
        Double paid = feeRepository.getTotalPaid(student.getRollNumber());
        if (paid == null) paid = 0.0;

        double balance = totalFee - paid;

        // ✅ Send data to UI
        model.addAttribute("student", student);
        model.addAttribute("totalFee", totalFee);
        model.addAttribute("balance", balance);

        return "fee_table";
    }
    // 💳 Open Payment Page
    @GetMapping("/pay/{rollNumber}")
    public String payPage(@PathVariable Integer rollNumber, Model model) {

        Student student = studentRepository.findByRollNumber(rollNumber);

        // 🎯 Total fee based on class
        double totalFee = 0;

        switch (student.getClassName()) {
        	case "5th": totalFee = 55000; break;
            case "6th": totalFee = 65000; break;
            case "7th": totalFee = 75000; break;
            case "8th": totalFee = 85000; break;
            case "9th": totalFee = 95000; break;
            case "10th": totalFee = 100000; break;
        }

        // 💰 Already paid
        Double paid = feeRepository.getTotalPaid(student.getRollNumber());
        if (paid == null) paid = 0.0;

        double balance = totalFee - paid;

        Fee fee = new Fee();
        fee.setStudentName(student.getName());
        fee.setClassName(student.getClassName());
        fee.setRollNumber(student.getRollNumber());

        model.addAttribute("fee", fee);
        model.addAttribute("totalFee", totalFee);
        model.addAttribute("paid", paid);
        model.addAttribute("balance", balance);

        return "payment_page";
    }
    // 💰 Process Payment
    @PostMapping("/processPayment")
    public String processPayment(@ModelAttribute Fee fee, Model model) {

        // 🎯 Total fee
        double totalFee = 0;

        switch (fee.getClassName()) {
            case "5th": totalFee = 65000; break;
            case "6th": totalFee = 65000; break;
            case "7th": totalFee = 75000; break;
            case "8th": totalFee = 85000; break;
            case "9th": totalFee = 95000; break;
            case "10th": totalFee = 100000; break;
        }

        Double paid = feeRepository.getTotalPaid(fee.getRollNumber());
        if (paid == null) paid = 0.0;

        double balance = totalFee - paid;

        // ❌ Minimum check
        if (fee.getAmount() < 1000) {
            model.addAttribute("error", "❌ Minimum amount is ₹1,000");
            return "payment_page";
        }

        // ❌ Overpayment check
        if (fee.getAmount() > balance) {
            model.addAttribute("error", "❌ Cannot pay more than remaining balance ₹" + balance);
            return "payment_page";
        }

        // ✅ Generate receipt number (ONLY ONCE)
        if (fee.getReceiptNumber() == null) {
            String receiptNo = "REC-" + System.currentTimeMillis();
            fee.setReceiptNumber(receiptNo);
        }

        // ✅ Set dates
        fee.setReceiptDate(LocalDate.now());
        fee.setPaymentDate(LocalDate.now());

        // ✅ Status
        fee.setStatus("Paid");

        // 💾 Save
        feeRepository.save(fee);

        double newPaid = paid + fee.getAmount();
        double newBalance = totalFee - newPaid;

        model.addAttribute("fee", fee);
        model.addAttribute("totalFee", totalFee);
        model.addAttribute("paid", newPaid);
        model.addAttribute("balance", newBalance);

        return "payment_slip";
    }
    @GetMapping("/summary")
    public String feeSummary(Model model) {

        List<Student> students = studentRepository.findAll();

        List<Map<String, Object>> summaryList = new ArrayList<>();

        for (Student s : students) {

            double totalFee = 0;

            switch (s.getClassName()) {
            	case "5th": totalFee = 65000; break;
                case "6th": totalFee = 65000; break;
                case "7th": totalFee = 75000; break;
                case "8th": totalFee = 85000; break;
                case "9th": totalFee = 95000; break;
                case "10th": totalFee = 100000; break;
            }

            Double paid = feeRepository.getTotalPaid(s.getRollNumber());
            if (paid == null) paid = 0.0;

            double balance = totalFee - paid;

            String status = (balance == 0) ? "Paid" : "Pending";

            Map<String, Object> map = new HashMap<>();
            map.put("name", s.getName());
            map.put("className", s.getClassName());
            map.put("rollNumber", s.getRollNumber());
            map.put("totalFee", totalFee);
            map.put("paid", paid);
            map.put("balance", balance);
            map.put("status", status);

            summaryList.add(map);
        }

        model.addAttribute("summaryList", summaryList);

        return "fee_summary";
    }
    @GetMapping("/daily")
    public String dailyReport(@RequestParam(required = false) String date, Model model) {

        LocalDate selectedDate;

        if (date == null || date.isEmpty()) {
            selectedDate = LocalDate.now(); // default today
        } else {
            selectedDate = LocalDate.parse(date);
        }

        List<Fee> fees = feeRepository.findByPaymentDate(selectedDate);

        model.addAttribute("fees", fees);
        model.addAttribute("selectedDate", selectedDate);

        return "daily_report";
    }
 // 🔎 Search Receipt by Number
    @GetMapping("/receipt")
    public String searchReceiptPage() {
        return "receipt_search";
    }

    // 📄 Show Receipt
    @GetMapping("/receipt/view")
    public String viewReceipt(@RequestParam String receiptNumber, Model model) {

        Fee fee = feeRepository.findByReceiptNumber(receiptNumber);

        if (fee == null) {
            model.addAttribute("error", "❌ Receipt not found!");
            return "receipt_search";
        }

        // 🎯 Total fee
        double totalFee = 0;

        switch (fee.getClassName()) {
            case "5th": totalFee = 65000; break;
            case "6th": totalFee = 65000; break;
            case "7th": totalFee = 75000; break;
            case "8th": totalFee = 85000; break;
            case "9th": totalFee = 95000; break;
            case "10th": totalFee = 100000; break;
        }

        Double paid = feeRepository.getTotalPaid(fee.getRollNumber());
        if (paid == null) paid = 0.0;

        double balance = totalFee - paid;

        model.addAttribute("fee", fee);
        model.addAttribute("totalFee", totalFee);
        model.addAttribute("paid", paid);
        model.addAttribute("balance", balance);

        return "payment_slip"; // ✅ same page reuse
    }
 // 📄 View Fee History (Before Payment)
    @GetMapping("/history/{rollNumber}")
    public String viewFeeHistory(@PathVariable Integer rollNumber, Model model) {

        Student student = studentRepository.findByRollNumber(rollNumber);

        if (student == null) {
            model.addAttribute("error", "❌ Student not found!");
            return "fee_search";  // ✅ FIXED
        }

        List<Fee> feeList = feeRepository.findByRollNumber(rollNumber);

        Double paid = feeRepository.getTotalPaid(rollNumber);
        if (paid == null) paid = 0.0;

        double totalFee = 0;
        switch (student.getClassName()) {
            case "5th": totalFee = 65000; break;
            case "6th": totalFee = 65000; break;
            case "7th": totalFee = 75000; break;
            case "8th": totalFee = 85000; break;
            case "9th": totalFee = 95000; break;
            case "10th": totalFee = 100000; break;
        }

        double balance = totalFee - paid;

        model.addAttribute("student", student);
        model.addAttribute("feeList", feeList);
        model.addAttribute("totalFee", totalFee);
        model.addAttribute("paid", paid);
        model.addAttribute("balance", balance);

        return "student_fee_details"; // ✅ your new page
    }
    
}