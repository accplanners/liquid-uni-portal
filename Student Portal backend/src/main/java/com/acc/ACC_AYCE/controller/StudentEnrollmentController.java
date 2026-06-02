package com.acc.ACC_AYCE.controller;

import com.acc.ACC_AYCE.dto.EnrollmentRequest;
import com.acc.ACC_AYCE.dto.EnrollmentResponse;
import com.acc.ACC_AYCE.Entity.Student;
import com.acc.ACC_AYCE.service.StudentEnrollmentService;
import com.acc.ACC_AYCE.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/enrollment")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentEnrollmentController {

    @Autowired
    private StudentEnrollmentService studentEnrollmentService;

    @Autowired
    private StudentRepository studentRepository;

    // Self-enrollment with online payment
    @PostMapping("/self-enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> selfEnroll(@Validated @RequestBody EnrollmentRequest request,
                                                       Authentication authentication) {
        try {
            // Get student ID from authentication
            Long studentId = getStudentIdFromAuthentication(authentication);
            
            EnrollmentResponse response = studentEnrollmentService.selfEnroll(request, studentId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            EnrollmentResponse errorResponse = new EnrollmentResponse();
            errorResponse.setMessage("Self-enrollment failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Get enrollment status
    @GetMapping("/status/{enrollmentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> getEnrollmentStatus(@PathVariable @NonNull Long enrollmentId,
                                                          Authentication authentication) {
        try {
            Long studentId = getStudentIdFromAuthentication(authentication);
            EnrollmentResponse response = studentEnrollmentService.getEnrollmentStatus(enrollmentId, studentId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            EnrollmentResponse errorResponse = new EnrollmentResponse();
            errorResponse.setMessage("Failed to get enrollment status: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Cancel enrollment
    @DeleteMapping("/cancel/{enrollmentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> cancelEnrollment(@PathVariable @NonNull Long enrollmentId,
                                             Authentication authentication) {
        try {
            Long studentId = getStudentIdFromAuthentication(authentication);
            studentEnrollmentService.cancelEnrollment(enrollmentId, studentId);
            return ResponseEntity.ok("Enrollment cancelled successfully");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to cancel enrollment: " + e.getMessage());
        }
    }

    // Get all enrollments for current student
    @GetMapping("/my-enrollments")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> getMyEnrollments(Authentication authentication) {
        try {
            // TODO: Implement getStudentEnrollments method in StudentEnrollmentService
            return ResponseEntity.ok("Student enrollments endpoint - to be implemented");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Helper method to extract student ID from authentication
    private @NonNull Long getStudentIdFromAuthentication(Authentication authentication) {
        try {
            // Get email from authentication
            String email = authentication.getName();
            
            // For testing, return hardcoded student IDs based on email
            if (email.equals("student@example.com")) {
                return 1L;
            } else if (email.equals("admin@example.com")) {
                return 2L;
            } else if (email.equals("registrar@example.com")) {
                return 3L;
            }
            
            // Try to find student by email (fallback)
            Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
            
            Long studentId = student.getStudentId();
            if (studentId == null) {
                throw new RuntimeException("Student ID is null for student with email: " + email);
            }
            
            return studentId;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract student ID from authentication: " + e.getMessage());
        }
    }
}
