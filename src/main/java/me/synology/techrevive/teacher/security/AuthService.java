package me.synology.techrevive.teacher.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.synology.techrevive.teacher.entities.RefreshToken;
import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.repositories.RefreshTokenRepository;
import me.synology.techrevive.teacher.resources.dto.auth.AuthResponse;
import me.synology.techrevive.teacher.resources.dto.auth.GoogleAuthRequest;
import me.synology.techrevive.teacher.resources.dto.auth.RefreshTokenRequest;
import me.synology.techrevive.teacher.services.GoogleService;
import me.synology.techrevive.teacher.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class  AuthService {

    private final JwtService jwtService;
    private final GoogleService googleService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration;

    /**
     * Authentification via Google OAuth
     */
    @Transactional
    public AuthResponse authenticateWithGoogle(GoogleAuthRequest request) {
        log.info("Attempting Google authentication");
        
        // 1. Valider le token Google
        if (!googleService.validateToken(request.getGoogleToken())) {
            throw new IllegalArgumentException("Invalid Google token");
        }

        // 2. Récupérer les infos utilisateur depuis Google
        String googleId = googleService.getGoogleIdFromToken(request.getGoogleToken());
        var payload = googleService.getGoogleUserInfo(request.getGoogleToken());
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");

        // 3. Créer ou récupérer l'utilisateur
        User user = userService.findOrCreateUser(googleId, email, name, pictureUrl);
        
        // 4. Révoquer les anciens refresh tokens de cet utilisateur (optionnel pour sécurité)
        // refreshTokenRepository.revokeAllTokensByUserId(user.getId());

        // 5. Générer les nouveaux tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenValue = generateUniqueRefreshToken();

        // 6. Sauvegarder le refresh token en base
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiresAt(jwtService.calculateRefreshTokenExpiration())
                .deviceInfo(request.getDeviceInfo())
                .revoked(false)
                .build();
        
        refreshTokenRepository.save(refreshToken);

        log.info("Google authentication successful for user: {}", user.getEmail());

        // 7. Retourner la réponse complète
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration / 1000) // Convertir ms -> secondes
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .pictureUrl(user.getPictureUrl())
                .build();
    }

    /**
     * Refresh des tokens
     */
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Attempting token refresh");

        // 1. Récupérer le refresh token depuis la DB
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository
                .findValidToken(request.getRefreshToken(), LocalDateTime.now());

        if (refreshTokenOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        RefreshToken refreshToken = refreshTokenOpt.get();
        User user = refreshToken.getUser();

        // 2. Révoquer l'ancien refresh token
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        // 3. Générer de nouveaux tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshTokenValue = generateUniqueRefreshToken();

        // 4. Créer le nouveau refresh token
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRefreshTokenValue)
                .user(user)
                .expiresAt(jwtService.calculateRefreshTokenExpiration())
                .deviceInfo(refreshToken.getDeviceInfo())
                .revoked(false)
                .build();

        refreshTokenRepository.save(newRefreshToken);

        log.info("Token refresh successful for user: {}", user.getEmail());

        // 5. Retourner la réponse
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration / 1000)
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .pictureUrl(user.getPictureUrl())
                .build();
    }

    /**
     * Révocation d'un refresh token
     */
    @Transactional
    public void revokeRefreshToken(String token) {
        log.info("Revoking refresh token");
        refreshTokenRepository.revokeToken(token);
    }

    /**
     * Révocation de tous les tokens d'un utilisateur
     */
    @Transactional
    public void revokeAllUserTokens(Long userId) {
        log.info("Revoking all tokens for user: {}", userId);
        refreshTokenRepository.revokeAllTokensByUserId(userId);
    }

    /**
     * Nettoyage des tokens expirés (à appeler périodiquement)
     */
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Cleaning up expired refresh tokens");
        int deletedCount = refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Deleted {} expired refresh tokens", deletedCount);
    }

    /**
     * Génère un refresh token unique
     */
    private String generateUniqueRefreshToken() {
        String token;
        do {
            token = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
        } while (refreshTokenRepository.existsByToken(token));
        return token;
    }
}