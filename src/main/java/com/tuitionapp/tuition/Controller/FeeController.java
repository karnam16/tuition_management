package com.tuitionapp.tuition.Controller;

import com.tuitionapp.tuition.dto.CreateFeeRecordDto;
import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.service.FeeService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class FeeController {

    @Autowired
    private FeeService feeService;

    @PostConstruct
    public void init() {
        System.out.println("✅ FeeController loaded successfully!");
    }

    // ────────────────── Basic CRUD Operations ──────────────────
    @GetMapping("/fees")
    public ResponseEntity<List<FeeRecord>> getAllFeeRecords() {
        try {
            List<FeeRecord> fees = feeService.getAllFeeRecords();
            return ResponseEntity.ok(fees);
        } catch (Exception e) {
            System.err.println("Error fetching all fees: " + e.getMessage());
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }

    @GetMapping("/fees/due")
    public ResponseEntity<List<FeeRecord>> getDueFees() {
        try {
            List<FeeRecord> dueFees = feeService.getDueFees();
            return ResponseEntity.ok(dueFees);
        } catch (Exception e) {
            System.err.println("Error fetching due fees: " + e.getMessage());
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }

    @PostMapping("/fees")
    public ResponseEntity<FeeRecord> addFeeRecord(@RequestBody CreateFeeRecordDto feeRecordDto) {
        try {
            FeeRecord saved = feeService.createFeeRecordFromDto(feeRecordDto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("Error adding fee record: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/fees/{id}")
    public ResponseEntity<FeeRecord> getFeeRecordById(@PathVariable Long id) {
        try {
            Optional<FeeRecord> opt = feeService.getFeeRecordById(id);
            return opt.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error fetching fee by ID: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/fees/{id}")
    public ResponseEntity<FeeRecord> updateFeeRecord(@PathVariable Long id, @RequestBody CreateFeeRecordDto feeRecordDto) {
        try {
            FeeRecord updated = feeService.updateFeeRecord(id, feeRecordDto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("Error updating fee record: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/fees/{id}")
    public ResponseEntity<Void> deleteFeeRecord(@PathVariable Long id) {
        try {
            feeService.deleteFeeRecord(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting fee record: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ────────────────── Additional Endpoints ──────────────────
    @GetMapping("/fees/student/{studentId}")
    public ResponseEntity<List<FeeRecord>> getFeesByStudentId(@PathVariable Long studentId) {
        try {
            List<FeeRecord> fees = feeService.getFeesByStudentId(studentId);
            return ResponseEntity.ok(fees);
        } catch (Exception e) {
            System.err.println("Error fetching fees for student: " + e.getMessage());
            return ResponseEntity.ok(List.of());
        }
    }

    @PutMapping("/fees/{id}/pay")
    public ResponseEntity<FeeRecord> markFeeAsPaid(@PathVariable Long id) {
        try {
            FeeRecord paidFee = feeService.markFeeAsPaid(id);
            return ResponseEntity.ok(paidFee);
        } catch (Exception e) {
            System.err.println("Error marking fee as paid: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ────────────────── Test Endpoint ──────────────────
    @GetMapping("/fees/test")
    public ResponseEntity<String> controllerTest() {
        return ResponseEntity.ok("FeeController is working perfectly!");
    }
}
