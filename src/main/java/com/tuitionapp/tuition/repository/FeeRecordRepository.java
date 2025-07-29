package com.tuitionapp.tuition.repository;

import com.tuitionapp.tuition.entity.FeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeeRecordRepository extends JpaRepository<FeeRecord, Long> {
    
    // Find by status
    List<FeeRecord> findByStatus(FeeRecord.FeeStatus status);
    
    // Count by status
    long countByStatus(FeeRecord.FeeStatus status);
    
    // Find by student ID using the relationship
    @Query("SELECT f FROM FeeRecord f WHERE f.student.id = :studentId")
    List<FeeRecord> findByStudentId(@Param("studentId") Long studentId);
    
    // Find by student ID ordered by due date desc
    @Query("SELECT f FROM FeeRecord f WHERE f.student.id = :studentId ORDER BY f.dueDate DESC")
    List<FeeRecord> findByStudentIdOrderByDueDateDesc(@Param("studentId") Long studentId);
    
    // Find by due date and status
    List<FeeRecord> findByDueDateAndStatus(LocalDate dueDate, FeeRecord.FeeStatus status);
    
    // Count by due date and status
    long countByDueDateAndStatus(LocalDate dueDate, FeeRecord.FeeStatus status);
    
    // Find overdue fees (due date before today and status is DUE)
    @Query("SELECT f FROM FeeRecord f WHERE f.dueDate < :today AND f.status = 'DUE'")
    List<FeeRecord> findOverdueFees(@Param("today") LocalDate today);
    
    // Find fees between dates
    @Query("SELECT f FROM FeeRecord f WHERE f.dueDate BETWEEN :startDate AND :endDate")
    List<FeeRecord> findByDueDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find fees between dates (alternative method name)
    @Query("SELECT f FROM FeeRecord f WHERE f.dueDate BETWEEN :startDate AND :endDate")
    List<FeeRecord> findFeesBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Get total amount by status
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FeeRecord f WHERE f.status = :status")
    BigDecimal getTotalAmountByStatus(@Param("status") FeeRecord.FeeStatus status);
    
    // Get outstanding amount for a student
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FeeRecord f WHERE f.student.id = :studentId AND f.status = 'DUE'")
    BigDecimal getOutstandingAmountByStudentId(@Param("studentId") Long studentId);
    
    // Get total collected in a specific month
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FeeRecord f WHERE f.status = 'PAID' AND YEAR(f.paymentDate) = :year AND MONTH(f.paymentDate) = :month")
    BigDecimal getTotalCollectedInMonth(@Param("year") int year, @Param("month") int month);
    
    // Alternative method for getting total collected in month using LocalDate
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FeeRecord f WHERE f.status = 'PAID' AND YEAR(f.paymentDate) = YEAR(:date) AND MONTH(f.paymentDate) = MONTH(:date)")
    BigDecimal getTotalCollectedInMonth(@Param("date") LocalDate date);
}
