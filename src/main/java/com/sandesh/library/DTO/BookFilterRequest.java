package com.sandesh.library.DTO;

public class BookFilterRequest {

    private String bookName;
    private String bookCategory;
    private String bookStatus;

    // Getters & Setters

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getBookCategory() { return bookCategory; }
    public void setBookCategory(String bookCategory) { this.bookCategory = bookCategory; }

    public String getBookStatus() { return bookStatus; }
    public void setBookStatus(String bookStatus) { this.bookStatus = bookStatus; }
}