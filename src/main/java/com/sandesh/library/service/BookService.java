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

    // 1️⃣ Validate days
    if (days <= 0) {
        return "Invalid number of days";
    }

    // 2️⃣ Get book
    Book book = bookRepository.findById(bookId).orElse(null);
    if (book == null) {
        return "Book not found";
    }

    // 3️⃣ Check availability
    if (!"AVAILABLE".equalsIgnoreCase(book.getBookStatus())) {
        return "Book already taken";
    }

    // 4️⃣ Get user
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
        return "User not found";
    }

    Date now = new Date();

    // 5️⃣ Check membership
    if (user.getMembershipEndDate() == null || user.getMembershipEndDate().before(now)) {
        return "Membership expired";
    }

    // 🔥 6️⃣ LIMIT BOOKS (MAX 3)
    List<BookTransaction> activeBooks =
            transactionRepository.findByUserIdAndStatus(userId, "Issued");

    if (activeBooks.size() >= 3) {
        return "User already has 3 books issued";
    }

    // 7️⃣ Calculate due date
    Calendar cal = Calendar.getInstance();
    cal.setTime(now);
    cal.add(Calendar.DAY_OF_MONTH, days);

    Date dueDate = cal.getTime();

    // 8️⃣ Update book
    book.setBookStatus("TAKEN");
    book.setTakenByUserId(userId);
    book.setReturnDate(dueDate); // optional: storing due date in book

    bookRepository.save(book);

    // 9️⃣ Create transaction
    BookTransaction transaction = new BookTransaction();
    transaction.setUserId(userId);
    transaction.setBookId(bookId);
    transaction.setIssueDate(now);
    transaction.setDueDate(dueDate);
    transaction.setStatus("Issued");

    transactionRepository.save(transaction);

    return "Book issued successfully";
}


// logic for return book
public String returnBook(Long bookId, Long userId) {

    // 1️⃣ Get book
    Book book = bookRepository.findById(bookId).orElse(null);
    if (book == null) return "Book not found";

    // 2️⃣ Check if already available
    if ("AVAILABLE".equalsIgnoreCase(book.getBookStatus())) {
        return "Book is already returned";
    }

    // 🔥 3️⃣ VALIDATE USER (IMPORTANT)
    if (book.getTakenByUserId() == null || !book.getTakenByUserId().equals(userId)) {
        return "This user did not take this book";
    }

    // 4️⃣ Update book
    book.setBookStatus("AVAILABLE");
    book.setTakenByUserId(null);
    book.setReturnDate(null);

    bookRepository.save(book);

    // 🔥 5️⃣ FIND TRANSACTION (BETTER WAY)
    BookTransaction t = transactionRepository
            .findByUserIdAndBookIdAndStatus(userId, bookId, "Issued");

    if (t != null) {

        Date returnDate = new Date();

        t.setStatus("Returned");
        t.setReturnDate(returnDate);

        // 🔥 FINE CALCULATION
        Date dueDate = t.getDueDate();

        if (dueDate != null && returnDate.after(dueDate)) {

            long diff = returnDate.getTime() - dueDate.getTime();
            long daysLate = (long) Math.ceil(diff / (1000.0 * 60 * 60 * 24));

            double fine = daysLate * 10; // ₹10 per day
            t.setFineAmount(fine);

        } else {
            t.setFineAmount(0.0);
        }

        transactionRepository.save(t);
    }

    return "Book returned successfully";
}

// update book infor
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

// fetch all unique categories of books
public List<String> getAllCategories() {
    return bookRepository.findDistinctCategories();
}


}