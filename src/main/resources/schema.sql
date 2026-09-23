-- 1. Tạo Database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'quanlysinhvien')
BEGIN
    CREATE DATABASE quanlysinhvien;
END;
GO

USE quanlysinhvien;
GO

-- 2. Tạo Bảng Students
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'students')
BEGIN
CREATE TABLE students (
                          id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
                          student_code VARCHAR(20) NOT NULL UNIQUE,
                          ho_ten NVARCHAR(100) NOT NULL,
                          email VARCHAR(100)
);
END;
GO

-- 3. Chèn Dữ Liệu Mẫu
INSERT INTO students (id, student_code, ho_ten, email)
VALUES
    (NEWID(), 'SV001', N'Nguyễn Văn A', 'a@gmail.com'),
    (NEWID(), 'SV004', N'Nguyen Van B', 'd@gmail.com'),
    (NEWID(), 'SV005', N'Nguyen Van C', 'e@gmail.com');
GO