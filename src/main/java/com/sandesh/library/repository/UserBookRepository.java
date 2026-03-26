package com.sandesh.library.repository;

import com.sandesh.library.entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
}