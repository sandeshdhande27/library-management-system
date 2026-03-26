package com.sandesh.library.service;

import com.sandesh.library.DTO.BookFilterRequest;
import com.sandesh.library.DTO.DeleteBookRequest;
import com.sandesh.library.DTO.UpdateBookRequest;
import com.sandesh.library.entity.Book;
import com.sandesh.library.entity.BookTransaction;
import com.sandesh.library.repository.BookRepository;
import com.sandesh.library.repository.BookTransactionRepository;
import com.sandesh.library.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandesh.library.entity.User;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
private BookTransactionRepository transactionRepository;


public String addBook(Book book) {

    // Required field validation
    if (book.getBookName() == null || 
        book.getBookAuthor() == null || 
        book.getBookCategory() == null) {
        return "Missing required fields";
    }

    // ❌ Reject if user tries to send TAKEN
    if (book.getBookStatus() != null && 
        !book.getBookStatus().equalsIgnoreCase("AVAILABLE")) {
        return "Invalid status";
    }

    // ✅ Always enforce AVAILABLE
    book.setBookStatus("AVAILABLE");

    book.setCreatedAt(new Date());

    bookRepository.save(book);

    return "Book added successfully";
}



public List<Book> getAllBooks() {
    return bookRepository.findAll();
}



public List<Book> getSpecificBooks(BookFilterRequest request) {

    List<Book> books = bookRepository.findAll();

    return books.stream()
            .filter(b -> request.getBookName() == null || 
                         b.getBookName().equalsIgnoreCase(request.getBookName()))
            .filter(b -> request.getBookCategory() == null || 
                         b.getBookCategory().equalsIgnoreCase(request.getBookCategory()))
            .filter(b -> request.getBookStatus() == null || 
                         b.getBookStatus().equalsIgnoreCase(request.getBookStatus()))
            .toList();
}


@Autowired
private UserRepository userRepository;

public String takeBook(Long bookId, Long userId, int days) {

    if (days <= 0) {
        return "Invalid number of days";
    }

    Book book = bookRepository.findById(bookId).orElse(null);
    if (book == null) {
        return "Book not found";
    }

    if (!"AVAILABLE".equalsIgnoreCase(book.getBookStatus())) {
        return "Book already taken";
    }

    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
        return "User not found";
    }

    Date now = new Date();

    if (user.getMembershipEndDate() == null || user.getMembershipEndDate().before(now)) {
        return "Membership expired";
    }

    // ✅ Update book
    book.setBookStatus("TAKEN");
    book.setTakenByUserId(userId);

    Calendar cal = Calendar.getInstance();
    cal.setTime(now);
    cal.add(Calendar.DAY_OF_MONTH, days);

    Date returnDate = cal.getTime();
    book.setReturnDate(returnDate);

    bookRepository.save(book);

    // 🔥 ADD THIS BLOCK (IMPORTANT)
    BookTransaction transaction = new BookTransaction();
    transaction.setUserId(userId);
    transaction.setBookId(bookId);
    transaction.setIssueDate(now);
    transaction.setReturnDate(returnDate);
    transaction.setStatus("Issued");

    transactionRepository.save(transaction);

    return "Book issued successfully";
}

public String returnBook(Long bookId) {

    // 1️⃣ Get book
    Book book = bookRepository.findById(bookId).orElse(null);
    if (book == null) return "Book not found";

    // 2️⃣ Check if already available
    if ("AVAILABLE".equalsIgnoreCase(book.getBookStatus())) {
        return "Book is already returned";
    }

    // 🔥 IMPORTANT: get userId before resetting
    Long userId = book.getTakenByUserId();

    // 3️⃣ Update book
    book.setBookStatus("AVAILABLE");
    book.setTakenByUserId(null);
    book.setReturnDate(null);

    bookRepository.save(book);

    // 🔥 4️⃣ UPDATE TRANSACTION (ADD THIS BLOCK)

    if (userId != null) {

        List<BookTransaction> list =
                transactionRepository.findByUserIdAndStatus(userId, "Issued");

        for (BookTransaction t : list) {
            if (t.getBookId().equals(bookId)) {
                t.setStatus("Returned");
                t.setReturnDate(new Date());
                transactionRepository.save(t);
                break;
            }
        }
    }

    return "Book returned successfully";
}


    public String updateBook(UpdateBookRequest request) {

        int updatedRows = bookRepository.updateBookDetails(
            request.getBookId(),
            request.getBookName(),
            request.getBookAuthor(),
            request.getBookCategory()
        );

        if (updatedRows == 0) {
            return "Book not found or already taken";
        }

        return "Book updated successfully";
    }


    public String deleteBook(DeleteBookRequest request) {
    int deleted = bookRepository.deleteBookById(request.getBookId());

    if (deleted == 0) {
        return "Book not found or it is currently taken";
    }
    return "Book deleted successfully";
}



}