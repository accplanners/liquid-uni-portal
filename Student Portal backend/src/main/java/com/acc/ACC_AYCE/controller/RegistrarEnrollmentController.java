package com.acc.ACC_AYCE.controller;

import com.acc.ACC_AYCE.dto.EnrollmentRequest;
import com.acc.ACC_AYCE.dto.EnrollmentResponse;
import com.acc.ACC_AYCE.dto.OfflinePaymentRequest;
import com.acc.ACC_AYCE.dto.OnlinePaymentRequest;
import com.acc.ACC_AYCE.service.RegistrarEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrar/enrollment")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrarEnrollmentController {

    @Autowired
    private RegistrarEnrollmentService registrarEnrollmentService;

    // Create enrollment (registrar creates for student)
    @PostMapping("/create/{studentId}")
    @PreAuthorize("hasAnyRole('REGISTRAR', 'ADMIN')")
    public ResponseEntity<EnrollmentResponse> createEnrollment(@PathVariable Long studentId,
                                                         @Validated @RequestBody EnrollmentRequest request) {
        try {
            EnrollmentResponse response = registrarEnrollmentService.createEnrollment(request, studentId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            EnrollmentResponse errorResponse = new EnrollmentResponse();
            errorResponse.setMessage("Failed to create enrollment: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Create enrollment with online payment
    @PostMapping("/create-with-online-payment/{studentId}")
    @PreAuthorize("hasAnyRole('REGISTRAR', 'ADMIN')")
    public ResponseEntity<EnrollmentResponse> createEnrollmentWithOnlinePayment(
            @PathVariable Long studentId,
            @Validated @RequestBody OnlinePaymentRequest paymentRequest,
            @RequestParam Long courseId) {
        try {
            EnrollmentResponse response = registrarEnrollmentService.createEnrollmentWithOnlinePayment(
                paymentRequest, studentId, courseId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            EnrollmentResponse errorResponse = new EnrollmentResponse();
            errorResponse.setMessage("Failed to create enrollment with online payment: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Create enrollment with offline payment
    @PostMapping("/create-with-offline-payment/{studentId}")
    @PreAuthorize("hasAnyRole('REGISTRAR', 'ADMIN')")
    public ResponseEntity<EnrollmentResponse> createEnrollmentWithOfflinePayment(
            @PathVariable Long studentId,
            @Validated @RequestBody OfflinePaymentRequest paymentRequest,
            @RequestParam Long courseId) {
        try {
            EnrollmentResponse response = registrarEnrollmentService.createEnrollmentWithOfflinePayment(
                paymentRequest, studentId, courseId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            EnrollmentResponse errorResponse = new EnrollmentResponse();
            errorResponse.setMessage("Failed to create enrollment with offline payment: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Update enrollment status
    @PutMapping("/update-status/{enrollmentId}")
    @PreAuthorize("hasAnyRole('REGISTRAR', 'ADMIN')")
    public ResponseEntity<EnrollmentResponse> updateEnrollmentStatus(
            @PathVariable Long enrollmentId,
            @RequestParam com.acc.ACC_AYCE.Entity.Enrollment.EnrollmentStatus status) {
        try {
            EnrollmentResponse response = registrarEnrollmentService.updateEnrollmentStatus(enrollmentId, status);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            EnrollmentResponse errorResponse = new EnrollmentResponse();
            errorResponse.setMessage("Failed to update enrollment status: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Get all enrollments
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('REGISTRAR', 'ADMIN')")
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments() {
        try {
            List<EnrollmentResponse> enrollments = registrarEnrollmentService.getAllEnrollments();
            return ResponseEntity.ok(enrollments);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get enrollments by student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('REGISTRAR', 'ADMIN')")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        try {
            List<EnrollmentResponse> enrollments = registrarEnrollmentService.getEnrollmentsByStudent(studentId);
            return ResponseEntity.ok(enrollments);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get enrollments by course
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('REGISTRAR', 'ADMIN')")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        try {
            List<EnrollmentResponse> enrollments = registrarEnrollmentService.getEnrollmentsByCourse(courseId);
            return ResponseEntity.ok(enrollments);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
