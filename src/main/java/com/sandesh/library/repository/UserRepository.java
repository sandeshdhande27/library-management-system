package com.sandesh.library.repository;

import com.sandesh.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :userId")
Optional<User> findUserById(Long userId);


@Query("SELECT u FROM User u WHERE u.email = :email")
Optional<User> findUserByEmailForDashboard(String email);


}