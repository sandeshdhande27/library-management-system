package com.sandesh.library.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "email") // optional but safe
    private String email;

    private String password;

    private Date createdAt;

    private Date membershipStartDate;

    private Date membershipEndDate;

    private Integer membershipMonths;

    // Getters & Setters

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getMembershipStartDate() { return membershipStartDate; }
    public void setMembershipStartDate(Date membershipStartDate) { this.membershipStartDate = membershipStartDate; }

    public Date getMembershipEndDate() { return membershipEndDate; }
    public void setMembershipEndDate(Date membershipEndDate) { this.membershipEndDate = membershipEndDate; }

    public Integer getMembershipMonths() {
    return membershipMonths;
}

public void setMembershipMonths(Integer membershipMonths) {
    this.membershipMonths = membershipMonths;
}
}