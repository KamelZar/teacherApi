package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.ClassStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudent, Long> {

    // Trouver toutes les classes d'un élève
    List<ClassStudent> findByStudentId(Long studentId);

    // Trouver tous les élèves d'une classe
    List<ClassStudent> findByClassId(Long classId);

    // Vérifier si un élève est dans une classe
    boolean existsByStudentIdAndClassId(Long studentId, Long classId);

    // Compter les élèves dans une classe
    long countByClassId(Long classId);

    // Compter les classes d'un élève
    long countByStudentId(Long studentId);
}