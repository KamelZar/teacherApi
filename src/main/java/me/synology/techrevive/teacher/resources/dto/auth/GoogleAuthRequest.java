package me.synology.techrevive.teacher.resources.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthRequest {
    
    @NotBlank(message = "Google token is required")
    private String googleToken;
    
    private String deviceInfo;
}