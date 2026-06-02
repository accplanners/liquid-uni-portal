package com.acc.ACC_AYCE.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "online_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OnlineSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    private String title;

    @Column(name = "meet_link")
    private String meetLink;           // Google Meet URL

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt; // When session starts

    @Column(name = "duration_minutes")
    private int durationMinutes;       // Duration of session

    private String status;             // SCHEDULED, ONGOING, COMPLETED, CANCELLED

    // Which course this session belongs to
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Faculty who created this session
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
