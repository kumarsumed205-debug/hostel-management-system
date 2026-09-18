package com.smarthostel.service;

import com.smarthostel.dao.StudentDAO;
import com.smarthostel.model.Student;
import com.smarthostel.util.Validator;

import java.util.List;

public class StudentService {
    private final StudentDAO dao = new StudentDAO();

    public List<Student> getAll() throws Exception { return dao.findAll(); }

    public void add(String id, String name, String course, String year, String phone, String email) throws Exception {
        Validator.student(id, name, course, year, phone, email);
        if (dao.findById(id.trim()) != null) throw new IllegalArgumentException("Student ID already exists.");
        dao.insert(new Student(id.trim(), name.trim(), course.trim(), Integer.parseInt(year), phone.trim(), email.trim()));
    }

    public void update(String id, String name, String course, String year, String phone, String email) throws Exception {
        Validator.student(id, name, course, year, phone, email);
        if (dao.findById(id.trim()) == null) throw new IllegalArgumentException("Student not found.");
        dao.update(new Student(id.trim(), name.trim(), course.trim(), Integer.parseInt(year), phone.trim(), email.trim()));
    }

    public void delete(String id) throws Exception {
        Validator.requireText(id, "Student ID");
        if (dao.findById(id.trim()) == null) throw new IllegalArgumentException("Student not found.");
        dao.delete(id.trim());
    }
}
