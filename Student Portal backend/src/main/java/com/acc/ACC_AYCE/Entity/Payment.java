package com.acc.ACC_AYCE.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = true)
    private Enrollment enrollment;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "receipt_number", unique = true)
    private String receiptNumber;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;

    @Column(name = "total_fees", nullable = false)
    private Double totalFees;

    @Column(name = "remaining_balance", nullable = false)
    private Double remainingBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false)
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "gateway_response", columnDefinition = "TEXT")
    private String gatewayResponse;

    @Column(name = "payment_proof")
    private String paymentProof;

    @Enumerated(EnumType.STRING)
    @Column(name = "registrar_approval_status")
    private RegistrarApprovalStatus registrarApprovalStatus;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "scholarship_code")
    private String scholarshipCode;

    @Column(name = "installment_number")
    private Integer installmentNumber;

    @Column(name = "total_installments")
    private Integer totalInstallments;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        paymentDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED,
        PARTIALLY_REFUNDED,
        CANCELLED
    }

    public enum PaymentMode {
        ONLINE,
        CASH,
        UPI,
        BANK_TRANSFER,
        CHEQUE
    }

    public enum PaymentType {
        FULL_PAYMENT,
        PARTIAL_PAYMENT,
        INSTALLMENT
    }

    public enum RegistrarApprovalStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
