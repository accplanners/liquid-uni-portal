package com.acc.ACC_AYCE.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateSessionRequest {
    private String title;
    private String meetLink;
    private LocalDateTime scheduledAt;
    private int durationMinutes;
    private Long courseId;
    private Long facultyId;
}
