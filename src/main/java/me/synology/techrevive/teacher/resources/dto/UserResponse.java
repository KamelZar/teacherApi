package me.synology.techrevive.teacher.resources.dto;

import me.synology.techrevive.teacher.entities.User;

public record UserResponse(
    Long id,
    String name
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName()
        );
    }
}