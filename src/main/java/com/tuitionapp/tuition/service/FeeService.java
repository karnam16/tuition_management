package com.tuitionapp.tuition.service;

import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.entity.Student;
import com.tuitionapp.tuition.repository.FeeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FeeService {
    
    @Autowired
    private FeeRecordRepository feeRecordRepository;
    
    // Generate monthly fee record for new student
    public FeeRecord generateMonthlyFeeRecord(Student student) {
        FeeRecord feeRecord = new FeeRecord();
        feeRecord.setStudent(student);
        
        // Apply discount if applicable
        BigDecimal baseAmount = student.getMonthlyFee();
        BigDecimal discountPercent = student.getDiscountPercent();
        BigDecimal finalAmount = baseAmount;
        
        if (discountPercent != null && discountPercent.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = baseAmount.multiply(discountPercent).divide(BigDecimal.valueOf(100));
            finalAmount = baseAmount.subtract(discount);
        }
        
        feeRecord.setAmount(finalAmount);
        
        // Set due date to next month from joining date (business logic correction)
        LocalDate dueDate = student.getJoiningDate().plusMonths(1);
        feeRecord.setDueDate(dueDate);
        feeRecord.setStatus(FeeRecord.FeeStatus.DUE);
        
        return feeRecordRepository.save(feeRecord);
    }
    
    // Get today's due fees
    public List<FeeRecord> getTodaysDueFees() {
        LocalDate today = LocalDate.now();
        return feeRecordRepository.findByDueDateAndStatus(today, FeeRecord.FeeStatus.DUE);
    }
    
    // Mark fee as paid
    public FeeRecord markAsPaid(Long feeRecordId) {
        FeeRecord feeRecord = feeRecordRepository.findById(feeRecordId)
            .orElseThrow(() -> new RuntimeException("Fee record not found"));
        
        feeRecord.setStatus(FeeRecord.FeeStatus.PAID);
        feeRecord.setPaidDate(LocalDate.now());
        
        return feeRecordRepository.save(feeRecord);
    }
}
