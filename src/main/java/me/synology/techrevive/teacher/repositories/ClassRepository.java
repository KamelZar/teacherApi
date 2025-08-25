package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    
    // ✅ Méthodes automatiques JPA - simples et efficaces
    
    // Trouver par année scolaire
    List<ClassEntity> findBySchoolYear(Integer schoolYear);
    
    // Trouver par nom et année (contrainte unique)
    Optional<ClassEntity> findByNameAndSchoolYear(String name, Integer schoolYear);
    
    // Vérifier existence par nom et année
    boolean existsByNameAndSchoolYear(String name, Integer schoolYear);
    
    // Trouver par nom (toutes années confondues)
    List<ClassEntity> findByName(String name);
    
    // Trouver par nom contenant (recherche)
    List<ClassEntity> findByNameContainingIgnoreCase(String namePart);
    
    // Trouver par description contenant
    List<ClassEntity> findByDescriptionContainingIgnoreCase(String descriptionPart);
    
    // Pour les relations avec professeurs/élèves, utiliser les services
    // qui combineront ClassRepository + ClassTeacherRepository + ClassStudentRepository
}