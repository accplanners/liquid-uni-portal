package com.acc.ACC_AYCE.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

import com.acc.ACC_AYCE.Entity.Enrollment;
import com.acc.ACC_AYCE.repository.EnrollmentRepository;
import com.acc.ACC_AYCE.repository.StudentRepository;

@Service
public class AuthService {

	private final EnrollmentRepository enrollmentRepository;
	private final StudentRepository studentRepository;

	public AuthService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository) {
		this.enrollmentRepository = enrollmentRepository;
		this.studentRepository = studentRepository;
	}

	public String login(String email, String password) {
		// Placeholder login implementation for now.
		return studentRepository.findByEmail(email) != null ? "Login successful" : "Invalid credentials";
	}

	public @NonNull Enrollment registerCourse(@NonNull Enrollment enrollment) {
		return enrollmentRepository.save(enrollment);
	}

	public String payFees(@NonNull Long id) {
		return enrollmentRepository.findById(id).map(enrollment -> {
			enrollment.setFeePaid(true);
			enrollmentRepository.save(enrollment);
			return "Fees paid successfully";
		}).orElse("Enrollment not found");
	}

	public List<Enrollment> viewReportCard(Long studentId) {
		return enrollmentRepository.findByStudentId(studentId);
	}

}
