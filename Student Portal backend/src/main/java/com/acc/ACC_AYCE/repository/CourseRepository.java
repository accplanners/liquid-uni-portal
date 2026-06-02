package com.acc.ACC_AYCE.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acc.ACC_AYCE.Entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
