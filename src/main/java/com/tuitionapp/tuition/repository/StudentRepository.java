package com.tuitionapp.tuition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuitionapp.tuition.entity.Student;

import java.util.List; // ✅ Make sure this is included!

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByName(String name);
    boolean existsByRollNumber(String rollNumber);
    List<Student> findByClassName(String className);
}
