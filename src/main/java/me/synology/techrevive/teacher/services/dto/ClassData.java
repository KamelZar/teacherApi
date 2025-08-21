package me.synology.techrevive.teacher.services.dto;

import me.synology.techrevive.teacher.entities.ClassEntity;

import java.time.LocalDateTime;

public record ClassData(
    Long id,
    String name,
    String description,
    Integer schoolYear,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ClassData from(ClassEntity entity) {
        return new ClassData(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getSchoolYear(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}