package com.sandesh.library.DTO;

import jakarta.validation.constraints.NotNull;

public class DeleteMembershipRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    // Getter & Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}