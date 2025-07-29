package com.tuitionapp.tuition.dto;

import com.tuitionapp.tuition.entity.FeeRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DueFeeResponse {
    private List<FeeRecord> dueFees;
    private List<String> messages;
    
    // Computed property for backward compatibility
    public int getTotalDueCount() {
        return dueFees != null ? dueFees.size() : 0;
    }
}
