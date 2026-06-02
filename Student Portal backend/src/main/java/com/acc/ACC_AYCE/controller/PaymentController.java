package com.acc.ACC_AYCE.controller;

import com.acc.ACC_AYCE.dto.OfflinePaymentRequest;
import com.acc.ACC_AYCE.dto.OnlinePaymentRequest;
import com.acc.ACC_AYCE.dto.PaymentResponse;
import com.acc.ACC_AYCE.service.PaymentService;
import com.acc.ACC_AYCE.Entity.Payment;
import com.acc.ACC_AYCE.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private PaymentRepository paymentRepository;

    // Initiate online payment
    @PostMapping("/online/initiate")
    @PreAuthorize("hasAnyRole('STUDENT', 'REGISTRAR', 'ADMIN')")
    public ResponseEntity<PaymentResponse> initiateOnlinePayment(@Validated @RequestBody OnlinePaymentRequest request) {
        try {
            PaymentResponse response = paymentService.initiateOnlinePayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setMessage("Failed to initiate online payment: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Create offline payment
    @PostMapping("/offline/create")
    @PreAuthorize("hasAnyRole('REGISTRAR', 'ADMIN')")
    public ResponseEntity<PaymentResponse> createOfflinePayment(@Validated @RequestBody OfflinePaymentRequest request) {
        try {
            PaymentResponse response = paymentService.createOfflinePayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setMessage("Failed to create offline payment: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Verify online payment
    @PostMapping("/online/verify")
    @PreAuthorize("hasAnyRole('STUDENT', 'REGISTRAR', 'ADMIN')")
    public ResponseEntity<PaymentResponse> verifyOnlinePayment(@RequestBody Map<String, String> verificationData) {
        try {
            String paymentId = verificationData.get("paymentId");
            String orderId = verificationData.get("orderId");
            String signature = verificationData.get("signature");
            
            PaymentResponse response = paymentService.verifyOnlinePayment(paymentId, orderId, signature);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setMessage("Failed to verify online payment: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Process webhook (for payment gateway callbacks)
    @PostMapping("/webhook")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processWebhook(@RequestBody String webhookData) {
        try {
            PaymentResponse response = paymentService.processWebhook(webhookData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setMessage("Failed to process webhook: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Get payment by ID
    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'REGISTRAR', 'ADMIN')")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable @NonNull Long paymentId) {
        try {
            PaymentResponse response = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setMessage("Failed to get payment: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Update payment status
    @PutMapping("/{paymentId}/status")
    @PreAuthorize("hasAnyRole('REGISTRAR', 'ADMIN')")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(@PathVariable @NonNull Long paymentId, @RequestBody Map<String, String> statusData) {
        try {
            String status = statusData.get("status");
            PaymentResponse response = paymentService.updatePaymentStatus(paymentId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setMessage("Failed to update payment status: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Get all payments (admin only)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAllPayments() {
        try {
            List<Payment> payments = paymentRepository.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("payments", payments);
            response.put("count", payments.size());
            response.put("message", "All payments retrieved successfully");
            response.put("status", "success");
            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get all payments: " + e.getMessage());
        }
    }

    // Get payments by student ID
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'REGISTRAR', 'ADMIN')")
    public ResponseEntity<String> getPaymentsByStudentId(@PathVariable Long studentId) {
        try {
            List<Payment> payments = paymentRepository.findByStudent_StudentId(studentId);
            Map<String, Object> response = new HashMap<>();
            response.put("studentId", studentId);
            response.put("payments", payments);
            response.put("count", payments.size());
            response.put("message", "Student payments retrieved successfully");
            response.put("status", "success");
            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get student payments: " + e.getMessage());
        }
    }
}
