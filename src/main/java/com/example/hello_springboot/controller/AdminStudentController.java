package com.example.hello_springboot.controller;

import com.example.hello_springboot.model.Student;
import com.example.hello_springboot.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin/students")
public class AdminStudentController {

    private final StudentRepository studentRepository;

    public AdminStudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("editingStudent", new Student());
        return "student-list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") UUID id, Model model) {
        Student student = studentRepository.findById(id).orElseGet(Student::new);
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("editingStudent", student);
        return "student-list";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("editingStudent") Student editingStudent) {
        // Nếu là sinh viên cũ (đang sửa) nhưng id bị rỗng thì giữ nguyên,
        // nếu là sinh viên mới thì nếu id chưa có, gán ngẫu nhiên UUID
        if (editingStudent.getId() == null) {
            editingStudent.setId(UUID.randomUUID());
        }
        studentRepository.save(editingStudent);
        return "redirect:/admin/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
        studentRepository.deleteById(id);
        return "redirect:/admin/students";
    }
}