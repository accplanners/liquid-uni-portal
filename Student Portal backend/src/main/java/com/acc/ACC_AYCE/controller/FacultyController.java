package com.acc.ACC_AYCE.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.acc.ACC_AYCE.Entity.Enrollment;
import com.acc.ACC_AYCE.Entity.Faculty;
import com.acc.ACC_AYCE.service.FacultyService;
import com.acc.ACC_AYCE.repository.FacultyRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private FacultyRepository facultyRepository;

    @PostMapping("/api/faculty/add-grade/{id}")
    public Enrollment addGrade(@PathVariable @NonNull Long id, @RequestBody String grade) {
        return facultyService.addGrade(id, grade);
    }

    @GetMapping("")
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable @NonNull Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
    }

    @PostMapping("")
    public Faculty createFaculty(@RequestBody @NonNull Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable @NonNull Long id, @RequestBody @NonNull Faculty faculty) {
        faculty.setFacultyId(id);
        return facultyRepository.save(faculty);
    }

    @DeleteMapping("/{id}")
    public String deleteFaculty(@PathVariable @NonNull Long id) {
        facultyRepository.deleteById(id);
        return "Faculty deleted successfully";
    }
}