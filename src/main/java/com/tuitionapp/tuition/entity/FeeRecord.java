package com.tuitionapp.tuition.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fee_records")  // Explicit table name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;
    
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(name = "paid_date")
    private LocalDate paidDate;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private FeeStatus status = FeeStatus.DUE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20)
    private PaymentMethod paymentMethod;
    
    @Column(name = "transaction_id", length = 100)
    private String transactionId;
    
    @Column(length = 500)  // Limit remarks length
    private String remarks;
    
    public enum FeeStatus {
        DUE, PAID, OVERDUE
    }
    
    public enum PaymentMethod {
        CASH, UPI, GPAY, PHONEPE, PAYTM
    }
}
