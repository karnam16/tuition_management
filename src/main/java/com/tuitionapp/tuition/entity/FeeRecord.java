package com.tuitionapp.tuition.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Proper JPA relationship instead of just studentId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;
    
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FeeStatus status;
    
    // Enum for fee status
    public enum FeeStatus {
        DUE, PAID, OVERDUE
    }
    
    // Convenience method to get student ID
    public Long getStudentId() {
        return student != null ? student.getId() : null;
    }
    
    // Convenience method to set student by ID (for backward compatibility)
    public void setStudentId(Long studentId) {
        if (studentId != null) {
            this.student = new Student();
            this.student.setId(studentId);
        }
    }

    // Alternative field name for backward compatibility
    public LocalDate getPaidDate() {
        return paymentDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paymentDate = paidDate;
    }
}
