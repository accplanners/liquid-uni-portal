package com.acc.ACC_AYCE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingResponse {
    private Long billId;
    private Double amount;
    private String status;
    private String dueDate;
    private String description;
    private StudentInfo student;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentInfo {
        private Long studentId;
        private String name;
        private String email;
        private String studentCode;
    }
}
