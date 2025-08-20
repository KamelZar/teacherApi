package me.synology.techrevive.teacher.resources.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUsernameRequest(
    @NotNull 
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    String username
) {
}