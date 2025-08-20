package me.synology.techrevive.teacher.resources.dto;

import jakarta.validation.constraints.NotNull;

public record UserRequest(@NotNull String accessToken) {
}