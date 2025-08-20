package me.synology.techrevive.teacher.resources;

import jakarta.validation.Valid;
import me.synology.techrevive.teacher.resources.dto.UpdateUsernameRequest;
import me.synology.techrevive.teacher.resources.dto.UserResponse;
import me.synology.techrevive.teacher.security.GoogleTokenAuthentication;
import me.synology.techrevive.teacher.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserEndpoint {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<UserResponse> getUserFromToken(Authentication authentication) {
        GoogleTokenAuthentication googleAuth = (GoogleTokenAuthentication) authentication;
        String accessToken = googleAuth.getCredentials().toString();
        
        UserResponse user = userService.getUserFromToken(accessToken);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<UserResponse> createUserFromToken(
            Authentication authentication,
            @RequestBody @Valid UpdateUsernameRequest request) {
        GoogleTokenAuthentication googleAuth = (GoogleTokenAuthentication) authentication;
        String accessToken = googleAuth.getCredentials().toString();
        
        UserResponse user = userService.createUserFromToken(accessToken, request.username());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    @PutMapping("/username")
    public ResponseEntity<UserResponse> updateUsername(
            Authentication authentication,
            @RequestBody @Valid UpdateUsernameRequest request) {
        
        GoogleTokenAuthentication googleAuth = (GoogleTokenAuthentication) authentication;
        String googleId = googleAuth.getGoogleId();
        
        UserResponse updatedUser = userService.updateUsername(googleId, request.username());
        return ResponseEntity.ok(updatedUser);
    }
}