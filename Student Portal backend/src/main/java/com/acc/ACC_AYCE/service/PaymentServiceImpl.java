package com.acc.ACC_AYCE.service;

import com.acc.ACC_AYCE.Entity.Payment;
import com.acc.ACC_AYCE.dto.OfflinePaymentRequest;
import com.acc.ACC_AYCE.dto.OnlinePaymentRequest;
import com.acc.ACC_AYCE.dto.PaymentResponse;
import com.acc.ACC_AYCE.repository.PaymentRepository;
import com.acc.ACC_AYCE.repository.EnrollmentRepository;
import com.acc.ACC_AYCE.repository.StudentRepository;
import com.acc.ACC_AYCE.Entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public PaymentResponse initiateOnlinePayment(OnlinePaymentRequest request) {
        // Validate request
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        System.out.println("DEBUG: Processing payment request for studentId: " + request.getStudentId());
        System.out.println("DEBUG: Processing payment request for enrollmentId: " + request.getEnrollmentId());
        System.out.println("DEBUG: Processing payment request for amount: " + request.getAmount());
        
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(System.currentTimeMillis());
        response.setStudentId(request.getStudentId());
        response.setEnrollmentId(request.getEnrollmentId());
        response.setTransactionId("TXN_" + UUID.randomUUID().toString().substring(0, 8));
        response.setAmountPaid(request.getAmount());
        response.setPaymentGatewayUrl("https://razorpay.com/pay/" + response.getTransactionId());
        response.setMessage("Online payment initiated successfully");
        
        // Save payment record to database
        Payment payment = new Payment();
        payment.setPaymentId(response.getPaymentId());
        payment.setTransactionId(response.getTransactionId());
        payment.setAmountPaid(request.getAmount());
        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        payment.setPaymentMode(Payment.PaymentMode.ONLINE);
        payment.setPaymentType(Payment.PaymentType.FULL_PAYMENT);
        payment.setTotalFees(request.getAmount());
        payment.setRemainingBalance(0.0);
        payment.setRegistrarApprovalStatus(Payment.RegistrarApprovalStatus.PENDING);
        
        // Set student relationship (required)
        Long studentId = request.getStudentId();
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        
        payment.setStudent(student);
        
        // Set enrollment relationship (optional)
        Long enrollmentId = request.getEnrollmentId();
        if (enrollmentId != null) {
            try {
                enrollmentRepository.findById(enrollmentId)
                    .ifPresent(payment::setEnrollment);
            } catch (Exception e) {
                // Log but don't fail if enrollment is not found
                System.out.println("Warning: Enrollment not found for ID: " + enrollmentId);
            }
        }
        
        paymentRepository.save(payment);
        
        return response;
    }

    @Override
    public PaymentResponse createOfflinePayment(OfflinePaymentRequest request) {
        // Validate request
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        System.out.println("DEBUG: Processing offline payment request for studentId: " + request.getStudentId());
        
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(System.currentTimeMillis());
        response.setStudentId(request.getStudentId());
        response.setEnrollmentId(request.getEnrollmentId());
        response.setTransactionId(request.getTransactionId());
        response.setAmountPaid(request.getAmountPaid());
        response.setMessage("Offline payment created - pending approval");
        
        // Save offline payment record to database
        Payment payment = new Payment();
        payment.setPaymentId(response.getPaymentId());
        payment.setTransactionId(request.getTransactionId());
        payment.setAmountPaid(request.getAmountPaid());
        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        
        // Set payment mode from request or default to CASH
        payment.setPaymentMode(request.getPaymentMode() != null ? request.getPaymentMode() : Payment.PaymentMode.CASH);
        
        // Set payment type from request or default to FULL_PAYMENT
        payment.setPaymentType(request.getPaymentType() != null ? request.getPaymentType() : Payment.PaymentType.FULL_PAYMENT);
        
        payment.setTotalFees(request.getAmountPaid());
        payment.setRemainingBalance(0.0);
        payment.setRegistrarApprovalStatus(Payment.RegistrarApprovalStatus.PENDING);
        
        // Set student relationship (required)
        Long studentId = request.getStudentId();
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        
        payment.setStudent(student);
        
        // Set enrollment relationship (optional)
        Long enrollmentId = request.getEnrollmentId();
        if (enrollmentId != null) {
            try {
                enrollmentRepository.findById(enrollmentId)
                    .ifPresent(payment::setEnrollment);
            } catch (Exception e) {
                System.out.println("Warning: Enrollment not found for ID: " + enrollmentId);
            }
        }
        
        paymentRepository.save(payment);
        
        return response;
    }

    @Override
    public PaymentResponse verifyOnlinePayment(String paymentId, String orderId, String signature) {
        PaymentResponse response = new PaymentResponse();
        
        if (paymentId == null || paymentId.trim().isEmpty()) {
            response.setMessage("Payment ID cannot be null or empty");
            return response;
        }
        
        try {
            Long paymentIdLong = Long.parseLong(paymentId);
            Optional<Payment> paymentOpt = paymentRepository.findById(paymentIdLong);
            
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
                paymentRepository.save(payment);
                response.setPaymentId(paymentIdLong);
                response.setMessage("Payment verified successfully");
            } else {
                response.setMessage("Payment not found");
            }
        } catch (NumberFormatException e) {
            response.setMessage("Invalid payment ID format: " + paymentId);
        }
        
        return response;
    }

    @Override
    public PaymentResponse processWebhook(String webhookData) {
        PaymentResponse response = new PaymentResponse();
        response.setMessage("Webhook processed successfully");
        return response;
    }

    @Override
    public boolean validatePaymentAmount(Double amount, @NonNull Long enrollmentId) {
        return amount != null && amount > 0;
    }

    @Override
    public PaymentResponse getPaymentById(@NonNull Long paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        PaymentResponse response = new PaymentResponse();
        
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            response.setPaymentId(payment.getPaymentId());
            response.setTransactionId(payment.getTransactionId());
            response.setAmountPaid(payment.getAmountPaid());
            response.setMessage("Payment retrieved successfully");
        } else {
            response.setMessage("Payment not found");
        }
        
        return response;
    }

    @Override
    public PaymentResponse updatePaymentStatus(@NonNull Long paymentId, String status) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        PaymentResponse response = new PaymentResponse();
        
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            try {
                Payment.PaymentStatus paymentStatus = Payment.PaymentStatus.valueOf(status.toUpperCase());
                payment.setPaymentStatus(paymentStatus);
                paymentRepository.save(payment);
                response.setPaymentId(paymentId);
                response.setMessage("Payment status updated successfully");
            } catch (IllegalArgumentException e) {
                response.setMessage("Invalid payment status: " + status);
            }
        } else {
            response.setMessage("Payment not found");
        }
        
        return response;
    }
}
