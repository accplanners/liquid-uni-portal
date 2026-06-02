package com.acc.ACC_AYCE.dto;

import com.acc.ACC_AYCE.Entity.Enrollment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {

    private Long courseId;
    
    private Double totalFees;
    
    private Enrollment.PaymentType paymentType = Enrollment.PaymentType.FULL_PAYMENT;
    
    private boolean onlinePayment = true;
    
    private String notes;
}
