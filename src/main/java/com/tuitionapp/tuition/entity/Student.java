package com.tuitionapp.tuition.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
    private String rollNumber;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    // Alternative field name for backward compatibility
    public String getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone = phoneNumber;
    }

    @NotBlank
    private String parentName;

    @NotBlank
    private String parentPhone;

    @Email
    private String parentEmail;

    @NotBlank
    private String department;

    @NotBlank
    private String className;

    @NotBlank
    private String address;

    @NotNull
    private LocalDate joiningDate;
    
    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal monthlyFee = BigDecimal.valueOf(1000.00);
    
    @Column(precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StudentStatus status = StudentStatus.ACTIVE;
    
    // Proper JPA relationship to FeeRecord
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FeeRecord> feeRecords;

    // Student status enum
    public enum StudentStatus {
        ACTIVE, INACTIVE, GRADUATED, SUSPENDED
    }
}
