package me.synology.techrevive.teacher.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.synology.techrevive.teacher.entities.AuthProvider;
import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.entities.UserRole;
import me.synology.techrevive.teacher.resources.dto.auth.AuthResponse;
import me.synology.techrevive.teacher.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Tag(name = "Hello", description = "Public hello endpoint with Easter egg")
public class HelloEndpoint {
    
    @Autowired
    private JwtService jwtService;
    
    @GetMapping("/hello")
    @Operation(summary = "Get hello message", description = "Returns a simple hello world message")
    @ApiResponse(responseCode = "200", description = "Hello message returned successfully")
    public String get(){
        return "Hello World";
    }
    
    @GetMapping("/hello/token")
    @Operation(summary = "Easter egg - Demo JWT token", 
               description = "🥚 Generates a JWT token for a fake demo user (for testing protected endpoints)")
    @ApiResponse(responseCode = "200", description = "Demo JWT token generated")
    public AuthResponse getDemoToken(){
        // Créer un faux utilisateur pour les tests
        User demoUser = new User();
        demoUser.setId(999L);
        demoUser.setGoogleId("demo-google-id-12345");
        demoUser.setEmail("demo@teacher.app");
        demoUser.setName("Demo Teacher");
        demoUser.setPictureUrl("https://via.placeholder.com/100x100?text=Demo");
        demoUser.setRole(UserRole.TEACHER);
        demoUser.setAuthProvider(AuthProvider.CUSTOM);
        demoUser.setCreatedAt(LocalDateTime.now());
        demoUser.setUpdatedAt(LocalDateTime.now());
        
        // Générer les tokens
        String accessToken = jwtService.generateAccessToken(demoUser);
        String refreshToken = "demo-refresh-token-" + System.currentTimeMillis();
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L) // 15 minutes
                .userId(demoUser.getId())
                .email(demoUser.getEmail())
                .name(demoUser.getName())
                .role(demoUser.getRole())
                .pictureUrl(demoUser.getPictureUrl())
                .build();
    }
}
