package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.Topic;
import me.synology.techrevive.teacher.entities.Subject;
import me.synology.techrevive.teacher.entities.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    // Trouver les sujets par matière
    List<Topic> findBySubject(Subject subject);
    
    // Trouver les sujets par ID de matière
    List<Topic> findBySubjectId(Long subjectId);

    // Trouver les sujets par période
    List<Topic> findByPeriod(Period period);
    
    // Trouver les sujets par ID de période
    List<Topic> findByPeriodId(Long periodId);

    // Trouver les sujets par matière et période
    List<Topic> findBySubjectAndPeriod(Subject subject, Period period);
    
    // Trouver les sujets par ID de matière et ID de période
    List<Topic> findBySubjectIdAndPeriodId(Long subjectId, Long periodId);

    // Recherche par nom (partielle, insensible à la casse)
    List<Topic> findByNameContainingIgnoreCase(String namePart);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec jointure
    // Trouver tous les sujets d'une classe via la matière
    @Query("SELECT t FROM Topic t WHERE t.subject.classEntity.id = :classId")
    List<Topic> findByClassId(@Param("classId") Long classId);

    // TODO: Remplacer par une méthode dans le service - logique complexe avec jointures multiples
    // Trouver les sujets d'une classe pour une période donnée
    @Query("SELECT t FROM Topic t WHERE t.subject.classEntity.id = :classId AND t.period.id = :periodId")
    List<Topic> findByClassIdAndPeriodId(@Param("classId") Long classId, @Param("periodId") Long periodId);

    // Compter les sujets par matière
    long countBySubjectId(Long subjectId);

    // Compter les sujets par période
    long countByPeriodId(Long periodId);
}