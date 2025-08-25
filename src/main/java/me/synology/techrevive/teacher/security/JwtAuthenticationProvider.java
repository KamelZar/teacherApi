package me.synology.techrevive.teacher.security;

import me.synology.techrevive.teacher.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private JwtService jwtService;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof JwtAuthentication)) {
            return null;
        }
        
        JwtAuthentication jwtAuth = (JwtAuthentication) authentication;
        String token = jwtAuth.getToken();
        
        try {
            // Valider le token JWT
            if (!jwtService.isTokenValid(token)) {
                throw new BadCredentialsException("Invalid JWT token");
            }
            
            // Extraire les informations du token
            Long userId = jwtService.extractUserId(token);
            String email = jwtService.extractEmail(token);
            String roleString = jwtService.extractRole(token);
            UserRole role = UserRole.valueOf(roleString);
            
            return new JwtAuthentication(token, userId, email, role);
            
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid JWT token: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }
}