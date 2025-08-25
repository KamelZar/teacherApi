package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // Trouver toutes les matières d'une classe (via ClassEntity)
    List<Subject> findByClassEntity_Id(Long classId);

    // Trouver par nom dans une classe
    List<Subject> findByNameAndClassEntity_Id(String name, Long classId);

    // Trouver par nom (toutes classes)
    List<Subject> findByName(String name);

    // Vérifier existence matière dans une classe
    boolean existsByNameAndClassEntity_Id(String name, Long classId);

    // Compter matières par classe
    long countByClassEntity_Id(Long classId);
}