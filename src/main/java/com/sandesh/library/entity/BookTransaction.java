package com.sandesh.library.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book_transactions")
public class BookTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long bookId;

    private Date issueDate;

    private Date returnDate;

    private String status; // Issued / Returned

    // modifications
private Date dueDate;        // when book should be returned
private Double fineAmount;  // late fine

    // 🔹 Getters & Setters

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // modifications...

    public Date getDueDate(){
        return dueDate;
    }

    public void setDueDate(Date dueDate){
         this.dueDate = dueDate;
    }

    public Double getFineAmount(){
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount){
         this.fineAmount = fineAmount;
    }
}