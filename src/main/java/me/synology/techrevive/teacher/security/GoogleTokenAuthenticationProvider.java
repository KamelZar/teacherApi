package me.synology.techrevive.teacher.security;

import me.synology.techrevive.teacher.services.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private GoogleService googleService;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof GoogleTokenAuthentication)) {
            return null;
        }
        
        GoogleTokenAuthentication googleAuth = (GoogleTokenAuthentication) authentication;
        String token = googleAuth.getToken();
        
        try {
            if (googleService.validateToken(token)) {
                String googleId = googleService.getGoogleIdFromToken(token);
                return new GoogleTokenAuthentication(token, googleId);
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid Google token", e);
        }
        
        throw new BadCredentialsException("Invalid Google token");
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return GoogleTokenAuthentication.class.isAssignableFrom(authentication);
    }
}