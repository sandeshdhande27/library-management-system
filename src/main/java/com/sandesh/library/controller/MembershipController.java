package com.sandesh.library.controller;

import com.sandesh.library.entity.Membership;
import com.sandesh.library.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    // 1️⃣ Add Membership (POST)
    @PostMapping("/add")
    public String addMembership(@RequestBody Membership membership) {
        return membershipService.addMembership(membership);
    }

    // 2️⃣ Get All Memberships (POST - as per your requirement)
    @PostMapping("/get-all")
    public List<Membership> getAllMemberships() {
        return membershipService.getAllMemberships();
    }

    // 3️⃣ Get Membership by UserId (POST)
@PostMapping("/get-by-user")
public List<Membership> getByUserId(@RequestParam Long userId) {
    return membershipService.getMembershipByUserId(userId);
}

    // 4️⃣ Update Membership (POST)
    @PostMapping("/update")
    public String updateMembership(
            @RequestParam Long membershipId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate
    ) {
        return membershipService.updateMembership(
                membershipId,
                type,
                startDate != null ? new java.util.Date(startDate) : null,
                endDate != null ? new java.util.Date(endDate) : null
        );
    }

    // 5️⃣ Delete Membership (POST)
    @PostMapping("/delete")
    public String deleteMembership(@RequestParam Long membershipId) {
        return membershipService.deleteMembership(membershipId);
    }
}