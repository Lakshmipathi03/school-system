package com.example.school.controller;

import com.example.school.model.Attendance;
import com.example.school.model.Student;
import com.example.school.repository.AttendanceRepository;
import com.example.school.repository.StudentRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@Controller
@RequestMapping("/adi")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    // 🟢 Step 1: Select class before marking attendance
    @GetMapping("/attendance/select-class")
    public String selectClassPage(Model model) {
        model.addAttribute("classes", new String[]{"5th", "6th", "7th", "8th", "9th", "10th"});
        return "select_class_attendance";
    }
    @GetMapping("/attendance/select-class-2")
    public String selectClassPage2(Model model) {
        model.addAttribute("classes", new String[]{"5th", "6th", "7th", "8th", "9th", "10th"});
        return "select_class_2";   // ✅ your new file
    }

    // 🟢 Step 2: Show students of selected class
    @GetMapping("/attendance/class")
    public String showClassStudents(@RequestParam("className") String className, Model model) {
        List<Student> studentsInClass = studentRepository.findByClassName(className);
        model.addAttribute("students", studentsInClass);
        model.addAttribute("className", className);
        return "attendance_form";
    }

    // 🟢 Step 3: Save attendance for multiple students
    @PostMapping("/attendance/save")
    public String saveAttendance(
            @RequestParam("studentId") List<Long> studentIds,
            @RequestParam("status") List<String> statuses,
            @RequestParam("className") String className,
            @RequestParam("date") String selectedDate,   // ✅ NEW
            Model model) {

        LocalDate date = LocalDate.parse(selectedDate);  // ✅ USE SELECTED DATE

        for (int i = 0; i < studentIds.size(); i++) {
            Student student = studentRepository.findById(studentIds.get(i)).orElse(null);
            if (student != null) {
                Attendance attendance = new Attendance(student, statuses.get(i), date);
                attendanceRepository.save(attendance);
            }
        }

        model.addAttribute("message", "✅ Attendance saved successfully!");
        model.addAttribute("attendances",
                attendanceRepository.findByStudent_ClassName(className));

        return "attendance_list";
    }
    // 🟢 Step 4: View all attendance records
    @GetMapping("/attendances")
    public String viewAllAttendance(Model model) {
        model.addAttribute("attendances", attendanceRepository.findAll());
        return "attendance_list";
    }
    @GetMapping("/attendance/class-students")
    public String getStudentsByClass(@RequestParam String className, Model model) {

        List<Student> students = studentRepository.findByClassName(className);

        model.addAttribute("students", students);
        model.addAttribute("className", className);

        return "class_students";
    }
    @GetMapping("/attendance/edit-by-class")
    public String editAttendanceByClass(@RequestParam String className, Model model) {

        List<Attendance> attendances =
                attendanceRepository.findByStudent_ClassName(className);

        model.addAttribute("attendances", attendances);
        model.addAttribute("className", className);

        return "edit_attendance_list";
    }
    @GetMapping("/attendance/edit/{id}")
    public String editAttendance(@PathVariable Long id, Model model) {

        Attendance attendance = attendanceRepository.findById(id).orElse(null);

        model.addAttribute("attendance", attendance);

        return "edit_attendance";   // ✅ must go to separate page
    }
    @PostMapping("/attendance/update/{id}")
    public String updateAttendance(@PathVariable Long id,
                                   @RequestParam String status) {

        Attendance attendance = attendanceRepository.findById(id).orElse(null);

        if (attendance != null) {
            attendance.setStatus(status);
            attendanceRepository.save(attendance);
        }

        return "redirect:/adi/attendances";
    }
    @GetMapping("/attendance/report")
    public String attendanceReport(@RequestParam(required = false) String className, Model model) {

        if (className != null) {
            List<Attendance> attendances =
                    attendanceRepository.findByStudent_ClassName(className);

            model.addAttribute("attendances", attendances);
            model.addAttribute("className", className);
        }

        return "attendance_report";
    }
    @GetMapping("/attendance/date")
    public String attendanceByDate(
            @RequestParam String date,
            Model model) {

        LocalDate selectedDate = LocalDate.parse(date);

        List<Attendance> list =
                attendanceRepository.findByDate(selectedDate);

        model.addAttribute("attendances", list);

        return "attendance_list";
    }
    
    
    @GetMapping("/attendance/pdf")
    public void downloadPdf(HttpServletResponse response) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=attendance.pdf");

        List<Attendance> list = attendanceRepository.findAll();

        Document document = new Document();

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        document.add(new Paragraph("School Attendance Report"));
        document.add(new Paragraph(" "));

        // Create table with 4 columns
        PdfPTable table = new PdfPTable(4);

        // Table headers
        table.addCell(new PdfPCell(new Phrase("Student Name")));
        table.addCell(new PdfPCell(new Phrase("Class")));
        table.addCell(new PdfPCell(new Phrase("Status")));
        table.addCell(new PdfPCell(new Phrase("Date")));

        // Table data
        for (Attendance a : list) {

            table.addCell(a.getStudent().getName());
            table.addCell(a.getStudent().getClassName());
            table.addCell(a.getStatus());
            table.addCell(a.getDate().toString());
        }

        document.add(table);

        document.close();
    }
    @GetMapping("/attendance/student-report")
    public String studentAttendanceReport(
            @RequestParam(required = false) Integer rollNumber,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {

        if (rollNumber == null || startDate == null || endDate == null) {
            return "attendance_report1";
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Student student = studentRepository.findByRollNumber(rollNumber);

        if (student == null) {
            model.addAttribute("error", "❌ Student not found!");
            return "attendance_report1";
        }

        List<Attendance> list =
                attendanceRepository.findByStudentAndDateBetween(student, start, end);

        // ✅ DEBUG FIXED
        System.out.println("Roll: " + rollNumber);
        System.out.println("Start: " + start);
        System.out.println("End: " + end);
        System.out.println("Records found: " + list.size());

        model.addAttribute("attendances", list);
        model.addAttribute("studentName", student.getName());
        model.addAttribute("rollNumber", rollNumber);

        return "attendance_report1";
    }
}
