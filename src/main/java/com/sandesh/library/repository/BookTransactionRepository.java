package com.sandesh.library.repository;

import com.sandesh.library.entity.BookTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookTransactionRepository extends JpaRepository<BookTransaction, Long> {

    List<BookTransaction> findByUserId(Long userId);

    List<BookTransaction> findByUserIdAndStatus(Long userId, String status);

    List<BookTransaction> findByStatus(String status);

    BookTransaction findByUserIdAndBookIdAndStatus(Long userId, Long bookId, String status);

}