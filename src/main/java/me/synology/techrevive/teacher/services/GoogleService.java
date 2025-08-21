package me.synology.techrevive.teacher.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import me.synology.techrevive.teacher.config.GoogleOAuthProperties;
import me.synology.techrevive.teacher.exceptions.InvalidTokenException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleService {
    
    private final GoogleIdTokenVerifier verifier;
    
    public GoogleService(GoogleOAuthProperties properties) {
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(properties.clientId()))
                .build();
    }
    
    public boolean validateToken(String accessToken) {
        try {
            GoogleIdToken idToken = verifier.verify(accessToken);
            return idToken != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getGoogleIdFromToken(String accessToken) {
        try {
            GoogleIdToken idToken = verifier.verify(accessToken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                return payload.getSubject();
            }
            throw new InvalidTokenException("Invalid Google access token");
        } catch (InvalidTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidTokenException("Error validating Google access token: " + e.getMessage());
        }
    }
    
    public GoogleIdToken.Payload getGoogleUserInfo(String accessToken) {
        try {
            GoogleIdToken idToken = verifier.verify(accessToken);
            if (idToken != null) {
                return idToken.getPayload();
            }
            throw new InvalidTokenException("Invalid Google access token");
        } catch (InvalidTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidTokenException("Error validating Google access token: " + e.getMessage());
        }
    }
}