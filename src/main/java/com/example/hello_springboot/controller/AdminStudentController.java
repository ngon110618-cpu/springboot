package com.example.hello_springboot.controller;

import com.example.hello_springboot.model.Student;
import com.example.hello_springboot.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/students")
public class AdminStudentController {

    private final StudentRepository studentRepository;

    public AdminStudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String listStudents(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Student> students;
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lower = keyword.trim().toLowerCase();
            students = studentRepository.findAll().stream()
                    .filter(s -> (s.getHoTen() != null && s.getHoTen().toLowerCase().contains(lower)) ||
                            (s.getMasv() != null && s.getMasv().toLowerCase().contains(lower)))
                    .toList();
        } else {
            students = studentRepository.findAll();
        }
        model.addAttribute("students", students);
        model.addAttribute("editingStudent", new Student());
        model.addAttribute("keyword", keyword);
        return "student-list";
    }

    // Xử lý lưu an toàn: Hứng từng RequestParam để tránh lỗi ép kiểu UUID
    @PostMapping("/save")
    public String saveStudent(@RequestParam(value = "id", required = false) String idStr,
                              @RequestParam(value = "masv", required = false) String masv,
                              @RequestParam(value = "hoTen", required = false) String hoTen,
                              @RequestParam(value = "email", required = false) String email) {
        try {
            Student student;
            // Nếu idStr có giá trị thực sự -> Đây là thao tác SỬA
            if (idStr != null && !idStr.trim().isEmpty()) {
                UUID id = UUID.fromString(idStr.trim());
                student = studentRepository.findById(id).orElse(new Student());
            } else {
                // Nếu idStr rỗng -> Đây là THÊM MỚI (để id = null để Hibernate tự tạo UUID)
                student = new Student();
            }

            student.setMasv(masv);
            student.setHoTen(hoTen);
            student.setEmail(email);

            studentRepository.save(student);
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu sinh viên: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/admin/students";
    }

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable("id") String idStr, Model model) {
        try {
            UUID id = UUID.fromString(idStr);
            Student student = studentRepository.findById(id).orElse(new Student());
            model.addAttribute("editingStudent", student);
            model.addAttribute("students", studentRepository.findAll());
        } catch (Exception e) {
            return "redirect:/admin/students";
        }
        return "student-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") String idStr) {
        try {
            UUID id = UUID.fromString(idStr);
            studentRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/students";
    }
}