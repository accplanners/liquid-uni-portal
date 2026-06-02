package com.acc.ACC_AYCE.service;

import com.acc.ACC_AYCE.Entity.Attendance;
import com.acc.ACC_AYCE.Entity.OnlineSession;
import com.acc.ACC_AYCE.Entity.Student;
import com.acc.ACC_AYCE.dto.AttendanceRequest;
import com.acc.ACC_AYCE.dto.AttendanceSummary;
import com.acc.ACC_AYCE.dto.CreateSessionRequest;
import com.acc.ACC_AYCE.repository.AttendanceRepository;
import com.acc.ACC_AYCE.repository.CourseRepository;
import com.acc.ACC_AYCE.repository.FacultyRepository;
import com.acc.ACC_AYCE.repository.OnlineSessionRepository;
import com.acc.ACC_AYCE.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.Objects;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private OnlineSessionRepository sessionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    // ─── SESSION MANAGEMENT ───────────────────────────────────────────────────

    /** Faculty creates a new online session with a Google Meet link */
    @SuppressWarnings("null")
    public OnlineSession createSession(CreateSessionRequest req) {
        OnlineSession session = new OnlineSession();
        session.setTitle(req.getTitle());
        session.setMeetLink(req.getMeetLink());
        session.setScheduledAt(req.getScheduledAt());
        session.setDurationMinutes(req.getDurationMinutes());
        session.setStatus("SCHEDULED");
        session.setCreatedAt(LocalDateTime.now());

        if (req.getCourseId() != null) {
            courseRepository.findById(req.getCourseId()).ifPresent(course -> session.setCourse(course));
        }
        if (req.getFacultyId() != null) {
            facultyRepository.findById(req.getFacultyId()).ifPresent(faculty -> session.setFaculty(faculty));
        }

        return sessionRepository.save(session);
    }

    /** Get all sessions for a course */
    public List<OnlineSession> getSessionsByCourse(Long courseId) {
        return sessionRepository.findByCourse_CourseId(courseId);
    }

    /** Get all sessions created by a faculty */
    public List<OnlineSession> getSessionsByFaculty(Long facultyId) {
        return sessionRepository.findByFaculty_FacultyId(facultyId);
    }

    /** Get all sessions (admin view) */
    public List<OnlineSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    /** Update session status (e.g., ONGOING, COMPLETED, CANCELLED) */
    public OnlineSession updateSessionStatus(@NonNull Long sessionId, String status) {
        OnlineSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));
        session.setStatus(status);
        return sessionRepository.save(session);
    }

    /** Delete a session */
    public String deleteSession(@NonNull Long sessionId) {
        sessionRepository.deleteById(sessionId);
        return "Session deleted successfully";
    }

    // ─── ATTENDANCE MANAGEMENT ────────────────────────────────────────────────

    /**
     * Student joins the session — marks attendance as PRESENT.
     * Marks LATE if joined more than 15 minutes after scheduled start.
     */
    public Attendance markAttendance(AttendanceRequest req) {
        // Check for duplicate
        Optional<Attendance> existing = attendanceRepository
                .findByStudent_StudentIdAndSession_SessionId(req.getStudentId(), req.getSessionId());

        if (existing.isPresent()) {
            return existing.get(); // Already marked
        }

        Long studentId = Objects.requireNonNull(req.getStudentId(), "Student ID cannot be null");
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        Long sessionId = Objects.requireNonNull(req.getSessionId(), "Session ID cannot be null");
        OnlineSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setSession(session);
        attendance.setJoinedAt(LocalDateTime.now());
        attendance.setMarkedAt(LocalDateTime.now());

        // Auto-detect LATE: joined > 15 min after scheduled start
        if (req.getStatus() != null) {
            attendance.setStatus(req.getStatus());
        } else {
            LocalDateTime lateThreshold = session.getScheduledAt().plusMinutes(15);
            attendance.setStatus(LocalDateTime.now().isAfter(lateThreshold) ? "LATE" : "PRESENT");
        }

        return attendanceRepository.save(attendance);
    }

    /** Get all attendance records for a student */
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudent_StudentId(studentId);
    }

    /** Get all attendance records for a session (faculty view) */
    public List<Attendance> getAttendanceBySession(Long sessionId) {
        return attendanceRepository.findBySession_SessionId(sessionId);
    }

    /** Get attendance of a student for a specific course */
    public List<Attendance> getAttendanceByStudentAndCourse(Long studentId, Long courseId) {
        return attendanceRepository.findByStudentAndCourse(studentId, courseId);
    }

    /** Attendance summary: percentage for a student */
    public AttendanceSummary getAttendanceSummary(@NonNull Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        long totalSessions = sessionRepository.count();
        long attended = attendanceRepository.countAttendedSessions(studentId);
        double percentage = totalSessions > 0 ? (attended * 100.0 / totalSessions) : 0.0;

        return new AttendanceSummary(studentId, student.getName(), totalSessions, attended, percentage);
    }

    /** Faculty manually marks a student absent */
    public Attendance markAbsent(@NonNull Long studentId, @NonNull Long sessionId) {
        Optional<Attendance> existing = attendanceRepository
                .findByStudent_StudentIdAndSession_SessionId(studentId, sessionId);

        if (existing.isPresent()) {
            Attendance a = existing.get();
            a.setStatus("ABSENT");
            return attendanceRepository.save(a);
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        OnlineSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setSession(session);
        attendance.setStatus("ABSENT");
        attendance.setMarkedAt(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }
}
