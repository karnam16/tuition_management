package com.tuitionapp.tuition.Controller;

import com.tuitionapp.tuition.dto.DueFeeResponse;
import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.entity.Student;
import com.tuitionapp.tuition.repository.FeeRecordRepository;
import com.tuitionapp.tuition.service.StudentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")  // ✅ Changed from "/students" to "/api"
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Validated
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
    @PostMapping("/students")  // ✅ Now creates /api/students
    public ResponseEntity<?> addStudent(@RequestBody @jakarta.validation.Valid Student student) {
        try {
            Student saved = studentService.addStudent(student);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // READ – all
    @GetMapping("/students")  // ✅ Now creates /api/students
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // READ – by id
    @GetMapping("/students/{id}")  // ✅ Now creates /api/students/{id}
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> opt = studentService.getStudentById(id);
        return opt.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/students/{id}")  // ✅ Now creates /api/students/{id}
    public ResponseEntity<Student> updateStudent(@PathVariable Long id,
                                                 @RequestBody Student student) {
        student.setId(id);
        Student updated = studentService.updateStudent(student);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/students/{id}")  // ✅ Now creates /api/students/{id}
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────── Fee-related End-points ──────────────────
    // List students whose fees are due today (raw list)
    @GetMapping("/students/due-fees")  // ✅ Now creates /api/students/due-fees
    public ResponseEntity<List<Student>> getDueFeesToday() {
        return ResponseEntity.ok(studentService.getStudentsWithDueFeesToday());
    }

    // Same list + formatted messages
    @GetMapping("/students/due-fees-with-messages")  // ✅ Now creates /api/students/due-fees-with-messages
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

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .reduce("", (msg, err) -> msg + err + "; ");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
    }
}
