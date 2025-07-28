package com.tuitionapp.tuition.Controller;

import com.tuitionapp.tuition.dto.DashboardStatsDto;
import com.tuitionapp.tuition.dto.DueFeeResponse;
import com.tuitionapp.tuition.dto.WhatsAppReminderDto;
import com.tuitionapp.tuition.entity.Student;
import com.tuitionapp.tuition.service.StudentService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @PostConstruct
    public void init() {
        logger.info("StudentController initialized successfully");
    }

    // ────────────────── Student CRUD Operations ──────────────────
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            logger.debug("Retrieved {} students", students.size());
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            logger.error("Error fetching all students: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/students")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        try {
            Student saved = studentService.addStudent(student);
            logger.info("Successfully added student with ID: {}", saved.getId());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            logger.error("Error adding student: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        try {
            Optional<Student> student = studentService.getStudentById(id);
            if (student.isPresent()) {
                logger.debug("Retrieved student with ID: {}", id);
                return ResponseEntity.ok(student.get());
            } else {
                logger.warn("Student not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching student by ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            student.setId(id);
            Student updated = studentService.updateStudent(student);
            logger.info("Successfully updated student with ID: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Error updating student with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            logger.info("Successfully deleted student with ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting student with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // ────────────────── Fee-related Operations ──────────────────
    @GetMapping("/students/{id}/due-fees")
    public ResponseEntity<List<DueFeeResponse>> getDueFees(@PathVariable Long id) {
        try {
            List<DueFeeResponse> dueFees = studentService.getDueFeesForStudent(id);
            logger.debug("Retrieved {} due fees for student ID: {}", dueFees.size(), id);
            return ResponseEntity.ok(dueFees);
        } catch (Exception e) {
            logger.error("Error fetching due fees for student ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ────────────────── Dashboard & Analytics ──────────────────
    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        try {
            DashboardStatsDto stats = studentService.getDashboardStats();
            logger.debug("Retrieved dashboard statistics");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error fetching dashboard stats: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/reminders/whatsapp")
    public ResponseEntity<List<WhatsAppReminderDto>> getWhatsAppReminders() {
        try {
            List<WhatsAppReminderDto> reminders = studentService.getWhatsAppReminders();
            logger.debug("Retrieved {} WhatsApp reminders", reminders.size());
            return ResponseEntity.ok(reminders);
        } catch (Exception e) {
            logger.error("Error fetching WhatsApp reminders: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ────────────────── Debug / Test End-points ──────────────────
    @GetMapping("/students/test")
    public ResponseEntity<String> controllerTest() {
        logger.info("StudentController test endpoint accessed");
        return ResponseEntity.ok("StudentController is working perfectly!");
    }
}
