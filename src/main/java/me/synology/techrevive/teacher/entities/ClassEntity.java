package me.synology.techrevive.teacher.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "school_year")
    private Integer schoolYear;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relations JPA pour simplifier les requêtes
    @OneToMany(mappedBy = "classId", fetch = FetchType.LAZY)
    private List<ClassTeacher> classTeachers;

    @OneToMany(mappedBy = "classId", fetch = FetchType.LAZY)  
    private List<ClassStudent> classStudents;

    @OneToMany(mappedBy = "classEntity", fetch = FetchType.LAZY)
    private List<Subject> subjects;
}