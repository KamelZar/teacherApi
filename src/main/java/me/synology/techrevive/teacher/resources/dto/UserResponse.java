package me.synology.techrevive.teacher.resources.dto;

import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.entities.UserRole;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String email,
    String name,
    UserRole role,
    String pictureUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole(),
            user.getPictureUrl(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}