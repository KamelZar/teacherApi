package me.synology.techrevive.teacher.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "google")
@Validated
public record GoogleOAuthProperties(
    @NotBlank(message = "Google Client ID is required")
    String clientId
) {
}