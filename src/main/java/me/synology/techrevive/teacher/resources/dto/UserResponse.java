package me.synology.techrevive.teacher.resources.dto;

import me.synology.techrevive.teacher.entities.User;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String googleId,
    String email,
    String name,
    String pictureUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getGoogleId(),
            user.getEmail(),
            user.getName(),
            user.getPictureUrl(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}