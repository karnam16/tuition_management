package com.tuitionapp.tuition.service;

import com.tuitionapp.tuition.dto.DashboardStatsDto;
import com.tuitionapp.tuition.dto.DueFeeResponse;
import com.tuitionapp.tuition.dto.WhatsAppReminderDto;
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

    public List<DueFeeResponse> getDueFeesForStudent(Long studentId) {
        List<FeeRecord> dueFees = feeRecordRepository.findByStudentId(studentId).stream()
                .filter(fee -> fee.getStatus() == FeeRecord.FeeStatus.DUE)
                .collect(Collectors.toList());
        
        return dueFees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DueFeeResponse convertToDto(FeeRecord feeRecord) {
        DueFeeResponse dto = new DueFeeResponse();
        dto.setTotalDueCount(1);
        // Set other properties as needed based on your DTO structure
        return dto;
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

        return response;
    }

    private String generateWhatsAppMessage(Student student) {
        return String.format(
                "Dear %s, your tuition fee is due today. Please make the payment at your earliest convenience. Thank you!",
                student.getName());
    }

    // ────────────────── Dashboard & Analytics ──────────────────
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        
        // Basic counts
        stats.setTotalStudents(studentRepository.count());
        
        // Due fees count
        LocalDate today = LocalDate.now();
        List<FeeRecord> dueFees = feeRecordRepository.findByDueDateAndStatus(today, FeeRecord.FeeStatus.DUE);
        stats.setDueFeesToday(dueFees.size());
        
        // Paid fees today
        List<FeeRecord> paidToday = feeRecordRepository.findPaidFeesInMonth(today);
        stats.setPaidToday(paidToday.size());
        
        // Calculate total due amount
        Double totalDueAmount = feeRecordRepository.getTotalAmountByStatus(FeeRecord.FeeStatus.DUE);
        stats.setTotalDueAmount(totalDueAmount != null ? BigDecimal.valueOf(totalDueAmount) : BigDecimal.ZERO);
        
        // Today's collection
        Double todayCollection = feeRecordRepository.getTotalCollectedInMonth(today);
        stats.setTodayCollection(todayCollection != null ? BigDecimal.valueOf(todayCollection) : BigDecimal.ZERO);
        
        return stats;
    }

    public List<WhatsAppReminderDto> getWhatsAppReminders() {
        List<Student> studentsWithDueFees = getStudentsWithDueFeesToday();
        
        return studentsWithDueFees.stream()
                .map(this::createWhatsAppReminder)
                .collect(Collectors.toList());
    }

    private WhatsAppReminderDto createWhatsAppReminder(Student student) {
        WhatsAppReminderDto reminder = new WhatsAppReminderDto();
        reminder.setStudentName(student.getName());
        reminder.setParentPhone(student.getPhone()); // Using 'phone' field from Student entity
        reminder.setMessage(generateWhatsAppMessage(student));
        return reminder;
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
