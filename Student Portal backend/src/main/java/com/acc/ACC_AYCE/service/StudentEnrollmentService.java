package com.acc.ACC_AYCE.service;

import com.acc.ACC_AYCE.dto.EnrollmentRequest;
import com.acc.ACC_AYCE.dto.EnrollmentResponse;
import com.acc.ACC_AYCE.dto.OnlinePaymentRequest;
import com.acc.ACC_AYCE.dto.PaymentResponse;
import com.acc.ACC_AYCE.Entity.Enrollment;
import com.acc.ACC_AYCE.Entity.Enrollment.PaymentType;
import com.acc.ACC_AYCE.Entity.Student;
import com.acc.ACC_AYCE.repository.CourseRepository;
import com.acc.ACC_AYCE.repository.EnrollmentRepository;
import com.acc.ACC_AYCE.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class StudentEnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private PaymentService paymentService;

    public EnrollmentResponse selfEnroll(EnrollmentRequest request, @NonNull Long studentId) {
        try {
            // Validate student exists
            studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
            
            // Validate course exists
            Long courseId = request.getCourseId();
            if (courseId == null) {
                throw new RuntimeException("Course ID cannot be null");
            }
            courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
            
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
            enrollment.setEnrollmentStatus(Enrollment.EnrollmentStatus.PENDING);
            enrollment.setTotalFees(request.getTotalFees() != null ? request.getTotalFees() : 10000.0);
            enrollment.setAmountPaid(0.0);
            enrollment.setRemainingBalance(enrollment.getTotalFees());
            enrollment.setPaymentType(request.getPaymentType() != null ? request.getPaymentType() : PaymentType.FULL_PAYMENT);
            
            Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
            
            // If online payment is requested, initiate payment
            if (request.getPaymentType() == PaymentType.FULL_PAYMENT && request.isOnlinePayment()) {
                OnlinePaymentRequest paymentRequest = new OnlinePaymentRequest();
                paymentRequest.setStudentId(studentId);
                paymentRequest.setEnrollmentId(savedEnrollment.getId());
                paymentRequest.setAmount(enrollment.getTotalFees());
                // Get student details for payment
                Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
                paymentRequest.setEmail(student.getEmail());
                paymentRequest.setContact(student.getStudentCode()); // Using studentCode as contact
                
                PaymentResponse paymentResponse = paymentService.initiateOnlinePayment(paymentRequest);
                
                return EnrollmentResponse.fromEntity(savedEnrollment, paymentResponse.getPaymentGatewayUrl());
            }
            
            return EnrollmentResponse.fromEntity(savedEnrollment, null);
            
        } catch (Exception e) {
            throw new RuntimeException("Self-enrollment failed: " + e.getMessage());
        }
    }
    
    public EnrollmentResponse getEnrollmentStatus(@NonNull Long enrollmentId, @NonNull Long studentId) {
        try {
            Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
            
            // Verify the enrollment belongs to the requesting student
            if (!enrollment.getStudentId().equals(studentId)) {
                throw new RuntimeException("Access denied: Enrollment does not belong to this student");
            }
            
            return EnrollmentResponse.fromEntity(enrollment, null);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to get enrollment status: " + e.getMessage());
        }
    }
    
    public void cancelEnrollment(@NonNull Long enrollmentId, @NonNull Long studentId) {
        try {
            Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
            
            // Verify the enrollment belongs to the requesting student
            if (!enrollment.getStudentId().equals(studentId)) {
                throw new RuntimeException("Access denied: Enrollment does not belong to this student");
            }
            
            // Only allow cancellation if payment is not completed
            if (enrollment.isFeePaid()) {
                throw new RuntimeException("Cannot cancel enrollment: Fees already paid");
            }
            
            enrollment.setEnrollmentStatus(Enrollment.EnrollmentStatus.CANCELLED);
            enrollmentRepository.save(enrollment);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel enrollment: " + e.getMessage());
        }
    }
}
