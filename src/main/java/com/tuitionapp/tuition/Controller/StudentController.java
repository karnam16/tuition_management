package com.tuitionapp.tuition.Controller;          // ✅ package name = controller (all lowercase)

import com.tuitionapp.tuition.dto.DueFeeResponse;
import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.entity.Student;
import com.tuitionapp.tuition.repository.FeeRecordRepository;
import com.tuitionapp.tuition.service.StudentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    // ────────────────── Dependencies ──────────────────
    @Autowired
    private StudentService studentService;

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    // ────────────────── Helper ──────────────────
    @PostConstruct
    public void init() {
        System.out.println("✅ StudentController loaded successfully!");
    }

    // ────────────────── CRUD End-points ──────────────────
    // CREATE
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student saved = studentService.addStudent(student);
        return ResponseEntity.ok(saved);
    }

    // READ – all
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // READ – by id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> opt = studentService.getStudentById(id);
        return opt.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id,
                                                 @RequestBody Student student) {
        // Check if student exists before updating
        Optional<Student> existingStudent = studentService.getStudentById(id);
        if (existingStudent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        student.setId(id);
        Student updated = studentService.updateStudent(student);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────── Fee-related End-points ──────────────────
    // List students whose fees are due today (raw list)
    @GetMapping("/due-fees")
    public ResponseEntity<List<Student>> getDueFeesToday() {
        return ResponseEntity.ok(studentService.getStudentsWithDueFeesToday());
    }

    // Same list + formatted messages
    @GetMapping("/due-fees-with-messages")
    public ResponseEntity<DueFeeResponse> getDueFeesWithMessages() {
        return ResponseEntity.ok(studentService.getDueFeesWithMessages());
    }

    // ────────────────── Debug / Test End-points ──────────────────
    @GetMapping("/test-fee-repo")
    public ResponseEntity<String> testFeeRepository() {
        try {
            LocalDate today = LocalDate.now();
            long count = feeRecordRepository
                    .countByDueDateAndStatus(today, FeeRecord.FeeStatus.DUE);
            return ResponseEntity.ok("Repository working! Due fees count: " + count);
        } catch (Exception ex) {
            return ResponseEntity.ok("Error: " + ex.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> controllerTest() {
        return ResponseEntity.ok("StudentController is working perfectly!");
    }
}
