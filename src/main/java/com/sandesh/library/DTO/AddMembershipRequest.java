package com.sandesh.library.DTO;

import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class AddMembershipRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "startDate is required")
    private Date startDate;

    @NotNull(message = "endDate is required")
    private Date endDate;

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}