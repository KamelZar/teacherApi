package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.ClassTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassTeacherRepository extends JpaRepository<ClassTeacher, Long> {

    // Trouver toutes les classes d'un professeur
    List<ClassTeacher> findByTeacherId(Long teacherId);

    // Trouver les classes où le prof est principal
    List<ClassTeacher> findByTeacherIdAndIsMainTeacher(Long teacherId, Boolean isMainTeacher);

    // Trouver tous les profs d'une classe
    List<ClassTeacher> findByClassId(Long classId);

    // Trouver le prof principal d'une classe
    List<ClassTeacher> findByClassIdAndIsMainTeacher(Long classId, Boolean isMainTeacher);

    // Vérifier si un prof enseigne dans une classe
    boolean existsByTeacherIdAndClassId(Long teacherId, Long classId);

    // Vérifier si un prof est principal d'une classe
    boolean existsByTeacherIdAndClassIdAndIsMainTeacher(Long teacherId, Long classId, Boolean isMainTeacher);
}