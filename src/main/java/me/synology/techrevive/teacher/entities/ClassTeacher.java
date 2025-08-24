package me.synology.techrevive.teacher.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "class_teachers")
public class ClassTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "is_main_teacher")
    private Boolean isMainTeacher;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
}