package com.tjetchy.CheckingOutRestTestClient.entity;

public record AddToRequest(
        String title,
        boolean completed
) {
}
