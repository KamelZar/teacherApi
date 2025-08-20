package me.synology.techrevive.teacher.resources.dto;

import me.synology.techrevive.teacher.entities.User;

public record UserResponse(
    Long id,
    String username
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername()
        );
    }
}