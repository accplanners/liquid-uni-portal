package com.acc.ACC_AYCE.repository;

import com.acc.ACC_AYCE.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    List<Payment> findByStudent_StudentId(Long studentId);
    
    List<Payment> findByEnrollment_Id(Long enrollmentId);
    
    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.enrollment.id = :enrollmentId AND p.paymentStatus = 'COMPLETED'")
    Double getTotalPaidAmountForEnrollment(@Param("enrollmentId") Long enrollmentId);
    
    @Query("SELECT p FROM Payment p WHERE p.student.studentId = :studentId AND p.enrollment.id = :enrollmentId")
    Optional<Payment> findByStudentAndEnrollment(@Param("studentId") Long studentId, @Param("enrollmentId") Long enrollmentId);
}
