package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.Remark;
import me.synology.techrevive.teacher.entities.RemarkType;
import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.entities.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RemarkRepository extends JpaRepository<Remark, Long> {

    // Trouver les remarques d'un élève
    List<Remark> findByStudent(User student);
    
    // Trouver les remarques d'un élève par ID
    List<Remark> findByStudentId(Long studentId);

    // Trouver les remarques par période
    List<Remark> findByPeriod(Period period);
    
    // Trouver les remarques par ID de période
    List<Remark> findByPeriodId(Long periodId);

    // Trouver les remarques par professeur
    List<Remark> findByTeacher(User teacher);
    
    // Trouver les remarques par ID de professeur
    List<Remark> findByTeacherId(Long teacherId);

    // Trouver les remarques par type
    List<Remark> findByType(RemarkType type);

    // Trouver les remarques d'un élève pour une période
    List<Remark> findByStudentAndPeriod(User student, Period period);
    
    // Trouver les remarques d'un élève pour une période par IDs
    List<Remark> findByStudentIdAndPeriodId(Long studentId, Long periodId);

    // Trouver les remarques d'un élève par type
    List<Remark> findByStudentAndType(User student, RemarkType type);
    
    // Trouver les remarques d'un élève par type et IDs
    List<Remark> findByStudentIdAndType(Long studentId, RemarkType type);

    // Trouver les remarques d'un professeur pour une période
    List<Remark> findByTeacherAndPeriod(User teacher, Period period);
    
    // Trouver les remarques d'un professeur pour une période par IDs
    List<Remark> findByTeacherIdAndPeriodId(Long teacherId, Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec sous-requête
    // Trouver toutes les remarques d'une classe pour une période
    @Query("SELECT r FROM Remark r WHERE r.student.id IN (SELECT cs.studentId FROM ClassStudent cs WHERE cs.classId = :classId) AND r.period.id = :periodId")
    List<Remark> findByClassIdAndPeriodId(@Param("classId") Long classId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique très complexe avec sous-requêtes imbriquées
    // Trouver les remarques d'un élève pour une classe
    @Query("SELECT r FROM Remark r WHERE r.student.id = :studentId AND r.period.id IN (SELECT p.id FROM Period p WHERE p.id IN (SELECT cs.classId FROM ClassStudent cs WHERE cs.studentId = :studentId))")
    List<Remark> findByStudentIdAndClassId(@Param("studentId") Long studentId, @Param("classId") Long classId);

    // Recherche par contenu (partielle, insensible à la casse)
    List<Remark> findByContentContainingIgnoreCase(String contentPart);

    // Trouver les remarques créées dans une période
    List<Remark> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // TODO: Remplacer par une méthode dans le service - logique de filtrage par date
    // Trouver les remarques récentes (derniers N jours)
    @Query("SELECT r FROM Remark r WHERE r.createdAt >= :fromDate ORDER BY r.createdAt DESC")
    List<Remark> findRecentRemarks(@Param("fromDate") LocalDateTime fromDate);

    // Compter les remarques par élève
    long countByStudentId(Long studentId);

    // Compter les remarques par type
    long countByType(RemarkType type);

    // Compter les remarques par professeur
    long countByTeacherId(Long teacherId);

    // Compter les remarques d'un élève pour une période
    long countByStudentIdAndPeriodId(Long studentId, Long periodId);

    // Ordonner par date de création (plus récentes en premier)
    List<Remark> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    
    // Ordonner par date de création (plus récentes en premier) pour une période
    List<Remark> findByStudentIdAndPeriodIdOrderByCreatedAtDesc(Long studentId, Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique métier de classification
    // Trouver les remarques positives (PROGRESS, EFFORT, PARTICIPATION)
    @Query("SELECT r FROM Remark r WHERE r.type IN ('PROGRESS', 'EFFORT', 'PARTICIPATION') AND r.student.id = :studentId")
    List<Remark> findPositiveRemarksByStudentId(@Param("studentId") Long studentId);

    // TODO: Remplacer par une méthode dans le service - logique métier de classification
    // Trouver les remarques négatives (BEHAVIOR, HOMEWORK, ATTENDANCE)
    @Query("SELECT r FROM Remark r WHERE r.type IN ('BEHAVIOR', 'HOMEWORK', 'ATTENDANCE') AND r.student.id = :studentId")
    List<Remark> findNegativeRemarksByStudentId(@Param("studentId") Long studentId);
}