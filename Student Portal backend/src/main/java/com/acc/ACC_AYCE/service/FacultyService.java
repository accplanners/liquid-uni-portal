package com.acc.ACC_AYCE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.acc.ACC_AYCE.Entity.Enrollment;
import com.acc.ACC_AYCE.repository.EnrollmentRepository;

@Service
public class FacultyService {

	@Autowired
	private EnrollmentRepository enrollmentRepository;

	public Enrollment addGrade(@NonNull Long id, String grade) {

		Enrollment enrollment = enrollmentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Enrollment not found with id: " + id));
		enrollment.setGrade(grade);

		return enrollmentRepository.save(enrollment);
	}
}
