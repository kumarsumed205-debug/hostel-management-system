CREATE DATABASE IF NOT EXISTS smart_hostel;
USE smart_hostel;

CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    course VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL,
    student_id VARCHAR(20) NULL,
    CONSTRAINT fk_user_student FOREIGN KEY (student_id)
        REFERENCES students(student_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(20) NOT NULL UNIQUE,
    block_name VARCHAR(20) NOT NULL,
    capacity INT NOT NULL,
    occupied INT NOT NULL DEFAULT 0,
    room_type VARCHAR(30) NOT NULL DEFAULT '2-Seater',
    status ENUM('AVAILABLE', 'PARTIALLY_OCCUPIED', 'FULL', 'MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE'
);

CREATE TABLE IF NOT EXISTS allocations (
    allocation_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL UNIQUE,
    room_id INT NOT NULL,
    allocation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_allocation_student FOREIGN KEY (student_id)
        REFERENCES students(student_id) ON DELETE CASCADE,
    CONSTRAINT fk_allocation_room FOREIGN KEY (room_id)
        REFERENCES rooms(room_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS complaints (
    complaint_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    category VARCHAR(30) NOT NULL,
    description VARCHAR(500) NOT NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL,
    status ENUM('PENDING', 'IN_PROGRESS', 'RESOLVED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    CONSTRAINT fk_complaint_student FOREIGN KEY (student_id)
        REFERENCES students(student_id) ON DELETE CASCADE
);

INSERT INTO students(student_id, name, course, year, phone, email)
VALUES ('STU001', 'Demo Student', 'CSE-AIML', 2, '9876543210', 'student@example.com')
ON DUPLICATE KEY UPDATE name = VALUES(name), course = VALUES(course), year = VALUES(year), phone = VALUES(phone), email = VALUES(email);

INSERT INTO rooms(room_number, block_name, capacity, occupied, room_type, status) VALUES
('A101', 'A', 2, 0, '2-Seater', 'AVAILABLE'),
('A102', 'A', 2, 1, '2-Seater', 'PARTIALLY_OCCUPIED'),
('A103', 'A', 2, 2, '2-Seater', 'FULL'),
('B201', 'B', 3, 1, '3-Seater', 'PARTIALLY_OCCUPIED'),
('B202', 'B', 3, 0, '3-Seater', 'AVAILABLE'),
('C301', 'C', 2, 0, '2-Seater', 'AVAILABLE')
ON DUPLICATE KEY UPDATE room_number = VALUES(room_number);

INSERT INTO users(username, password_hash, role, student_id)
VALUES
('admin', SHA2('admin123', 256), 'ADMIN', NULL),
('student', SHA2('student123', 256), 'STUDENT', 'STU001')
ON DUPLICATE KEY UPDATE password_hash = VALUES(password_hash), role = VALUES(role), student_id = VALUES(student_id);
