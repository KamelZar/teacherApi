package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.StudentEvaluation;
import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.entities.EvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentEvaluationRepository extends JpaRepository<StudentEvaluation, Long> {

    // Trouver les résultats d'un élève
    List<StudentEvaluation> findByStudent(User student);
    
    // Trouver les résultats d'un élève par ID
    List<StudentEvaluation> findByStudentId(Long studentId);

    // Trouver les résultats d'une évaluation
    List<StudentEvaluation> findByEvaluation(EvaluationEntity evaluation);
    
    // Trouver les résultats d'une évaluation par ID
    List<StudentEvaluation> findByEvaluationId(Long evaluationId);

    // Trouver le résultat spécifique d'un élève pour une évaluation
    Optional<StudentEvaluation> findByStudentAndEvaluation(User student, EvaluationEntity evaluation);
    
    // Trouver le résultat spécifique par IDs
    Optional<StudentEvaluation> findByStudentIdAndEvaluationId(Long studentId, Long evaluationId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec jointures multiples
    // Trouver tous les résultats d'un élève pour une matière
    @Query("SELECT se FROM StudentEvaluation se WHERE se.student.id = :studentId AND se.evaluation.topic.subject.id = :subjectId")
    List<StudentEvaluation> findByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec jointures multiples
    // Trouver tous les résultats d'un élève pour une période
    @Query("SELECT se FROM StudentEvaluation se WHERE se.student.id = :studentId AND se.evaluation.topic.period.id = :periodId")
    List<StudentEvaluation> findByStudentIdAndPeriodId(@Param("studentId") Long studentId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique très complexe avec jointures multiples
    // Trouver tous les résultats d'un élève pour une classe
    @Query("SELECT se FROM StudentEvaluation se WHERE se.student.id = :studentId AND se.evaluation.topic.subject.classEntity.id = :classId")
    List<StudentEvaluation> findByStudentIdAndClassId(@Param("studentId") Long studentId, @Param("classId") Long classId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec sous-requête et jointure
    // Trouver tous les résultats d'une classe pour une évaluation
    @Query("SELECT se FROM StudentEvaluation se WHERE se.evaluation.id = :evaluationId AND se.student.id IN (SELECT cs.studentId FROM ClassStudent cs WHERE cs.classId = :classId)")
    List<StudentEvaluation> findByEvaluationIdAndClassId(@Param("evaluationId") Long evaluationId, @Param("classId") Long classId);

    // TODO: Remplacer par une méthode dans le service - calcul complexe avec jointures et logique métier
    // Calculer la moyenne d'un élève pour une matière
    @Query("SELECT AVG((se.points / se.evaluation.maxPoints) * s.maxGrade) FROM StudentEvaluation se JOIN se.evaluation.topic.subject s WHERE se.student.id = :studentId AND s.id = :subjectId")
    Optional<BigDecimal> calculateStudentSubjectAverage(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);

    // TODO: Remplacer par une méthode dans le service - calcul de moyenne avec logique métier
    // Calculer la moyenne de classe pour une évaluation
    @Query("SELECT AVG(se.points) FROM StudentEvaluation se WHERE se.evaluation.id = :evaluationId")
    Optional<BigDecimal> calculateClassEvaluationAverage(@Param("evaluationId") Long evaluationId);

    // TODO: Remplacer par une méthode dans le service - logique de filtrage
    // Trouver les résultats avec photo
    @Query("SELECT se FROM StudentEvaluation se WHERE se.photoPath IS NOT NULL")
    List<StudentEvaluation> findAllWithPhotos();

    // TODO: Remplacer par une méthode dans le service - logique de filtrage
    // Trouver les résultats avec commentaires
    @Query("SELECT se FROM StudentEvaluation se WHERE se.comments IS NOT NULL AND se.comments != ''")
    List<StudentEvaluation> findAllWithComments();

    // Trouver les résultats soumis dans une période
    List<StudentEvaluation> findBySubmittedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Compter les résultats d'un élève
    long countByStudentId(Long studentId);

    // Compter les résultats d'une évaluation
    long countByEvaluationId(Long evaluationId);

    // Vérifier si un élève a déjà été évalué pour une évaluation
    boolean existsByStudentIdAndEvaluationId(Long studentId, Long evaluationId);

    // Ordonner par date de soumission (plus récents en premier)
    List<StudentEvaluation> findByStudentIdOrderBySubmittedAtDesc(Long studentId);
}