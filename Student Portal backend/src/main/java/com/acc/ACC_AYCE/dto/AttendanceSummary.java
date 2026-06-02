package com.acc.ACC_AYCE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceSummary {
    private Long studentId;
    private String studentName;
    private long totalSessions;
    private long attended;
    private double percentage;
}
