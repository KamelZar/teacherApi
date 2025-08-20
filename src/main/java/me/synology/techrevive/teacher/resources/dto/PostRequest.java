package me.synology.techrevive.teacher.resources.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public record PostRequest (String user, String content, @Valid @Min(0) Integer value){}
