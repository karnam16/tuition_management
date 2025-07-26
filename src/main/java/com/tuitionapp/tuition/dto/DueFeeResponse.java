package com.tuitionapp.tuition.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.tuitionapp.tuition.entity.Student;

// Update your DueFeeResponse class
@Data
@NoArgsConstructor
public class DueFeeResponse {
    private List<StudentDueInfo> dueStudents;
    private int totalCount;
    private String generatedOn;
    
    public DueFeeResponse(List<Student> students, List<String> messages) {
        this.dueStudents = new ArrayList<>();
        this.generatedOn = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            String message = messages.get(i);
            this.dueStudents.add(new StudentDueInfo(student, message));
        }
        this.totalCount = students.size();
    }
    
    @Data
    @AllArgsConstructor
    public static class StudentDueInfo {
        private Long id;
        private String name;
        private String rollNumber;
        private String email;
        private String department;
        private Double monthlyFee;
        private String dueMessage;
        
        public StudentDueInfo(Student student, String message) {
            this.id = student.getId();
            this.name = student.getName();
            this.rollNumber = student.getRollNumber();
            this.email = student.getEmail();
            this.department = student.getDepartment();
            this.dueMessage = message;
        }
    }
}
