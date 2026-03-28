package com.sandesh.library.controller;

import com.sandesh.library.DTO.BookFilterRequest;
import com.sandesh.library.DTO.DeleteBookRequest;
import com.sandesh.library.DTO.ReturnBookRequest;
import com.sandesh.library.DTO.TakeBookRequest;
import com.sandesh.library.DTO.UpdateBookRequest;
import com.sandesh.library.entity.Book;
import com.sandesh.library.service.BookService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    

    @PostMapping("/add_books")
    public String addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }


    @GetMapping("/get_books")
public List<Book> getAllBooks() {
    return bookService.getAllBooks();
}

@PostMapping("/select_specific_book")
public List<Book> getSpecificBooks(@RequestBody BookFilterRequest request) {
    return bookService.getSpecificBooks(request);
}

@PostMapping("/take_book")
public String takeBook(@RequestBody TakeBookRequest request) {
    return bookService.takeBook(
            request.getBookId(),
            request.getUserId(),
            request.getDays()
    );
}

@PostMapping("/return_book")
    public ResponseEntity<String> returnBook(@Valid @RequestBody ReturnBookRequest request) {
        // Call service with only bookId
        String response = bookService.returnBook(request.getBookId());
        return ResponseEntity.ok(response);
    }




    @PutMapping("/update_book")
    public String updateBook(@Valid @RequestBody UpdateBookRequest request) {
        return bookService.updateBook(request);
    }


        @PostMapping("/delete_book")
    public String deleteBook(@Valid @RequestBody DeleteBookRequest request) {
        return bookService.deleteBook(request);
    }


    @GetMapping("/categories")
public List<String> getCategories() {
    return bookService.getAllCategories();
}

}