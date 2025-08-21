package me.synology.techrevive.teacher.resources.dto;

public record ClassUpdateRequest(
    String name,
    String description
) {}