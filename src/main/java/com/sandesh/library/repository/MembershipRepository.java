package com.sandesh.library.repository;

import com.sandesh.library.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Membership m SET m.startDate = :startDate, m.endDate = :endDate WHERE m.userId = :userId")
    int updateMembership(Long userId, java.util.Date startDate, java.util.Date endDate);

    @Modifying
    @Transactional
    @Query("DELETE FROM Membership m WHERE m.userId = :userId")
    int deleteMembershipByUserId(Long userId);
}