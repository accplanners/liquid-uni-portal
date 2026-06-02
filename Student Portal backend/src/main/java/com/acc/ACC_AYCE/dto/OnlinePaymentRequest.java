package com.acc.ACC_AYCE.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlinePaymentRequest {

    private Long studentId;

    private Long enrollmentId;

    private Double amount;

    private String email;

    private String contact;

    private String description = "Course Enrollment Fee Payment";
}
