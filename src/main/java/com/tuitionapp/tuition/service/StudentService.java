package com.tuitionapp.tuition.service;

import com.tuitionapp.tuition.dto.DueFeeResponse;
import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.entity.Student;
import com.tuitionapp.tuition.repository.FeeRecordRepository;
import com.tuitionapp.tuition.repository.StudentRepository;
import com.tuitionapp.tuition.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private FeeService feeService;

    // ────────────────── CRUD Operations ──────────────────
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // ────────────────── Fee-related Methods ──────────────────
    public List<Student> getStudentsWithDueFeesToday() {
        LocalDate today = LocalDate.now();
        List<FeeRecord> dueFees = feeRecordRepository.findByDueDateAndStatus(today, FeeRecord.FeeStatus.DUE);

        List<Long> studentIds = dueFees.stream()
                .map(FeeRecord::getStudentId)
                .distinct()
                .collect(Collectors.toList());

        return studentRepository.findAllById(studentIds);
    }

    public DueFeeResponse getDueFeesWithMessages() {
        List<Student> studentsWithDueFees = getStudentsWithDueFeesToday();

        DueFeeResponse response = new DueFeeResponse();
        response.setStudents(studentsWithDueFees);

        List<String> messages = studentsWithDueFees.stream()
                .map(this::generateWhatsAppMessage)
                .collect(Collectors.toList());

        response.setWhatsappMessages(messages);
        response.setTotalDueCount(studentsWithDueFees.size());
        // Calculate and set the aggregated due amount using FeeService
        response.setTotalDueAmount(feeService.getTotalDueAmount());

        return response;
    }

    private String generateWhatsAppMessage(Student student) {
        return String.format(
                "Dear %s, your tuition fee is due today. Please make the payment at your earliest convenience. Thank you!",
                student.getName());
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
