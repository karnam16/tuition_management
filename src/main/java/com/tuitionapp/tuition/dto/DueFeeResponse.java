package com.tuitionapp.tuition.dto;

import com.tuitionapp.tuition.entity.Student;
import java.util.List;

public class DueFeeResponse {
    private List<Student> students;
    private List<String> whatsappMessages;
    private int totalDueCount;
    private double totalDueAmount;

    // Constructors
    public DueFeeResponse() {}

    public DueFeeResponse(List<Student> students, List<String> whatsappMessages, int totalDueCount, double totalDueAmount) {
        this.students = students;
        this.whatsappMessages = whatsappMessages;
        this.totalDueCount = totalDueCount;
        this.totalDueAmount = totalDueAmount;
    }

    // Getters and Setters
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    public List<String> getWhatsappMessages() { return whatsappMessages; }
    public void setWhatsappMessages(List<String> whatsappMessages) { this.whatsappMessages = whatsappMessages; }

    public int getTotalDueCount() { return totalDueCount; }
    public void setTotalDueCount(int totalDueCount) { this.totalDueCount = totalDueCount; }

    public double getTotalDueAmount() { return totalDueAmount; }
    public void setTotalDueAmount(double totalDueAmount) { this.totalDueAmount = totalDueAmount; }
}
