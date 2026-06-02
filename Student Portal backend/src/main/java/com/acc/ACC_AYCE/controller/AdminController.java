package com.acc.ACC_AYCE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.acc.ACC_AYCE.Entity.Course;
import com.acc.ACC_AYCE.Entity.Faculty;
import com.acc.ACC_AYCE.Entity.Student;
import com.acc.ACC_AYCE.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/student")
    public @NonNull Student addStudent(@RequestBody @NonNull Student student) {
        Student result = adminService.addStudent(student);
        if (result == null) {
            throw new IllegalStateException("Failed to save student");
        }
        return result;
    }

    @GetMapping("/student")
    public List<Student> getStudents() {
        return adminService.getAllStudents();
    }

    @PostMapping("/addProfessor")
    public @NonNull Faculty addProfessor(@RequestBody @NonNull Faculty faculty){

        Faculty result = adminService.addFaculty(faculty);
        if (result == null) {
            throw new IllegalStateException("Failed to save faculty");
        }
        return result;
    }

    @PostMapping("/addCourse")
    public @NonNull Course addCourse(@RequestBody @NonNull Course course){

        return adminService.addCourse(course);
    }
}
