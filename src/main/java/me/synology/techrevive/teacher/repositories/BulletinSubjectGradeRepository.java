package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.BulletinSubjectGrade;
import me.synology.techrevive.teacher.entities.Bulletin;
import me.synology.techrevive.teacher.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BulletinSubjectGradeRepository extends JpaRepository<BulletinSubjectGrade, Long> {

    // Trouver les notes par bulletin
    List<BulletinSubjectGrade> findByBulletin(Bulletin bulletin);
    
    // Trouver les notes par ID de bulletin
    List<BulletinSubjectGrade> findByBulletinId(Long bulletinId);

    // Trouver les notes par matière
    List<BulletinSubjectGrade> findBySubject(Subject subject);
    
    // Trouver les notes par ID de matière
    List<BulletinSubjectGrade> findBySubjectId(Long subjectId);

    // Trouver une note spécifique par bulletin et matière
    Optional<BulletinSubjectGrade> findByBulletinAndSubject(Bulletin bulletin, Subject subject);
    
    // Trouver une note spécifique par IDs
    Optional<BulletinSubjectGrade> findByBulletinIdAndSubjectId(Long bulletinId, Long subjectId);

    // TODO: Remplacer par une méthode dans le service - logique avec jointure
    // Trouver toutes les notes d'un élève pour toutes les matières
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.bulletin.student.id = :studentId")
    List<BulletinSubjectGrade> findByStudentId(@Param("studentId") Long studentId);

    // TODO: Remplacer par une méthode dans le service - logique avec jointures multiples
    // Trouver toutes les notes d'un élève pour une matière spécifique (tous bulletins confondus)
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.bulletin.student.id = :studentId AND bsg.subject.id = :subjectId")
    List<BulletinSubjectGrade> findByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);

    // TODO: Remplacer par une méthode dans le service - logique avec jointures multiples
    // Trouver toutes les notes d'un élève pour une période
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.bulletin.student.id = :studentId AND bsg.bulletin.period.id = :periodId")
    List<BulletinSubjectGrade> findByStudentIdAndPeriodId(@Param("studentId") Long studentId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique très complexe avec jointures multiples
    // Trouver toutes les notes d'une classe pour une matière et une période
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.subject.classEntity.id = :classId AND bsg.subject.id = :subjectId AND bsg.bulletin.period.id = :periodId")
    List<BulletinSubjectGrade> findByClassIdAndSubjectIdAndPeriodId(@Param("classId") Long classId, @Param("subjectId") Long subjectId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec jointures multiples
    // Trouver toutes les notes d'une classe pour une période
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.subject.classEntity.id = :classId AND bsg.bulletin.period.id = :periodId")
    List<BulletinSubjectGrade> findByClassIdAndPeriodId(@Param("classId") Long classId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - calcul complexe avec jointures et logique métier
    // Calculer la moyenne de classe pour une matière et une période
    @Query("SELECT AVG(bsg.finalGrade) FROM BulletinSubjectGrade bsg WHERE bsg.subject.classEntity.id = :classId AND bsg.subject.id = :subjectId AND bsg.bulletin.period.id = :periodId")
    Optional<BigDecimal> calculateClassAverageForSubjectAndPeriod(@Param("classId") Long classId, @Param("subjectId") Long subjectId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - calcul de moyenne avec jointures et logique métier
    // Calculer la moyenne générale d'un élève pour une période
    @Query("SELECT AVG(bsg.finalGrade) FROM BulletinSubjectGrade bsg WHERE bsg.bulletin.student.id = :studentId AND bsg.bulletin.period.id = :periodId")
    Optional<BigDecimal> calculateStudentGeneralAverageForPeriod(@Param("studentId") Long studentId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique de filtrage
    // Trouver les notes avec commentaire
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.subjectComment IS NOT NULL AND bsg.subjectComment != ''")
    List<BulletinSubjectGrade> findAllWithComments();

    // TODO: Remplacer par une méthode dans le service - logique de filtrage par seuil
    // Trouver les meilleures notes (supérieures à un seuil)
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.finalGrade >= :minGrade")
    List<BulletinSubjectGrade> findGradesAbove(@Param("minGrade") BigDecimal minGrade);

    // TODO: Remplacer par une méthode dans le service - logique de filtrage par seuil
    // Trouver les notes en difficulté (inférieures à un seuil)
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.finalGrade < :maxGrade")
    List<BulletinSubjectGrade> findGradesBelow(@Param("maxGrade") BigDecimal maxGrade);

    // Trouver les notes calculées dans une période
    List<BulletinSubjectGrade> findByCalculatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // TODO: Remplacer par une méthode dans le service - logique de filtrage par date
    // Trouver les notes récemment calculées
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.calculatedAt >= :fromDate ORDER BY bsg.calculatedAt DESC")
    List<BulletinSubjectGrade> findRecentlyCalculated(@Param("fromDate") LocalDateTime fromDate);

    // Compter les notes par bulletin
    long countByBulletinId(Long bulletinId);

    // Compter les notes par matière
    long countBySubjectId(Long subjectId);

    // Vérifier si une note existe pour un bulletin et une matière
    boolean existsByBulletinIdAndSubjectId(Long bulletinId, Long subjectId);

    // Ordonner par note (meilleures en premier)
    List<BulletinSubjectGrade> findByBulletinIdOrderByFinalGradeDesc(Long bulletinId);
    
    // TODO: Remplacer par une méthode dans le service - logique de tri avec jointure
    // Ordonner par matière pour un bulletin
    @Query("SELECT bsg FROM BulletinSubjectGrade bsg WHERE bsg.bulletin.id = :bulletinId ORDER BY bsg.subject.name ASC")
    List<BulletinSubjectGrade> findByBulletinIdOrderBySubjectName(@Param("bulletinId") Long bulletinId);

    // TODO: Remplacer par une méthode dans le service - calcul statistique avec jointures
    // Statistiques : trouver la note maximale pour une matière et une période
    @Query("SELECT MAX(bsg.finalGrade) FROM BulletinSubjectGrade bsg WHERE bsg.subject.id = :subjectId AND bsg.bulletin.period.id = :periodId")
    Optional<BigDecimal> findMaxGradeForSubjectAndPeriod(@Param("subjectId") Long subjectId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - calcul statistique avec jointures
    // Statistiques : trouver la note minimale pour une matière et une période
    @Query("SELECT MIN(bsg.finalGrade) FROM BulletinSubjectGrade bsg WHERE bsg.subject.id = :subjectId AND bsg.bulletin.period.id = :periodId")
    Optional<BigDecimal> findMinGradeForSubjectAndPeriod(@Param("subjectId") Long subjectId, @Param("periodId") Long periodId);
}