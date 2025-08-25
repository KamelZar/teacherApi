package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.Bulletin;
import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.entities.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Long> {

    // Trouver les bulletins d'un élève
    List<Bulletin> findByStudent(User student);
    
    // Trouver les bulletins d'un élève par ID
    List<Bulletin> findByStudentId(Long studentId);

    // Trouver les bulletins par période
    List<Bulletin> findByPeriod(Period period);
    
    // Trouver les bulletins par ID de période
    List<Bulletin> findByPeriodId(Long periodId);

    // Trouver le bulletin spécifique d'un élève pour une période
    Optional<Bulletin> findByStudentAndPeriod(User student, Period period);
    
    // Trouver le bulletin spécifique par IDs
    Optional<Bulletin> findByStudentIdAndPeriodId(Long studentId, Long periodId);

    // Trouver les bulletins finalisés
    List<Bulletin> findByIsFinalized(Boolean isFinalized);
    
    // Trouver les bulletins finalisés d'un élève
    List<Bulletin> findByStudentIdAndIsFinalized(Long studentId, Boolean isFinalized);

    // Trouver les bulletins finalisés par un professeur
    List<Bulletin> findByFinalizedByTeacher(User teacher);
    
    // Trouver les bulletins finalisés par un professeur par ID
    List<Bulletin> findByFinalizedByTeacherId(Long teacherId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec sous-requête
    // Trouver tous les bulletins d'une classe pour une période
    @Query("SELECT b FROM Bulletin b WHERE b.student.id IN (SELECT cs.studentId FROM ClassStudent cs WHERE cs.classId = :classId) AND b.period.id = :periodId")
    List<Bulletin> findByClassIdAndPeriodId(@Param("classId") Long classId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique avec sous-requête
    // Trouver tous les bulletins d'une classe
    @Query("SELECT b FROM Bulletin b WHERE b.student.id IN (SELECT cs.studentId FROM ClassStudent cs WHERE cs.classId = :classId)")
    List<Bulletin> findByClassId(@Param("classId") Long classId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec sous-requête et filtrage
    // Trouver les bulletins finalisés d'une classe pour une période
    @Query("SELECT b FROM Bulletin b WHERE b.student.id IN (SELECT cs.studentId FROM ClassStudent cs WHERE cs.classId = :classId) AND b.period.id = :periodId AND b.isFinalized = true")
    List<Bulletin> findFinalizedByClassIdAndPeriodId(@Param("classId") Long classId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec sous-requête et conditions multiples
    // Trouver les bulletins non finalisés d'une classe pour une période
    @Query("SELECT b FROM Bulletin b WHERE b.student.id IN (SELECT cs.studentId FROM ClassStudent cs WHERE cs.classId = :classId) AND b.period.id = :periodId AND (b.isFinalized = false OR b.isFinalized IS NULL)")
    List<Bulletin> findNotFinalizedByClassIdAndPeriodId(@Param("classId") Long classId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique de filtrage
    // Trouver les bulletins avec commentaire final
    @Query("SELECT b FROM Bulletin b WHERE b.teacherFinalComment IS NOT NULL AND b.teacherFinalComment != ''")
    List<Bulletin> findAllWithTeacherComment();

    // TODO: Remplacer par une méthode dans le service - logique de filtrage
    // Trouver les bulletins avec commentaire de fin d'année
    @Query("SELECT b FROM Bulletin b WHERE b.yearEndComment IS NOT NULL AND b.yearEndComment != ''")
    List<Bulletin> findAllWithYearEndComment();

    // Trouver les bulletins finalisés dans une période
    List<Bulletin> findByFinalizedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // TODO: Remplacer par une méthode dans le service - logique de filtrage par date
    // Trouver les bulletins récemment finalisés
    @Query("SELECT b FROM Bulletin b WHERE b.finalizedAt >= :fromDate AND b.isFinalized = true ORDER BY b.finalizedAt DESC")
    List<Bulletin> findRecentlyFinalized(@Param("fromDate") LocalDateTime fromDate);

    // Compter les bulletins d'un élève
    long countByStudentId(Long studentId);

    // Compter les bulletins par période
    long countByPeriodId(Long periodId);

    // Compter les bulletins finalisés
    long countByIsFinalized(Boolean isFinalized);

    // Compter les bulletins finalisés par un professeur
    long countByFinalizedByTeacherId(Long teacherId);

    // TODO: Remplacer par une méthode dans le service - logique de comptage avec sous-requête
    // Compter les bulletins d'une classe pour une période
    @Query("SELECT COUNT(b) FROM Bulletin b WHERE b.student.id IN (SELECT cs.studentId FROM ClassStudent cs WHERE cs.classId = :classId) AND b.period.id = :periodId")
    long countByClassIdAndPeriodId(@Param("classId") Long classId, @Param("periodId") Long periodId);

    // Vérifier si un bulletin existe pour un élève et une période
    boolean existsByStudentIdAndPeriodId(Long studentId, Long periodId);

    // Ordonner par date de création (plus récents en premier)
    List<Bulletin> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    
    // Ordonner par date de finalisation (plus récents en premier)
    List<Bulletin> findByStudentIdAndIsFinalizedOrderByFinalizedAtDesc(Long studentId, Boolean isFinalized);
}