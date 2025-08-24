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
@Table(name = "remarks")
public class Remark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private Period period;

    @Enumerated(EnumType.STRING)
    private RemarkType type;

    private String content;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

enum RemarkType {
    BEHAVIOR, PROGRESS, EFFORT, PARTICIPATION, HOMEWORK, ATTENDANCE, GENERAL
}