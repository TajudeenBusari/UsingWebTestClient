package com.tjetchy.CheckingOutRestTestClient.entity;

public record TodoDto(
        String id,
        String title,
        boolean completed
) {
}
