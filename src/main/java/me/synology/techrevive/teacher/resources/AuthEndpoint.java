package me.synology.techrevive.teacher.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.synology.techrevive.teacher.resources.dto.auth.AuthResponse;
import me.synology.techrevive.teacher.resources.dto.auth.GoogleAuthRequest;
import me.synology.techrevive.teacher.resources.dto.auth.RefreshTokenRequest;
import me.synology.techrevive.teacher.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management endpoints")
public class AuthEndpoint {

    @Autowired
    private AuthService authService;

    @PostMapping("/google")
    @Operation(summary = "Google OAuth Authentication", description = "Exchange Google access token for internal JWT tokens")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "400", description = "Invalid Google token"),
        @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    public ResponseEntity<AuthResponse> authenticateWithGoogle(
            @RequestBody @Valid GoogleAuthRequest request) {
        AuthResponse response = authService.authenticateWithGoogle(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT Token", description = "Refresh access token using refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
        @ApiResponse(responseCode = "401", description = "Refresh token expired or revoked")
    })
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody @Valid RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}