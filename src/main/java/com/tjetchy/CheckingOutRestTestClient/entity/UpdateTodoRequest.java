package com.tjetchy.CheckingOutRestTestClient.entity;

public record UpdateTodoRequest(
        String title,
        boolean completed
) {
}
