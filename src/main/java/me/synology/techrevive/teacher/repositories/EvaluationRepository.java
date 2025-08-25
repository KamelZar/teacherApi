package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.EvaluationEntity;
import me.synology.techrevive.teacher.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<EvaluationEntity, Long> {

    // Trouver les évaluations par sujet
    List<EvaluationEntity> findByTopic(Topic topic);
    
    // Trouver les évaluations par ID de sujet
    List<EvaluationEntity> findByTopicId(Long topicId);

    // Trouver les évaluations par date
    List<EvaluationEntity> findByEvaluationDate(LocalDate evaluationDate);
    
    // Trouver les évaluations dans une plage de dates
    List<EvaluationEntity> findByEvaluationDateBetween(LocalDate startDate, LocalDate endDate);

    // Recherche par nom (partielle, insensible à la casse)
    List<EvaluationEntity> findByNameContainingIgnoreCase(String namePart);

    // TODO: Remplacer par une méthode dans le service - logique avec jointure
    // Trouver toutes les évaluations d'une matière
    @Query("SELECT e FROM EvaluationEntity e WHERE e.topic.subject.id = :subjectId")
    List<EvaluationEntity> findBySubjectId(@Param("subjectId") Long subjectId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec jointures multiples
    // Trouver toutes les évaluations d'une classe
    @Query("SELECT e FROM EvaluationEntity e WHERE e.topic.subject.classEntity.id = :classId")
    List<EvaluationEntity> findByClassId(@Param("classId") Long classId);

    // TODO: Remplacer par une méthode dans le service - logique avec jointure
    // Trouver les évaluations d'une période
    @Query("SELECT e FROM EvaluationEntity e WHERE e.topic.period.id = :periodId")
    List<EvaluationEntity> findByPeriodId(@Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique très complexe avec jointures multiples
    // Trouver les évaluations d'une classe pour une période donnée
    @Query("SELECT e FROM EvaluationEntity e WHERE e.topic.subject.classEntity.id = :classId AND e.topic.period.id = :periodId")
    List<EvaluationEntity> findByClassIdAndPeriodId(@Param("classId") Long classId, @Param("periodId") Long periodId);

    // TODO: Remplacer par une méthode dans le service - logique de filtrage par date
    // Trouver les évaluations récentes (derniers N jours)
    @Query("SELECT e FROM EvaluationEntity e WHERE e.evaluationDate >= :fromDate ORDER BY e.evaluationDate DESC")
    List<EvaluationEntity> findRecentEvaluations(@Param("fromDate") LocalDate fromDate);

    // Compter les évaluations par sujet
    long countByTopicId(Long topicId);

    // TODO: Remplacer par une méthode dans le service - logique de comptage avec jointure
    // Compter les évaluations par matière
    @Query("SELECT COUNT(e) FROM EvaluationEntity e WHERE e.topic.subject.id = :subjectId")
    long countBySubjectId(@Param("subjectId") Long subjectId);

    // Ordonner par date d'évaluation (plus récentes en premier)
    List<EvaluationEntity> findByTopicIdOrderByEvaluationDateDesc(Long topicId);
}