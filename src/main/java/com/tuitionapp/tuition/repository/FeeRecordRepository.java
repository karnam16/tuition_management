package com.tuitionapp.tuition.repository;

import com.tuitionapp.tuition.entity.FeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeeRecordRepository extends JpaRepository<FeeRecord, Long> {
    
    // Find by status
    List<FeeRecord> findByStatus(FeeRecord.FeeStatus status);
    
    // Find by student ID
    List<FeeRecord> findByStudentId(Long studentId);
    
    // Find by due date and status
    List<FeeRecord> findByDueDateAndStatus(LocalDate dueDate, FeeRecord.FeeStatus status);
    
    // Count by due date and status
    long countByDueDateAndStatus(LocalDate dueDate, FeeRecord.FeeStatus status);
    
    // Count by status
    long countByStatus(FeeRecord.FeeStatus status);
    
    // Find all due fees
    @Query("SELECT f FROM FeeRecord f WHERE f.status = 'DUE'")
    List<FeeRecord> findAllDueFees();
    
    // Find overdue fees
    @Query("SELECT f FROM FeeRecord f WHERE f.dueDate < :date AND f.status = 'DUE'")
    List<FeeRecord> findOverdueFees(@Param("date") LocalDate date);
    
    // Find fees between dates
    @Query("SELECT f FROM FeeRecord f WHERE f.dueDate BETWEEN :startDate AND :endDate")
    List<FeeRecord> findFeesBetweenDates(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate);
    
    // Find paid fees this month
    @Query("SELECT f FROM FeeRecord f WHERE f.status = 'PAID' AND " +
           "MONTH(f.paymentDate) = MONTH(:date) AND YEAR(f.paymentDate) = YEAR(:date)")
    List<FeeRecord> findPaidFeesInMonth(@Param("date") LocalDate date);
    
    // Get total amount by status
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FeeRecord f WHERE f.status = :status")
    Double getTotalAmountByStatus(@Param("status") FeeRecord.FeeStatus status);
    
    // Get total collected this month
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FeeRecord f WHERE f.status = 'PAID' AND " +
           "MONTH(f.paymentDate) = MONTH(:date) AND YEAR(f.paymentDate) = YEAR(:date)")
    Double getTotalCollectedInMonth(@Param("date") LocalDate date);
}
