package com.acc.ACC_AYCE.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "enrollment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "fee_paid")
    private boolean feePaid;

    @Column(name = "grade")
    private String grade;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;

    @Column(name = "total_fees")
    private Double totalFees;

    @Column(name = "amount_paid")
    private Double amountPaid;

    @Column(name = "remaining_balance")
    private Double remainingBalance;

    @Column(name = "enrollment_status")
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus enrollmentStatus;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "installment_plan")
    private Integer installmentPlan;

    @Column(name = "next_payment_due")
    private LocalDateTime nextPaymentDue;

    @Column(name = "scholarship_applied")
    private String scholarshipApplied;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    @PrePersist
    protected void onCreate() {
        if (enrollmentDate == null) {
            enrollmentDate = LocalDateTime.now();
        }
        if (enrollmentStatus == null) {
            enrollmentStatus = EnrollmentStatus.PENDING;
        }
        if (totalFees == null) {
            totalFees = 10000.0; // Default fee
        }
        if (amountPaid == null) {
            amountPaid = 0.0;
        }
        if (remainingBalance == null) {
            remainingBalance = totalFees - amountPaid;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Update remaining balance when amount paid changes
        if (totalFees != null && amountPaid != null) {
            remainingBalance = totalFees - amountPaid;
        }
        
        // Update fee paid status
        if (remainingBalance != null && remainingBalance <= 0) {
            feePaid = true;
            if (enrollmentStatus == EnrollmentStatus.PENDING) {
                enrollmentStatus = EnrollmentStatus.ACTIVE;
            }
        }
    }

    public enum EnrollmentStatus {
        PENDING,
        ACTIVE,
        SUSPENDED,
        COMPLETED,
        CANCELLED
    }

    public enum PaymentType {
        FULL_PAYMENT,
        PARTIAL_PAYMENT,
        INSTALLMENT
    }

    // Explicit getters to avoid Lombok compilation issues
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}