package com.sandesh.library.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = false) // ❌ Reject extra fields
public class ReturnBookRequest {

    @NotNull(message = "bookId is required") // ✅ Ensure bookId is provided
    private Long bookId;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}