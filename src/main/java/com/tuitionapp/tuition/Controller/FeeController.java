package com.tuitionapp.tuition.Controller;

import com.tuitionapp.tuition.entity.FeeRecord;
import com.tuitionapp.tuition.service.FeeService;
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
public class FeeController {

    private static final Logger logger = LoggerFactory.getLogger(FeeController.class);

    @Autowired
    private FeeService feeService;

    @PostConstruct
    public void init() {
        logger.info("FeeController initialized successfully");
    }

    // ────────────────── Basic CRUD Operations ──────────────────
    @GetMapping("/fees")
    public ResponseEntity<List<FeeRecord>> getAllFeeRecords() {
        try {
            List<FeeRecord> fees = feeService.getAllFeeRecords();
            logger.debug("Retrieved {} fee records", fees.size());
            return ResponseEntity.ok(fees);
        } catch (Exception e) {
            logger.error("Error fetching all fees: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/fees/due")
    public ResponseEntity<List<FeeRecord>> getDueFees() {
        try {
            List<FeeRecord> dueFees = feeService.getDueFees();
            logger.debug("Retrieved {} due fee records", dueFees.size());
            return ResponseEntity.ok(dueFees);
        } catch (Exception e) {
            logger.error("Error fetching due fees: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/fees")
    public ResponseEntity<FeeRecord> addFeeRecord(@RequestBody FeeRecord feeRecord) {
        try {
            FeeRecord saved = feeService.addFeeRecord(feeRecord);
            logger.info("Successfully added fee record with ID: {}", saved.getId());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            logger.error("Error adding fee record: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/fees/{id}")
    public ResponseEntity<FeeRecord> getFeeRecordById(@PathVariable Long id) {
        try {
            Optional<FeeRecord> opt = feeService.getFeeRecordById(id);
            if (opt.isPresent()) {
                logger.debug("Retrieved fee record with ID: {}", id);
                return ResponseEntity.ok(opt.get());
            } else {
                logger.warn("Fee record not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching fee by ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ────────────────── Test Endpoint ──────────────────
    @GetMapping("/fees/test")
    public ResponseEntity<String> controllerTest() {
        logger.info("FeeController test endpoint accessed");
        return ResponseEntity.ok("FeeController is working perfectly!");
    }
}
