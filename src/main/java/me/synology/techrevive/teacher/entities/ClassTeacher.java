package me.synology.techrevive.teacher.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "class_teachers",
       uniqueConstraints = @UniqueConstraint(columnNames = {"class_id", "teacher_id"}))
public class ClassTeacher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "class_id", nullable = false)
    private Long classId;
    
    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;
    
    @Column(name = "is_main_teacher", nullable = false)
    private Boolean isMainTeacher = false;
    
    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;
    
    public ClassTeacher() {}
    
    public ClassTeacher(Long classId, Long teacherId, Boolean isMainTeacher) {
        this.classId = classId;
        this.teacherId = teacherId;
        this.isMainTeacher = isMainTeacher;
        this.assignedAt = LocalDateTime.now();
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
    
    public Long getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
    
    public Boolean getIsMainTeacher() {
        return isMainTeacher;
    }
    
    public void setIsMainTeacher(Boolean isMainTeacher) {
        this.isMainTeacher = isMainTeacher;
    }
    
    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
    
    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}