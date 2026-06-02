package com.acc.ACC_AYCE.service;

import com.acc.ACC_AYCE.dto.EnrollmentRequest;
import com.acc.ACC_AYCE.dto.EnrollmentResponse;
import com.acc.ACC_AYCE.dto.OfflinePaymentRequest;
import com.acc.ACC_AYCE.dto.OnlinePaymentRequest;
import com.acc.ACC_AYCE.dto.PaymentResponse;
import com.acc.ACC_AYCE.Entity.Enrollment;
import com.acc.ACC_AYCE.repository.CourseRepository;
import com.acc.ACC_AYCE.repository.EnrollmentRepository;
import com.acc.ACC_AYCE.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class RegistrarEnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private PaymentService paymentService;

    // Registrar creates enrollment for student
    public EnrollmentResponse createEnrollment(EnrollmentRequest request, Long studentId) {
        try {
            // Validate studentId is not null
            if (studentId == null) {
                throw new IllegalArgumentException("Student ID cannot be null");
            }
            
            // Validate student exists
            if (!studentRepository.existsById(studentId)) {
                throw new RuntimeException("Student not found");
            }
            
            // Validate course exists
            Long courseId = request.getCourseId();
            if (courseId == null) {
                throw new IllegalArgumentException("Course ID cannot be null");
            }
            if (!courseRepository.existsById(courseId)) {
                throw new RuntimeException("Course not found");
            }
            
            // Check if already enrolled
            boolean alreadyEnrolled = enrollmentRepository.findByStudentId(studentId).stream()
                .anyMatch(enrollment -> enrollment.getCourseId().equals(request.getCourseId()) 
                    && enrollment.getEnrollmentStatus() != Enrollment.EnrollmentStatus.CANCELLED);
            
            if (alreadyEnrolled) {
                throw new RuntimeException("Student is already enrolled in this course");
            }
            
            // Create enrollment
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(studentId);
            enrollment.setCourseId(request.getCourseId());
            enrollment.setEnrollmentDate(LocalDateTime.now());
            enrollment.setEnrollmentStatus(Enrollment.EnrollmentStatus.ACTIVE); // Registrar directly activates
            enrollment.setTotalFees(request.getTotalFees() != null ? request.getTotalFees() : 10000.0);
            enrollment.setAmountPaid(0.0);
            enrollment.setRemainingBalance(enrollment.getTotalFees());
            enrollment.setPaymentType(request.getPaymentType() != null ? request.getPaymentType() : Enrollment.PaymentType.FULL_PAYMENT);
            
            Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
            
            return EnrollmentResponse.fromEntity(savedEnrollment, null);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create enrollment: " + e.getMessage());
        }
    }
    
    // Registrar creates enrollment with online payment
    public EnrollmentResponse createEnrollmentWithOnlinePayment(OnlinePaymentRequest paymentRequest, Long studentId, Long courseId) {
        try {
            // Validate parameters are not null
            if (studentId == null) {
                throw new IllegalArgumentException("Student ID cannot be null");
            }
            if (courseId == null) {
                throw new IllegalArgumentException("Course ID cannot be null");
            }
            if (paymentRequest == null) {
                throw new IllegalArgumentException("Payment request cannot be null");
            }
            
            // First create enrollment
            EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
            enrollmentRequest.setCourseId(courseId);
            enrollmentRequest.setTotalFees(paymentRequest.getAmount());
            enrollmentRequest.setPaymentType(Enrollment.PaymentType.FULL_PAYMENT);
            enrollmentRequest.setOnlinePayment(true);
            
            EnrollmentResponse enrollmentResponse = createEnrollment(enrollmentRequest, studentId);
            
            // Then initiate payment
            PaymentResponse paymentResponse = paymentService.initiateOnlinePayment(paymentRequest);
            
            enrollmentResponse.setPaymentGatewayUrl(paymentResponse.getPaymentGatewayUrl());
            enrollmentResponse.setMessage("Enrollment created and payment initiated");
            
            return enrollmentResponse;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create enrollment with online payment: " + e.getMessage());
        }
    }
    
    // Registrar creates enrollment with offline payment
    public EnrollmentResponse createEnrollmentWithOfflinePayment(OfflinePaymentRequest paymentRequest, Long studentId, Long courseId) {
        try {
            // Validate parameters are not null
            if (studentId == null) {
                throw new IllegalArgumentException("Student ID cannot be null");
            }
            if (courseId == null) {
                throw new IllegalArgumentException("Course ID cannot be null");
            }
            if (paymentRequest == null) {
                throw new IllegalArgumentException("Payment request cannot be null");
            }
            
            // First create enrollment
            EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
            enrollmentRequest.setCourseId(courseId);
            enrollmentRequest.setTotalFees(paymentRequest.getAmountPaid());
            enrollmentRequest.setPaymentType(Enrollment.PaymentType.FULL_PAYMENT);
            enrollmentRequest.setOnlinePayment(false);
            
            EnrollmentResponse enrollmentResponse = createEnrollment(enrollmentRequest, studentId);
            
            // Then create payment record
            paymentRequest.setStudentId(studentId);
            paymentRequest.setEnrollmentId(enrollmentResponse.getEnrollmentId());
            PaymentResponse paymentResponse = paymentService.createOfflinePayment(paymentRequest);
            
            enrollmentResponse.setMessage("Enrollment created with offline payment - pending approval. Payment ID: " + paymentResponse.getPaymentId());
            
            return enrollmentResponse;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create enrollment with offline payment: " + e.getMessage());
        }
    }
    
    // Update enrollment status
    public EnrollmentResponse updateEnrollmentStatus(Long enrollmentId, Enrollment.EnrollmentStatus newStatus) {
        try {
            // Validate enrollmentId is not null
            if (enrollmentId == null) {
                throw new IllegalArgumentException("Enrollment ID cannot be null");
            }
            
            Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
            
            enrollment.setEnrollmentStatus(newStatus);
            enrollmentRepository.save(enrollment);
            
            return EnrollmentResponse.fromEntity(enrollment, null);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to update enrollment status: " + e.getMessage());
        }
    }
    
    // Get all enrollments (for registrar)
    public java.util.List<EnrollmentResponse> getAllEnrollments() {
        try {
            return enrollmentRepository.findAll().stream()
                .map(enrollment -> EnrollmentResponse.fromEntity(enrollment, null))
                .collect(java.util.stream.Collectors.toList());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to get enrollments: " + e.getMessage());
        }
    }
    
    // Get enrollments by student
    public java.util.List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId) {
        try {
            // Validate studentId is not null
            if (studentId == null) {
                throw new IllegalArgumentException("Student ID cannot be null");
            }
            
            return enrollmentRepository.findByStudentId(studentId).stream()
                .map(enrollment -> EnrollmentResponse.fromEntity(enrollment, null))
                .collect(java.util.stream.Collectors.toList());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to get student enrollments: " + e.getMessage());
        }
    }
    
    // Get enrollments by course
    public java.util.List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId) {
        try {
            // Validate courseId is not null
            if (courseId == null) {
                throw new IllegalArgumentException("Course ID cannot be null");
            }
            
            return enrollmentRepository.findAll().stream()
                .filter(enrollment -> enrollment.getCourseId().equals(courseId))
                .map(enrollment -> EnrollmentResponse.fromEntity(enrollment, null))
                .collect(java.util.stream.Collectors.toList());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to get course enrollments: " + e.getMessage());
        }
    }
}
