package com.tuitionapp.tuition.service;

import com.tuitionapp.tuition.dto.CreateFeeRecordDto;
import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.entity.Student;
import com.tuitionapp.tuition.repository.FeeRecordRepository;
import com.tuitionapp.tuition.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public FeeRecord createFeeRecordFromDto(CreateFeeRecordDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + dto.getStudentId()));

        FeeRecord feeRecord = FeeRecord.builder()
                .student(student)
                .amount(dto.getAmount())
                .dueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDate.now().plusDays(30))
                .paymentDate(dto.getPaidDate())
                .status(dto.getStatus() != null ? dto.getStatus() : FeeRecord.FeeStatus.DUE)
                .description(dto.getDescription())
                .build();

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

    public FeeRecord updateFeeRecord(Long id, CreateFeeRecordDto dto) {
        FeeRecord existingFee = feeRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee record not found with id: " + id));

        // Update student if changed
        if (dto.getStudentId() != null && !dto.getStudentId().equals(existingFee.getStudent().getId())) {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + dto.getStudentId()));
            existingFee.setStudent(student);
        }

        // Update other fields
        if (dto.getAmount() != null) {
            existingFee.setAmount(dto.getAmount());
        }
        if (dto.getDueDate() != null) {
            existingFee.setDueDate(dto.getDueDate());
        }
        if (dto.getPaidDate() != null) {
            existingFee.setPaidDate(dto.getPaidDate());
        }
        if (dto.getStatus() != null) {
            existingFee.setStatus(dto.getStatus());
        }
        if (dto.getDescription() != null) {
            existingFee.setDescription(dto.getDescription());
        }

        return feeRecordRepository.save(existingFee);
    }

    public void deleteFeeRecord(Long id) {
        if (!feeRecordRepository.existsById(id)) {
            throw new RuntimeException("Fee record not found with id: " + id);
        }
        feeRecordRepository.deleteById(id);
    }

    public List<FeeRecord> getFeesByStudentId(Long studentId) {
        return feeRecordRepository.findByStudentId(studentId);
    }

    public FeeRecord markFeeAsPaid(Long feeId) {
        FeeRecord feeRecord = feeRecordRepository.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee record not found with id: " + feeId));

        feeRecord.setStatus(FeeRecord.FeeStatus.PAID);
        feeRecord.setPaidDate(LocalDate.now());

        return feeRecordRepository.save(feeRecord);
    }

    // ────────────────── Analytics & Statistics ──────────────────
    public BigDecimal getTotalCollectedAmount() {
        List<FeeRecord> paidFees = feeRecordRepository.findByStatus(FeeRecord.FeeStatus.PAID);
        return paidFees.stream()
                .map(FeeRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalDueAmount() {
        List<FeeRecord> dueFees = feeRecordRepository.findByStatus(FeeRecord.FeeStatus.DUE);
        return dueFees.stream()
                .map(FeeRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long getTotalDueCount() {
        return feeRecordRepository.countByStatus(FeeRecord.FeeStatus.DUE);
    }

    public long getTotalPaidCount() {
        return feeRecordRepository.countByStatus(FeeRecord.FeeStatus.PAID);
    }

    public List<FeeRecord> getOverdueFees() {
        return feeRecordRepository.findOverdueFees(LocalDate.now());
    }

    public List<FeeRecord> getFeesDueToday() {
        return feeRecordRepository.findByDueDateAndStatus(LocalDate.now(), FeeRecord.FeeStatus.DUE);
    }

    public List<FeeRecord> getFeesForDateRange(LocalDate startDate, LocalDate endDate) {
        return feeRecordRepository.findByDueDateBetween(startDate, endDate);
    }

    // ────────────────── Fee Generation ──────────────────
    public FeeRecord generateMonthlyFeeForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        // Calculate discounted amount
        BigDecimal discountAmount = student.getMonthlyFee()
                .multiply(student.getDiscountPercent())
                .divide(BigDecimal.valueOf(100));
        BigDecimal finalAmount = student.getMonthlyFee().subtract(discountAmount);

        FeeRecord feeRecord = FeeRecord.builder()
                .student(student)
                .amount(finalAmount)
                .dueDate(LocalDate.now().plusDays(30))
                .status(FeeRecord.FeeStatus.DUE)
                .description("Monthly tuition fee")
                .build();

        return feeRecordRepository.save(feeRecord);
    }

    public void generateMonthlyFeesForAllStudents() {
        List<Student> activeStudents = studentRepository.findByStatus(Student.StudentStatus.ACTIVE);
        
        for (Student student : activeStudents) {
            generateMonthlyFeeForStudent(student.getId());
        }
    }
}
