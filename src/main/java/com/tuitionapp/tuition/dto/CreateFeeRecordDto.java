package com.tuitionapp.tuition.dto;

import com.tuitionapp.tuition.entity.FeeRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeeRecordDto {
    private Long studentId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private FeeRecord.FeeStatus status;
    private String description;
}