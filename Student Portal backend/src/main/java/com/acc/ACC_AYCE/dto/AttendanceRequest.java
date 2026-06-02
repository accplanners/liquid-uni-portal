package com.acc.ACC_AYCE.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class AttendanceRequest {
    @NonNull
    private Long studentId;
    @NonNull
    private Long sessionId;
    // Optional: override status (PRESENT, LATE, ABSENT). Defaults to PRESENT.
    private String status;
}
