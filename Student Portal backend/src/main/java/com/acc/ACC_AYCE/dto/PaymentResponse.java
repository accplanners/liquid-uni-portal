package com.acc.ACC_AYCE.dto;

import com.acc.ACC_AYCE.Entity.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long paymentId;
    private Long studentId;
    private Long enrollmentId;
    private String transactionId;
    private String receiptNumber;
    private LocalDateTime paymentDate;
    private Double amountPaid;
    private Double totalFees;
    private Double remainingBalance;
    private Payment.PaymentStatus paymentStatus;
    private Payment.PaymentMode paymentMode;
    private Payment.PaymentType paymentType;
    private Payment.RegistrarApprovalStatus registrarApprovalStatus;
    private Double discountAmount;
    private String scholarshipCode;
    private Integer installmentNumber;
    private Integer totalInstallments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String paymentGatewayUrl;
    private String message;

    public static PaymentResponse fromEntity(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setStudentId(payment.getStudent().getStudentId());
        response.setEnrollmentId(payment.getEnrollment().getId());
        response.setTransactionId(payment.getTransactionId());
        response.setReceiptNumber(payment.getReceiptNumber());
        response.setPaymentDate(payment.getPaymentDate());
        response.setAmountPaid(payment.getAmountPaid());
        response.setTotalFees(payment.getTotalFees());
        response.setRemainingBalance(payment.getRemainingBalance());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setPaymentMode(payment.getPaymentMode());
        response.setPaymentType(payment.getPaymentType());
        response.setRegistrarApprovalStatus(payment.getRegistrarApprovalStatus());
        response.setDiscountAmount(payment.getDiscountAmount());
        response.setScholarshipCode(payment.getScholarshipCode());
        response.setInstallmentNumber(payment.getInstallmentNumber());
        response.setTotalInstallments(payment.getTotalInstallments());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        response.setMessage("Payment processed successfully");
        return response;
    }
}
