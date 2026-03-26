package com.sandesh.library.DTO;

import jakarta.validation.constraints.NotNull;

public class UpdateBookRequest {

    @NotNull(message = "bookId is required")
    private Long bookId;

    private String bookName;
    private String bookAuthor;
    private String bookCategory;

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public String getBookCategory() { return bookCategory; }
    public void setBookCategory(String bookCategory) { this.bookCategory = bookCategory; }
}