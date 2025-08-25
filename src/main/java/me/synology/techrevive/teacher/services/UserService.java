package me.synology.techrevive.teacher.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import me.synology.techrevive.teacher.entities.AuthProvider;
import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.entities.UserRole;
import me.synology.techrevive.teacher.exceptions.InvalidTokenException;
import me.synology.techrevive.teacher.exceptions.NotFoundException;
import me.synology.techrevive.teacher.repositories.UserRepository;
import me.synology.techrevive.teacher.resources.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            
            // Créer un nouvel utilisateur 
            User newUser = new User();
            newUser.setGoogleId(googleId);
            newUser.setName(username);  // name au lieu de username
            newUser.setRole(me.synology.techrevive.teacher.entities.UserRole.STUDENT); // rôle par défaut
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
            user.setName(username);
            User savedUser = userRepository.save(user);
            return UserResponse.from(savedUser);
        }
        throw new NotFoundException("User not found with Google ID: " + googleId);
    }
    
    public boolean validateToken(String accessToken) {
        return googleService.validateToken(accessToken);
    }
    
    /**
     * Trouve ou crée un utilisateur à partir des infos Google
     */
    public User findOrCreateUser(String googleId, String email, String name, String pictureUrl) {
        Optional<User> existingUser = userRepository.findByGoogleId(googleId);
        
        if (existingUser.isPresent()) {
            // Utilisateur existant - mettre à jour les infos si nécessaire
            User user = existingUser.get();
            boolean needsUpdate = false;
            
            if (!email.equals(user.getEmail())) {
                user.setEmail(email);
                needsUpdate = true;
            }
            if (!name.equals(user.getName())) {
                user.setName(name);
                needsUpdate = true;
            }
            if (pictureUrl != null && !pictureUrl.equals(user.getPictureUrl())) {
                user.setPictureUrl(pictureUrl);
                needsUpdate = true;
            }
            
            if (needsUpdate) {
                user.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(user);
            }
            
            return user;
        } else {
            // Nouvel utilisateur
            User newUser = new User();
            newUser.setGoogleId(googleId);
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setPictureUrl(pictureUrl);
            newUser.setRole(UserRole.STUDENT); // Rôle par défaut
            newUser.setAuthProvider(AuthProvider.GOOGLE); // Provider Google
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            
            return userRepository.save(newUser);
        }
    }
    
    /**
     * Met à jour le nom d'utilisateur par ID
     */
    public UserResponse updateUsernameById(Long userId, String username) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(username);
            user.setUpdatedAt(LocalDateTime.now());
            User savedUser = userRepository.save(user);
            return UserResponse.from(savedUser);
        }
        throw new NotFoundException("User not found with ID: " + userId);
    }
    
    public String getGoogleIdFromToken(String accessToken) {
        return googleService.getGoogleIdFromToken(accessToken);
    }
    
    /**
     * Récupère un utilisateur par son ID
     */
    public UserResponse getUserById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return UserResponse.from(userOpt.get());
        }
        throw new NotFoundException("User not found with ID: " + userId);
    }
}