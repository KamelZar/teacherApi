package me.synology.techrevive.teacher.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GoogleTokenAuthenticationProvider implements AuthenticationProvider {
    
    private final GoogleIdTokenVerifier verifier;
    
    public GoogleTokenAuthenticationProvider() {
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList("YOUR_CLIENT_ID")) // À configurer
                .build();
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof GoogleTokenAuthentication)) {
            return null;
        }
        
        GoogleTokenAuthentication googleAuth = (GoogleTokenAuthentication) authentication;
        String token = googleAuth.getToken();
        
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String googleId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                
                return new GoogleTokenAuthentication(token, googleId, email, name);
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