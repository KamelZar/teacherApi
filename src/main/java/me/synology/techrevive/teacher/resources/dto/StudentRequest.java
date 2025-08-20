package me.synology.techrevive.teacher.resources.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StudentRequest(@NotNull String firstName, @NotNull String lastName, LocalDate birthDate) {
}
