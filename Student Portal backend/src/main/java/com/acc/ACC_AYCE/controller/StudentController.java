package com.acc.ACC_AYCE.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;

import com.acc.ACC_AYCE.Entity.Enrollment;
import com.acc.ACC_AYCE.Entity.Student;
import com.acc.ACC_AYCE.service.AdminService;
import com.acc.ACC_AYCE.service.AuthService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
    private AdminService adminService;

    @Autowired
    private AuthService authService;
    
    @PostMapping("/add")
    public Student addStudent(@RequestBody Student student) {
        if (student.getStatus() == null) {
            student.setStatus("Active");
        }
        return adminService.saveStudent(student);
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return adminService.getAllStudents();
    }

    @GetMapping("/api/student/email/{email}")
    public Student getStudentByEmail(@PathVariable String email) {
        return adminService.getStudentRepository().findByEmail(email).orElse(null);
    }

    @GetMapping("/api/student/name/{name}")
    public List<Student> getStudentsByName(@PathVariable String name) {
        return adminService.getStudentRepository().findByName(name);
    }

    @PostMapping("/registerCourse")
    public Enrollment registerCourse(@RequestBody @NonNull Enrollment enrollment){
        return authService.registerCourse(enrollment);
    }

    @PutMapping("/payFees/{id}")
    public String payFees(@PathVariable @NonNull Long id){
        return authService.payFees(id);
    }

    @GetMapping("/reportCard")
    public List<Enrollment> reportCard(@RequestParam Long studentId){
        return authService.viewReportCard(studentId);
    }
}