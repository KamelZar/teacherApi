package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Méthodes automatiques JPA pour authentification
    Optional<User> findByGoogleId(String googleId);
    boolean existsByGoogleId(String googleId);

    // Recherche par email
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // Trouver par rôle
    List<User> findByRole(UserRole role);

    // Recherche par nom (partielle, insensible à la casse)
    List<User> findByNameContainingIgnoreCase(String namePart);

    // Compter par rôle
    long countByRole(UserRole role);

    // Trouver professeurs
    default List<User> findTeachers() {
        return findByRole(UserRole.TEACHER);
    }

    // Trouver élèves
    default List<User> findStudents() {
        return findByRole(UserRole.STUDENT);
    }
}