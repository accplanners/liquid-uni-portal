package com.acc.ACC_AYCE.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.acc.ACC_AYCE.Entity.Course;
import com.acc.ACC_AYCE.repository.CourseRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public @NonNull Course addCourse(@NonNull Course course) {
        Course savedCourse = courseRepository.save(course);
        return savedCourse;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(@NonNull Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course updateCourse(@NonNull Long id, Course updatedCourse) {
        return courseRepository.findById(id).map(course -> {
            course.setTitle(updatedCourse.getTitle());
            course.setCredits(updatedCourse.getCredits());
            course.setFaculty(updatedCourse.getFaculty());
            course.setCatalogue(updatedCourse.getCatalogue());
            return courseRepository.save(course);
        }).orElse(null);
    }

    public String deleteCourse(@NonNull Long id) {
        if (!courseRepository.existsById(id)) {
            return "Course not found";
        }
        courseRepository.deleteById(id);
        return "Course deleted successfully";
    }
}
