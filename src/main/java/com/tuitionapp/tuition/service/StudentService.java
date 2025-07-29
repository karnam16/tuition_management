package com.tuitionapp.tuition.service;

import com.tuitionapp.tuition.dto.DueFeeResponse;
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
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    // ────────────────── CRUD Operations ──────────────────
    public Student addStudent(Student student) {
        // Set default values if not provided
        if (student.getJoiningDate() == null) {
            student.setJoiningDate(LocalDate.now());
        }
        if (student.getStatus() == null) {
            student.setStatus(Student.StudentStatus.ACTIVE);
        }
        if (student.getDiscountPercent() == null) {
            student.setDiscountPercent(BigDecimal.ZERO);
        }
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Update fields
        student.setName(studentDetails.getName());
        student.setPhoneNumber(studentDetails.getPhoneNumber());
        student.setEmail(studentDetails.getEmail());
        student.setAddress(studentDetails.getAddress());
        student.setClassName(studentDetails.getClassName());
        student.setMonthlyFee(studentDetails.getMonthlyFee());
        student.setDiscountPercent(studentDetails.getDiscountPercent());
        student.setStatus(studentDetails.getStatus());

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    // ────────────────── Fee-related Methods ──────────────────
    public List<Student> getStudentsWithDueFees() {
        List<FeeRecord> dueFees = feeRecordRepository.findByStatus(FeeRecord.FeeStatus.DUE);
        return dueFees.stream()
                .map(FeeRecord::getStudent)
                .distinct()
                .collect(Collectors.toList());
    }

    public DueFeeResponse getDueFeesWithMessages() {
        List<FeeRecord> dueFees = feeRecordRepository.findByStatus(FeeRecord.FeeStatus.DUE);
        
        List<String> messages = dueFees.stream()
                .map(fee -> String.format("Dear %s, your fee of Rs. %.2f is due on %s. Please pay at your earliest convenience.",
                        fee.getStudent().getName(),
                        fee.getAmount(),
                        fee.getDueDate()))
                .collect(Collectors.toList());

        return DueFeeResponse.builder()
                .dueFees(dueFees)
                .messages(messages)
                .build();
    }

    // ────────────────── Utility Methods ──────────────────
    public long getTotalStudentCount() {
        return studentRepository.count();
    }

    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> searchStudents(String name, String className) {
        return studentRepository.searchStudents(name, className);
    }

    public List<Student> getStudentsByClass(String className) {
        return studentRepository.findByClassName(className);
    }
}
