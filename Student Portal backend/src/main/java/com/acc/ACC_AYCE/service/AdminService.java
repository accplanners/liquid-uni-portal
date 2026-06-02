package com.acc.ACC_AYCE.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.acc.ACC_AYCE.Entity.Course;
import com.acc.ACC_AYCE.Entity.Student;
import com.acc.ACC_AYCE.Entity.Faculty;
import com.acc.ACC_AYCE.repository.StudentRepository;
import com.acc.ACC_AYCE.repository.FacultyRepository;
import com.acc.ACC_AYCE.repository.CourseRepository;

@Service
public class AdminService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    

    public AdminService(StudentRepository studentRepository,
                        FacultyRepository facultyRepository,
                        CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
    }

    public Student addStudent(@NonNull Student student){
        return studentRepository.save(student);
    }

    public Faculty addFaculty(@NonNull Faculty faculty){
        return facultyRepository.save(faculty);
    }

    public @NonNull Course addCourse(@NonNull Course course){
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

	public List<Student> getAllStudents() {
		 return studentRepository.findAll();
	}

	 public Student saveStudent(@NonNull  Student student) {
	        return studentRepository.save(student);
	    }

    public StudentRepository getStudentRepository() {
        return studentRepository;
    }
}