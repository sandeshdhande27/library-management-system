package com.sandesh.library.service;

import com.sandesh.library.entity.Book;
import com.sandesh.library.entity.BookTransaction;
import com.sandesh.library.repository.BookRepository;
import com.sandesh.library.repository.BookTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private BookTransactionRepository transactionRepository;

    @Autowired
    private BookRepository bookRepository;

    public Map<String, Object> getUserHistory(Long userId) {

        List<BookTransaction> current = transactionRepository.findByUserIdAndStatus(userId, "Issued");
        List<BookTransaction> past = transactionRepository.findByUserIdAndStatus(userId, "Returned");

        List<Map<String, Object>> currentBooks = new ArrayList<>();
        List<Map<String, Object>> pastBooks = new ArrayList<>();

        // 🔹 Current Books
        for (BookTransaction t : current) {
            Optional<Book> bookOpt = bookRepository.findById(t.getBookId());

            if (bookOpt.isPresent()) {
                Book b = bookOpt.get();

                Map<String, Object> map = new HashMap<>();
                map.put("bookName", b.getBookName());
                map.put("issueDate", t.getIssueDate());
                map.put("returnDate", t.getReturnDate()); // optional
                map.put("status", t.getStatus());

                currentBooks.add(map);
            }
        }

        // 🔹 Past Books
        for (BookTransaction t : past) {
            Optional<Book> bookOpt = bookRepository.findById(t.getBookId());

            if (bookOpt.isPresent()) {
                Book b = bookOpt.get();

                Map<String, Object> map = new HashMap<>();
                map.put("bookName", b.getBookName());
                map.put("returnDate", t.getReturnDate());
                map.put("returnDate", t.getReturnDate());
                map.put("status", t.getStatus());

                pastBooks.add(map);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("current_books", currentBooks);
        response.put("past_books", pastBooks);
        response.put("message", "User history retrieved successfully");

        return response;
    }
}