package com.acc.ACC_AYCE.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "session_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private OnlineSession session;

    private String status;                // PRESENT, ABSENT, LATE

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;       // When student actually joined

    @Column(name = "marked_at")
    private LocalDateTime markedAt;       // When attendance was recorded
}
