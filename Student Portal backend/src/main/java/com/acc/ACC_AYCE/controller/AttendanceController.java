package com.acc.ACC_AYCE.controller;

import com.acc.ACC_AYCE.Entity.Attendance;
import com.acc.ACC_AYCE.Entity.OnlineSession;
import com.acc.ACC_AYCE.dto.AttendanceRequest;
import com.acc.ACC_AYCE.dto.AttendanceSummary;
import com.acc.ACC_AYCE.dto.CreateSessionRequest;
import com.acc.ACC_AYCE.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // ─── SESSION ENDPOINTS ────────────────────────────────────────────────────

    /**
     * POST /api/attendance/sessions
     * Faculty creates a new session with Google Meet link.
     * Body: { title, meetLink, scheduledAt, durationMinutes, courseId, facultyId }
     */
    @PostMapping("/sessions")
    public ResponseEntity<OnlineSession> createSession(@RequestBody CreateSessionRequest request) {
        return ResponseEntity.ok(attendanceService.createSession(request));
    }

    /**
     * GET /api/attendance/sessions
     * Admin: get all sessions.
     */
    @GetMapping("/sessions")
    public ResponseEntity<List<OnlineSession>> getAllSessions() {
        return ResponseEntity.ok(attendanceService.getAllSessions());
    }

    /**
     * GET /api/attendance/sessions/course/{courseId}
     * Get all sessions for a specific course.
     */
    @GetMapping("/sessions/course/{courseId}")
    public ResponseEntity<List<OnlineSession>> getSessionsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(attendanceService.getSessionsByCourse(courseId));
    }

    /**
     * GET /api/attendance/sessions/faculty/{facultyId}
     * Faculty sees their own sessions.
     */
    @GetMapping("/sessions/faculty/{facultyId}")
    public ResponseEntity<List<OnlineSession>> getSessionsByFaculty(@PathVariable Long facultyId) {
        return ResponseEntity.ok(attendanceService.getSessionsByFaculty(facultyId));
    }

    /**
     * PUT /api/attendance/sessions/{sessionId}/status?status=ONGOING
     * Update session status: SCHEDULED → ONGOING → COMPLETED / CANCELLED
     */
    @PutMapping("/sessions/{sessionId}/status")
    public ResponseEntity<OnlineSession> updateSessionStatus(
            @PathVariable @NonNull Long sessionId,
            @RequestParam String status) {
        return ResponseEntity.ok(attendanceService.updateSessionStatus(sessionId, status));
    }

    /**
     * DELETE /api/attendance/sessions/{sessionId}
     */
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<String> deleteSession(@PathVariable @NonNull Long sessionId) {
        return ResponseEntity.ok(attendanceService.deleteSession(sessionId));
    }

    // ─── ATTENDANCE ENDPOINTS ─────────────────────────────────────────────────

    /**
     * POST /api/attendance/mark
     * Student joins the Google Meet → frontend calls this to mark attendance.
     * Body: { studentId, sessionId }
     * Status is auto-set to PRESENT or LATE based on join time.
     */
    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(@RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.markAttendance(request));
    }

    /**
     * POST /api/attendance/mark-absent
     * Faculty manually marks a student absent.
     * Body: { studentId, sessionId }
     */
    @PostMapping("/mark-absent")
    public ResponseEntity<Attendance> markAbsent(@RequestBody AttendanceRequest request) {
        Long studentId = request.getStudentId();
        Long sessionId = request.getSessionId();
        
        if (studentId == null || sessionId == null) {
            throw new IllegalArgumentException("Student ID and Session ID cannot be null");
        }
        
        return ResponseEntity.ok(attendanceService.markAbsent(studentId, sessionId));
    }

    /**
     * GET /api/attendance/student/{studentId}
     * Student views all their attendance records.
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
    }

    /**
     * GET /api/attendance/session/{sessionId}
     * Faculty views who attended a specific session.
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Attendance>> getAttendanceBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getAttendanceBySession(sessionId));
    }

    /**
     * GET /api/attendance/student/{studentId}/course/{courseId}
     * Student views attendance for a specific course.
     */
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudentAndCourse(studentId, courseId));
    }

    /**
     * GET /api/attendance/summary/{studentId}
     * Returns: totalSessions, attended, percentage.
     */
    @GetMapping("/summary/{studentId}")
    public ResponseEntity<AttendanceSummary> getAttendanceSummary(@PathVariable @NonNull Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceSummary(studentId));
    }
}
