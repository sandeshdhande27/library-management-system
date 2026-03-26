package com.sandesh.library.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookName;

    private String bookAuthor;

    private String bookCategory;

    private String bookStatus; // AVAILABLE / TAKEN

    private Date createdAt;

    private Long takenByUserId;

private Date returnDate;

    // Getters & Setters

    public Long getId() { return id; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public String getBookCategory() { return bookCategory; }
    public void setBookCategory(String bookCategory) { this.bookCategory = bookCategory; }

    public String getBookStatus() { return bookStatus; }
    public void setBookStatus(String bookStatus) { this.bookStatus = bookStatus; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Long getTakenByUserId() { return takenByUserId; }
    public void setTakenByUserId(Long takenByUserId) { this.takenByUserId = takenByUserId; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

}