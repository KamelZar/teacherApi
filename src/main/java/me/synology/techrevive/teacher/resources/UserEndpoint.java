package me.synology.techrevive.teacher.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.synology.techrevive.teacher.resources.dto.UpdateUsernameRequest;
import me.synology.techrevive.teacher.resources.dto.UserResponse;
import me.synology.techrevive.teacher.security.JwtAuthentication;
import me.synology.techrevive.teacher.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "User management endpoints")
@SecurityRequirement(name = "Google OAuth")
public class UserEndpoint {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @Operation(summary = "Get current user", description = "Retrieve current user information from Google access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing access token"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> getUserFromToken(
            @Parameter(hidden = true) Authentication authentication) {
        JwtAuthentication jwtAuth = (JwtAuthentication) authentication;
        String accessToken = jwtAuth.getCredentials().toString();
        
        UserResponse user = userService.getUserFromToken(accessToken);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    @Operation(summary = "Create new user", description = "Create a new user from Google access token with username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "200", description = "User already exists"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing access token"),
        @ApiResponse(responseCode = "400", description = "Invalid username")
    })
    public ResponseEntity<UserResponse> createUserFromToken(
            @Parameter(hidden = true) Authentication authentication,
            @RequestBody @Valid UpdateUsernameRequest request) {
        JwtAuthentication jwtAuth = (JwtAuthentication) authentication;
        String accessToken = jwtAuth.getCredentials().toString();
        
        UserResponse user = userService.createUserFromToken(accessToken, request.username());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    @PutMapping("/username")
    @Operation(summary = "Update username", description = "Update the username for the current user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Username updated successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing access token"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid username")
    })
    public ResponseEntity<UserResponse> updateUsername(
            @Parameter(hidden = true) Authentication authentication,
            @RequestBody @Valid UpdateUsernameRequest request) {
        
        JwtAuthentication jwtAuth = (JwtAuthentication) authentication;
        Long userId = jwtAuth.getUserId();
        
        UserResponse updatedUser = userService.updateUsernameById(userId, request.username());
        return ResponseEntity.ok(updatedUser);
    }
}