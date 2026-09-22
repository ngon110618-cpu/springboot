package com.example.hello_springboot.controller;

import com.example.hello_springboot.model.Student;
import com.example.hello_springboot.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            return ResponseEntity.ok(studentRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // Yêu cầu 4: Lấy sinh viên theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") String idStr) {
        try {
            UUID id = UUID.fromString(idStr);
            Optional<Student> studentOpt = studentRepository.findById(id);
            if (studentOpt.isPresent()) {
                return ResponseEntity.ok(studentOpt.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sinh viên có ID: " + idStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ID không đúng định dạng UUID hợp lệ!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // Yêu cầu 1: Thêm sinh viên
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            if (student == null) {
                return ResponseEntity.badRequest().body("Dữ liệu sinh viên không được để trống!");
            }
            Student savedStudent = studentRepository.save(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể thêm sinh viên: " + e.getMessage());
        }
    }

    // Yêu cầu 6: Cập nhật sinh viên theo /update/{id}
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable("id") String idStr, @RequestBody Student studentDetails) {
        try {
            UUID id = UUID.fromString(idStr);
            Optional<Student> studentOpt = studentRepository.findById(id);
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                if (studentDetails.getMasv() != null) student.setMasv(studentDetails.getMasv());
                if (studentDetails.getHoTen() != null) student.setHoTen(studentDetails.getHoTen());
                if (studentDetails.getEmail() != null) student.setEmail(studentDetails.getEmail());

                return ResponseEntity.ok(studentRepository.save(student));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sinh viên để cập nhật!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ID không đúng định dạng UUID hợp lệ!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    // Yêu cầu 2: Xóa sinh viên theo /delete/{id}
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") String idStr) {
        try {
            UUID id = UUID.fromString(idStr);
            if (!studentRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sinh viên để xóa!");
            }
            studentRepository.deleteById(id);
            return ResponseEntity.ok("Xóa sinh viên thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ID không đúng định dạng UUID hợp lệ!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sinh viên: " + e.getMessage());
        }
    }

    // Yêu cầu 3: Tìm kiếm sinh viên theo tên với /search
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        try {
            List<Student> allStudents = studentRepository.findAll();
            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.ok(allStudents);
            }

            String lowerKeyword = keyword.trim().toLowerCase();
            List<Student> result = allStudents.stream()
                    .filter(s -> s != null && s.getHoTen() != null && s.getHoTen().toLowerCase().contains(lowerKeyword))
                    .toList();

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}