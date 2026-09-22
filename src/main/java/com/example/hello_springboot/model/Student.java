package com.example.hello_springboot.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Bổ sung @Column ánh xạ đúng với tên cột student_code trong Database
    @Column(name = "student_code", nullable = false)
    private String masv;

    @Column(name = "ho_ten")
    private String hoTen;

    private String email;

    // --- Các Getter & Setter giữ nguyên ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getMasv() { return masv; }
    public void setMasv(String masv) { this.masv = masv; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}