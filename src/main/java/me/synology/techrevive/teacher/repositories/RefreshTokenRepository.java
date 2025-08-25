package me.synology.techrevive.teacher.repositories;

import me.synology.techrevive.teacher.entities.RefreshToken;
import me.synology.techrevive.teacher.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // Trouver un refresh token par sa valeur
    Optional<RefreshToken> findByToken(String token);
    
    // Trouver tous les tokens d'un utilisateur
    List<RefreshToken> findByUser(User user);
    
    // Trouver tous les tokens d'un utilisateur par ID
    List<RefreshToken> findByUserId(Long userId);
    
    // Trouver tous les tokens valides d'un utilisateur (non révoqués et non expirés)
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user.id = :userId AND rt.revoked = false AND rt.expiresAt > :now")
    List<RefreshToken> findValidTokensByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    // Vérifier si un token existe et est valide
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.token = :token AND rt.revoked = false AND rt.expiresAt > :now")
    Optional<RefreshToken> findValidToken(@Param("token") String token, @Param("now") LocalDateTime now);
    
    // Révoquer tous les tokens d'un utilisateur
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId")
    int revokeAllTokensByUserId(@Param("userId") Long userId);
    
    // Révoquer un token spécifique
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.token = :token")
    int revokeToken(@Param("token") String token);
    
    // Supprimer tous les tokens expirés
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    int deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    // Supprimer tous les tokens révoqués
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.revoked = true")
    int deleteRevokedTokens();
    
    // Compter les tokens actifs d'un utilisateur
    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.user.id = :userId AND rt.revoked = false AND rt.expiresAt > :now")
    long countActiveTokensByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    // Trouver les tokens créés dans une période
    List<RefreshToken> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Vérifier si un token existe
    boolean existsByToken(String token);
    
    // Ordonner par date de création (plus récents en premier)
    List<RefreshToken> findByUserIdOrderByCreatedAtDesc(Long userId);
}