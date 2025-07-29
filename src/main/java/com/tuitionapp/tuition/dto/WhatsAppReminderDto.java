package com.tuitionapp.tuition.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppReminderDto {
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private String parentName;
    private String parentPhone;
    private BigDecimal amount;
    private String message;
    private String whatsappUrl;
    private Long feeRecordId;
}
