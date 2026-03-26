package com.sandesh.library.DTO;

import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class UpdateMembershipRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    private Date startDate; // optional
    private Date endDate;   // optional

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}