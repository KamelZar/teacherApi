package me.synology.techrevive.teacher.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "class_students",
       uniqueConstraints = @UniqueConstraint(columnNames = {"class_id", "student_id"}))
public class ClassStudent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "class_id", nullable = false)
    private Long classId;
    
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    
    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt;
    
    public ClassStudent() {}
    
    public ClassStudent(Long classId, Long studentId) {
        this.classId = classId;
        this.studentId = studentId;
        this.enrolledAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getClassId() {
        return classId;
    }
    
    public void setClassId(Long classId) {
        this.classId = classId;
    }
    
    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    
    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }
    
    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}