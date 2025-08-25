package me.synology.techrevive.teacher.resources.dto.auth;

import me.synology.techrevive.teacher.entities.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long expiresIn; // en secondes
    
    // Informations utilisateur
    private Long userId;
    private String email;
    private String name;
    private UserRole role;
    private String pictureUrl;
}