package com.example.hello_springboot.controller;

import com.example.hello_springboot.model.Student;
import com.example.hello_springboot.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Yêu cầu 5: Lấy tất cả sinh viên
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Yêu cầu 4: Lấy sinh viên theo ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable("id") UUID id) {
        return studentRepository.findById(id).orElse(null);
    }

    // Yêu cầu 1: Thêm sinh viên
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // Yêu cầu 6: Cập nhật sinh viên theo /update/{id}
    @PostMapping("/update/{id}")
    public Student updateStudent(@PathVariable("id") UUID id, @RequestBody Student studentDetails) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setMasv(studentDetails.getMasv());
            student.setHoTen(studentDetails.getHoTen());
            student.setEmail(studentDetails.getEmail());
            return studentRepository.save(student);
        }
        return null;
    }

    // Yêu cầu 2: Xóa sinh viên theo /delete/{id}
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") UUID id) {
        studentRepository.deleteById(id);
        return "Deleted successfully";
    }

    // Yêu cầu 3: Tìm kiếm sinh viên theo tên với /search
    @GetMapping("/search")
    public List<Student> searchStudents(@RequestParam("keyword") String keyword) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getHoTen() != null && s.getHoTen().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}