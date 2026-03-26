package com.sandesh.library.controller;

import com.sandesh.library.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserHistoryController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{userId}/history")
    public Map<String, Object> getUserHistory(@PathVariable Long userId) {
        return transactionService.getUserHistory(userId);
    }
}