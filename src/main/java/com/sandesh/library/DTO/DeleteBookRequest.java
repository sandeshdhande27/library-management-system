package com.sandesh.library.DTO;

import jakarta.validation.constraints.NotNull;

public class DeleteBookRequest {

    @NotNull(message = "bookId is required")
    private Long bookId;

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
}