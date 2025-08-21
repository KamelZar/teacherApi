package me.synology.techrevive.teacher.resources.dto;

import me.synology.techrevive.teacher.services.dto.ClassData;

import java.time.LocalDateTime;

public record ClassResponse(
    Long id,
    String name,
    String description,
    Integer schoolYear,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ClassResponse from(ClassData classData) {
        return new ClassResponse(
            classData.id(),
            classData.name(),
            classData.description(),
            classData.schoolYear(),
            classData.createdAt(),
            classData.updatedAt()
        );
    }
}