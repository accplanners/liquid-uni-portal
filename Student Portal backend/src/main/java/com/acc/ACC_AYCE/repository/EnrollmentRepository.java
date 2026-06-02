package com.acc.ACC_AYCE.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acc.ACC_AYCE.Entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
}
