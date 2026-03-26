package com.sandesh.library.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "membership")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String membershipType; // ✅ ADDED

    private Date startDate;

    private Date endDate;

    // 🔹 Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMembershipType() {   // ✅ FIXED
        return membershipType;
    }

    public void setMembershipType(String membershipType) {   // ✅ FIXED
        this.membershipType = membershipType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}