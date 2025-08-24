package me.synology.techrevive.teacher.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bulletin_subject_grades")
public class BulletinSubjectGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bulletin_id")
    private Bulletin bulletin;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "final_grade")
    private BigDecimal finalGrade;

    @Column(name = "subject_comment")
    private String subjectComment;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
}