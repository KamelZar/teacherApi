package me.synology.techrevive.teacher.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.exceptions.InvalidTokenException;
import me.synology.techrevive.teacher.exceptions.NotFoundException;
import me.synology.techrevive.teacher.repositories.UserRepository;
import me.synology.techrevive.teacher.resources.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    private final GoogleIdTokenVerifier verifier;
    
    public UserService() {
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList("YOUR_CLIENT_ID")) // À configurer
                .build();
    }
    
    public UserResponse getUserFromToken(String accessToken) {
        try {
            GoogleIdToken idToken = verifier.verify(accessToken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String googleId = payload.getSubject();
                
                Optional<User> existingUser = userRepository.findByGoogleId(googleId);
                if (existingUser.isPresent()) {
                    return UserResponse.from(existingUser.get());
                }
                throw new NotFoundException("User not found with Google ID: " + googleId);
            }
            throw new InvalidTokenException("Invalid Google access token");
        } catch (NotFoundException | InvalidTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidTokenException("Error validating Google access token: " + e.getMessage());
        }
    }
    
    public UserResponse createUserFromToken(String accessToken, String username) {
        try {
            GoogleIdToken idToken = verifier.verify(accessToken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                
                String googleId = payload.getSubject();
                
                // Vérifier si l'utilisateur existe déjà
                Optional<User> existingUser = userRepository.findByGoogleId(googleId);
                if (existingUser.isPresent()) {
                    return UserResponse.from(existingUser.get());
                }
                
                // Créer un nouvel utilisateur avec le username fourni
                User newUser = new User(googleId, username);
                User savedUser = userRepository.save(newUser);
                return UserResponse.from(savedUser);
            }
            throw new InvalidTokenException("Invalid Google access token");
        } catch (InvalidTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidTokenException("Error validating Google access token: " + e.getMessage());
        }
    }
    
    public UserResponse updateUsername(String googleId, String username) {
        Optional<User> userOpt = userRepository.findByGoogleId(googleId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(username);
            User savedUser = userRepository.save(user);
            return UserResponse.from(savedUser);
        }
        throw new NotFoundException("User not found with Google ID: " + googleId);
    }
    
    public boolean validateToken(String accessToken) {
        try {
            GoogleIdToken idToken = verifier.verify(accessToken);
            return idToken != null;
        } catch (Exception e) {
            return false;
        }
    }
}