package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    
    List<ClassEntity> findBySchoolYear(Integer schoolYear);
    
    @Query("SELECT c FROM ClassEntity c JOIN ClassTeacher ct ON c.id = ct.classId WHERE ct.teacherId = :teacherId")
    List<ClassEntity> findClassesByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT c FROM ClassEntity c JOIN ClassStudent cs ON c.id = cs.classId WHERE cs.studentId = :studentId")
    List<ClassEntity> findClassesByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT c FROM ClassEntity c JOIN ClassTeacher ct ON c.id = ct.classId WHERE ct.teacherId = :teacherId AND ct.isMainTeacher = true")
    List<ClassEntity> findClassesByMainTeacherId(@Param("teacherId") Long teacherId);
    
    boolean existsByNameAndSchoolYear(String name, Integer schoolYear);
}