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
@Table(name = "bulletins")
public class Bulletin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private Period period;

    @Column(name = "teacher_final_comment")
    private String teacherFinalComment;

    @Column(name = "year_end_comment")
    private String yearEndComment;

    @Column(name = "is_finalized")
    private Boolean isFinalized;

    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;

    @ManyToOne
    @JoinColumn(name = "finalized_by_teacher_id")
    private User finalizedByTeacher;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}