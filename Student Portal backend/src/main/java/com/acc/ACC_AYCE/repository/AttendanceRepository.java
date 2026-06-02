package com.acc.ACC_AYCE.repository;

import com.acc.ACC_AYCE.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudent_StudentId(Long studentId);

    List<Attendance> findBySession_SessionId(Long sessionId);

    Optional<Attendance> findByStudent_StudentIdAndSession_SessionId(Long studentId, Long sessionId);

    // Count how many sessions a student attended (PRESENT or LATE)
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.studentId = :studentId AND a.status IN ('PRESENT', 'LATE')")
    long countAttendedSessions(@Param("studentId") Long studentId);

    // Attendance per course
    @Query("SELECT a FROM Attendance a WHERE a.student.studentId = :studentId AND a.session.course.courseId = :courseId")
    List<Attendance> findByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
