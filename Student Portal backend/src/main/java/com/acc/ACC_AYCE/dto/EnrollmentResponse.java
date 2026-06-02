package com.acc.ACC_AYCE.dto;

import com.acc.ACC_AYCE.Entity.Enrollment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {

    private Long enrollmentId;
    private Long studentId;
    private Long courseId;
    private LocalDateTime enrollmentDate;
    private Enrollment.EnrollmentStatus enrollmentStatus;
    private Double totalFees;
    private Double amountPaid;
    private Double remainingBalance;
    private Enrollment.PaymentType paymentType;
    private Integer installmentPlan;
    private LocalDateTime nextPaymentDue;
    private String scholarshipApplied;
    private Double discountAmount;
    private String paymentGatewayUrl;
    private String message;

    public static EnrollmentResponse fromEntity(Enrollment enrollment, String paymentGatewayUrl) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setEnrollmentId(enrollment.getId());
        response.setStudentId(enrollment.getStudentId());
        response.setCourseId(enrollment.getCourseId());
        response.setEnrollmentDate(enrollment.getEnrollmentDate());
        response.setEnrollmentStatus(enrollment.getEnrollmentStatus());
        response.setTotalFees(enrollment.getTotalFees());
        response.setAmountPaid(enrollment.getAmountPaid());
        response.setRemainingBalance(enrollment.getRemainingBalance());
        response.setPaymentType(enrollment.getPaymentType());
        response.setInstallmentPlan(enrollment.getInstallmentPlan());
        response.setNextPaymentDue(enrollment.getNextPaymentDue());
        response.setScholarshipApplied(enrollment.getScholarshipApplied());
        response.setDiscountAmount(enrollment.getDiscountAmount());
        response.setPaymentGatewayUrl(paymentGatewayUrl);
        response.setMessage("Enrollment created successfully");
        return response;
    }
}
