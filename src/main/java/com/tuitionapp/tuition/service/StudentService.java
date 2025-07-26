package com.tuitionapp.tuition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuitionapp.tuition.dto.DueFeeResponse;
import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.entity.Student;
import com.tuitionapp.tuition.repository.StudentRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private FeeService feeService;

    // Add student with automatic fee record generation
    public Student addStudent(Student student) {
        // Save student first
        Student savedStudent = studentRepository.save(student);
        
        // Auto-generate fee record
        feeService.generateMonthlyFeeRecord(savedStudent);
        
        return savedStudent;
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Update student
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    // Delete student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Get students with due fees today using FeeService
    public List<Student> getStudentsWithDueFeesToday() {
        List<FeeRecord> dueRecords = feeService.getTodaysDueFees();
        return dueRecords.stream()
            .map(FeeRecord::getStudent)
            .distinct()
            .collect(Collectors.toList());
    }
    
    // Get due fees with formatted messages
    public DueFeeResponse getDueFeesWithMessages() {
        List<FeeRecord> dueRecords = feeService.getTodaysDueFees();
        List<Student> dueStudents = dueRecords.stream()
            .map(FeeRecord::getStudent)
            .distinct()
            .collect(Collectors.toList());
        
        List<String> messages = new ArrayList<>();
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        
        for (FeeRecord feeRecord : dueRecords) {
            Student student = feeRecord.getStudent();
            String parentName = student.getParentName() != null ? student.getParentName() : student.getName();
            
            String message = String.format(
                "Dear %s,\n\n" +
                "Fee reminder for %s (Roll No: %s)\n" +
                "Amount Due: ₹%.2f\n" +
                "Due Date: %s\n\n" +
                "Payment Methods: Cash/UPI\n" +
                "Contact: [Your Tuition Center Phone]\n\n" +
                "Thank you!",
                parentName,
                student.getName(),
                student.getRollNumber(),
                feeRecord.getAmount(),
                today.format(formatter)
            );
            messages.add(message);
        }
        
        return new DueFeeResponse(dueStudents, messages);
    }

    // Get students by name
    public List<Student> getStudentsByName(String name) {
        return studentRepository.findByName(name);
    }

    // Check if student exists by roll number
    public boolean existsByRollNumber(String rollNumber) {
        return studentRepository.findAll().stream()
                .anyMatch(student -> rollNumber.equals(student.getRollNumber()));
    }

    // Get students by class
    public List<Student> getStudentsByClass(String className) {
        return studentRepository.findAll().stream()
                .filter(student -> className.equals(student.getClassName()))
                .collect(Collectors.toList());
    }
}
