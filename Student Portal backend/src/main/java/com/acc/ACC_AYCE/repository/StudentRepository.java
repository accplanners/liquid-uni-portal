package com.acc.ACC_AYCE.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acc.ACC_AYCE.Entity.Student;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
 // find student by email
 Optional<Student> findByEmail(String email);
 // find students by name
 List<Student> findByName(String name);
 // find students handled by registrar
 List<Student> findByRegistrarRegistrarId(Long registrarId);
}
