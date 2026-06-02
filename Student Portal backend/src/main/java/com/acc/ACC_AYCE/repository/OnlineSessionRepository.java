package com.acc.ACC_AYCE.repository;

import com.acc.ACC_AYCE.Entity.OnlineSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnlineSessionRepository extends JpaRepository<OnlineSession, Long> {
    List<OnlineSession> findByCourse_CourseId(Long courseId);
    List<OnlineSession> findByFaculty_FacultyId(Long facultyId);
    List<OnlineSession> findByStatus(String status);
}
