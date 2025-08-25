package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {

    // Trouver toutes les périodes d'une classe (via ClassEntity)
    List<Period> findByClassEntity_Id(Long classId);

    // Trouver la période active d'une classe (unique grâce à la contrainte DB)
    Optional<Period> findByClassEntity_IdAndIsActive(Long classId, Boolean isActive);

    // Trouver les périodes fermées d'une classe
    List<Period> findByClassEntity_IdAndIsClosed(Long classId, Boolean isClosed);

    // Trouver par nom dans une classe
    Optional<Period> findByNameAndClassEntity_Id(String name, Long classId);

    // Vérifier s'il y a une période active pour une classe
    boolean existsByClassEntity_IdAndIsActive(Long classId, Boolean isActive);

    // Compter périodes par classe
    long countByClassEntity_Id(Long classId);

    // Ordonner par date de début
    List<Period> findByClassEntity_IdOrderByStartDate(Long classId);
}