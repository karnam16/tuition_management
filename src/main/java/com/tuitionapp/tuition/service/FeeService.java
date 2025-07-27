package com.tuitionapp.tuition.service;

import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.entity.Student;
import com.tuitionapp.tuition.repository.FeeRecordRepository;
import com.tuitionapp.tuition.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FeeService {

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    // ────────────────── CRUD Operations ──────────────────
    public FeeRecord addFeeRecord(FeeRecord feeRecord) {
        // Set default values if not provided
        if (feeRecord.getStatus() == null) {
            feeRecord.setStatus(FeeRecord.FeeStatus.DUE);
        }
        if (feeRecord.getDueDate() == null) {
            feeRecord.setDueDate(LocalDate.now().plusDays(30)); // Due in 30 days
        }
        return feeRecordRepository.save(feeRecord);
    }

    public List<FeeRecord> getAllFeeRecords() {
        return feeRecordRepository.findAll();
    }

    public List<FeeRecord> getDueFees() {
        return feeRecordRepository.findByStatus(FeeRecord.FeeStatus.DUE);
    }

    public Optional<FeeRecord> getFeeRecordById(Long id) {
        return feeRecordRepository.findById(id);
    }

    public FeeRecord updateFeeRecord(FeeRecord feeRecord) {
        return feeRecordRepository.save(feeRecord);
    }

    public void deleteFeeRecord(Long id) {
        feeRecordRepository.deleteById(id);
    }

    // ────────────────── Fee-specific Operations ──────────────────
    public List<FeeRecord> getFeesByStudentId(Long studentId) {
        return feeRecordRepository.findByStudentId(studentId);
    }

    public FeeRecord markFeeAsPaid(Long id, String paymentMethod) {
        Optional<FeeRecord> opt = feeRecordRepository.findById(id);
        if (opt.isPresent()) {
            FeeRecord fee = opt.get();
            fee.setStatus(FeeRecord.FeeStatus.PAID);
            fee.setPaymentDate(LocalDate.now());
            fee.setPaymentMethod(paymentMethod);
            return feeRecordRepository.save(fee);
        }
        throw new RuntimeException("Fee record not found with id: " + id);
    }

    public List<FeeRecord> getTodaysDueFees() {
        LocalDate today = LocalDate.now();
        return feeRecordRepository.findByDueDateAndStatus(today, FeeRecord.FeeStatus.DUE);
    }

    public List<FeeRecord> getOverdueFees() {
        LocalDate today = LocalDate.now();
        return feeRecordRepository.findOverdueFees(today);
    }

    // ────────────────── Financial Calculations ──────────────────
    public double getTotalDueAmount() {
        return getDueFees().stream()
                .mapToDouble(fee -> fee.getAmount() != null ? fee.getAmount() : 0.0)
                .sum();
    }

    public double getTotalCollectedThisMonth() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        return feeRecordRepository.findAll().stream()
                .filter(fee -> fee.getPaymentDate() != null)
                .filter(fee -> !fee.getPaymentDate().isBefore(startOfMonth) &&
                        !fee.getPaymentDate().isAfter(endOfMonth))
                .mapToDouble(fee -> fee.getAmount() != null ? fee.getAmount() : 0.0)
                .sum();
    }

    public double getTotalCollectedAllTime() {
        return feeRecordRepository.findAll().stream()
                .filter(fee -> fee.getStatus() == FeeRecord.FeeStatus.PAID)
                .mapToDouble(fee -> fee.getAmount() != null ? fee.getAmount() : 0.0)
                .sum();
    }

    // ────────────────── Utility Methods ──────────────────
    public FeeRecord createMonthlyFeeForStudent(Long studentId, double amount) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            FeeRecord feeRecord = new FeeRecord();
            feeRecord.setStudentId(studentId);
            feeRecord.setAmount(amount);
            feeRecord.setDueDate(LocalDate.now().plusDays(30));
            feeRecord.setStatus(FeeRecord.FeeStatus.DUE);
            return addFeeRecord(feeRecord);
        }
        throw new RuntimeException("Student not found with id: " + studentId);
    }

    public long getTotalDueFeeCount() {
        return feeRecordRepository.countByStatus(FeeRecord.FeeStatus.DUE);
    }
}
