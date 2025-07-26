package com.tuitionapp.tuition.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String rollNumber;
    private String email;
    private String phone;
    private String parentName;
    private String parentPhone;  
    private String parentEmail;
    private String department;
    private String className;
    private LocalDate joiningDate;
    private BigDecimal monthlyFee = BigDecimal.valueOf(1000.00);
    private BigDecimal discountPercent = BigDecimal.ZERO;
}
