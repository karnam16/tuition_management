package com.tuitionapp.tuition.repository;

import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeeRecordRepository extends JpaRepository<FeeRecord, Long> {
    
    List<FeeRecord> findByDueDateAndStatus(LocalDate dueDate, FeeRecord.FeeStatus status);
    List<FeeRecord> findByStudentAndStatus(Student student, FeeRecord.FeeStatus status);
    long countByDueDateAndStatus(LocalDate dueDate, FeeRecord.FeeStatus status);
    
    @Query("SELECT SUM(f.amount) FROM FeeRecord f WHERE f.paidDate = :paidDate AND f.status = 'PAID'")
    BigDecimal getTodayCollection(@Param("paidDate") LocalDate paidDate);
    
    @Query("SELECT DISTINCT f.student FROM FeeRecord f WHERE f.paidDate = :paidDate AND f.status = 'PAID'")
    List<Student> findPaidStudentsToday(@Param("paidDate") LocalDate paidDate);
    
    // ✅ MISSING METHODS - ADD THESE
    List<FeeRecord> findByStatusAndDueDateBefore(FeeRecord.FeeStatus status, LocalDate date);
    List<FeeRecord> findByStudentOrderByDueDateDesc(Student student);
    boolean existsByStudentAndDueDateBetween(Student student, LocalDate startDate, LocalDate endDate);
}
