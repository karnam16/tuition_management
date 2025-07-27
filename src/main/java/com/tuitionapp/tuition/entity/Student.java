package com.tuitionapp.tuition.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @NotNull
    private LocalDate joiningDate;
    private BigDecimal monthlyFee = BigDecimal.valueOf(1000.00);
    private BigDecimal discountPercent = BigDecimal.ZERO;
}
