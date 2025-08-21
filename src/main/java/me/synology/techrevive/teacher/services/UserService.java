package me.synology.techrevive.teacher.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.exceptions.InvalidTokenException;
import me.synology.techrevive.teacher.exceptions.NotFoundException;
import me.synology.techrevive.teacher.repositories.UserRepository;
import me.synology.techrevive.teacher.resources.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GoogleService googleService;
    
    public UserResponse getUserFromToken(String accessToken) {
        try {
            String googleId = googleService.getGoogleIdFromToken(accessToken);
            
            Optional<User> existingUser = userRepository.findByGoogleId(googleId);
            if (existingUser.isPresent()) {
                return UserResponse.from(existingUser.get());
            }
            throw new NotFoundException("User not found with Google ID: " + googleId);
        } catch (NotFoundException | InvalidTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidTokenException("Error validating Google access token: " + e.getMessage());
        }
    }
    
    public UserResponse createUserFromToken(String accessToken, String username) {
        try {
            String googleId = googleService.getGoogleIdFromToken(accessToken);
            
            // Vérifier si l'utilisateur existe déjà
            Optional<User> existingUser = userRepository.findByGoogleId(googleId);
            if (existingUser.isPresent()) {
                return UserResponse.from(existingUser.get());
            }
            
            // Créer un nouvel utilisateur avec le username fourni
            User newUser = new User(googleId, username);
            User savedUser = userRepository.save(newUser);
            return UserResponse.from(savedUser);
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
        return googleService.validateToken(accessToken);
    }
    
    public String getGoogleIdFromToken(String accessToken) {
        return googleService.getGoogleIdFromToken(accessToken);
    }
}