package com.acc.ACC_AYCE.dto;

import com.acc.ACC_AYCE.Entity.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflinePaymentRequest {

    private Long studentId;

    private Long enrollmentId;

    private Double amountPaid;

    private Payment.PaymentMode paymentMode;

    private Payment.PaymentType paymentType;

    private String transactionId;

    private Double discountAmount;

    private String scholarshipCode;

    private Integer installmentNumber;

    private Integer totalInstallments;

    private String paymentProof;

    private String notes;

    private String bankName;

    private String chequeNumber;

    private String upiTransactionId;

    private String paymentDate;
}
