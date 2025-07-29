package com.tuitionapp.tuition.repository;

import com.tuitionapp.tuition.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Find by name (case insensitive, partial match)
    List<Student> findByNameContainingIgnoreCase(String name);
    
    // Find by phone number
    List<Student> findByPhone(String phone);
    
    // Find by email  
    List<Student> findByEmail(String email);

    // Find by status
    List<Student> findByStatus(Student.StudentStatus status);
    
    // Use explicit queries for all complex field names to avoid property path issues
    @Query("SELECT s FROM Student s WHERE s.className = :className")
    List<Student> findByClassName(@Param("className") String className);
    
    @Query("SELECT s FROM Student s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:className IS NULL OR s.className = :className)")
    List<Student> searchStudents(@Param("name") String name, 
                                @Param("className") String className);
}
