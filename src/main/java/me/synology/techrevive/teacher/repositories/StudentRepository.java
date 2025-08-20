package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}