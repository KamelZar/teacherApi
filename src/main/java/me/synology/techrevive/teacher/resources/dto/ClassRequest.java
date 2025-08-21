package me.synology.techrevive.teacher.resources.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record ClassRequest(
    @NotBlank(message = "Class name is required")
    String name,
    
    String description,
    
    @NotNull(message = "School year is required")
    @Min(value = 2000, message = "School year must be at least 2000")
    Integer schoolYear
) {}