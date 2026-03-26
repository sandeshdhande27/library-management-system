package com.sandesh.library.service;

import com.sandesh.library.entity.Membership;
import com.sandesh.library.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    // 1️⃣ Add new membership
    public String addMembership(Membership membership) {

        // 🔹 Validation
        if (membership.getMembershipType() == null ||
            membership.getStartDate() == null ||
            membership.getEndDate() == null ||
            membership.getUserId() == null) {

            return "Missing required fields";
        }

        membershipRepository.save(membership);
        return "Membership added successfully";
    }

    // 2️⃣ Get all memberships
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    // 3️⃣ Get memberships by userId (FIXED - returns List)
    public List<Membership> getMembershipByUserId(Long userId) {

        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        return membershipRepository.findByUserId(userId);
    }

    // 4️⃣ Update membership
    public String updateMembership(Long membershipId, String type, Date startDate, Date endDate) {

        Optional<Membership> optional = membershipRepository.findById(membershipId);

        if (optional.isEmpty()) {
            return "Membership not found";
        }

        Membership membership = optional.get();

        // 🔹 Update only if values provided
        if (type != null) {
            membership.setMembershipType(type);
        }

        if (startDate != null) {
            membership.setStartDate(startDate);
        }

        if (endDate != null) {
            membership.setEndDate(endDate);
        }

        membershipRepository.save(membership);
        return "Membership updated successfully";
    }

    // 5️⃣ Delete membership
    public String deleteMembership(Long membershipId) {

        if (membershipId == null) {
            return "MembershipId is required";
        }

        if (!membershipRepository.existsById(membershipId)) {
            return "Membership not found";
        }

        membershipRepository.deleteById(membershipId);
        return "Membership deleted successfully";
    }
}