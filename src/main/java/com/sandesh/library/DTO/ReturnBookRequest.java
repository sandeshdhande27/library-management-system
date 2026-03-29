package com.sandesh.library.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = false) // ❌ Reject extra fields
public class ReturnBookRequest {

    @NotNull(message = "bookId is required") // ✅ Ensure bookId is provided
    private Long bookId;
    private Long userId; // ✅ Add userId to identify who is returning the book

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}