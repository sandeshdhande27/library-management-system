package com.sandesh.library.service;

import com.sandesh.library.entity.Book;
import com.sandesh.library.entity.BookTransaction;
import com.sandesh.library.repository.BookRepository;
import com.sandesh.library.repository.BookTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {

    @Autowired
    private BookTransactionRepository transactionRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Map<String, Object>> getTopCategories() {

        List<BookTransaction> transactions = transactionRepository.findByStatus("Returned");

        Map<String, Integer> categoryCount = new HashMap<>();

        int total = transactions.size();

        // 🔹 Count per category
        for (BookTransaction t : transactions) {

            Optional<Book> bookOpt = bookRepository.findById(t.getBookId());

            if (bookOpt.isPresent()) {
                String category = bookOpt.get().getBookCategory();

                categoryCount.put(category,
                        categoryCount.getOrDefault(category, 0) + 1);
            }
        }

        // 🔹 Convert to percentage
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {

            Map<String, Object> map = new HashMap<>();

            double percentage = (entry.getValue() * 100.0) / total;

            map.put("category", entry.getKey());
            map.put("percentage", percentage);

            result.add(map);
        }

        return result;
    }
}