package com.sandesh.library.controller;

import com.sandesh.library.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandesh.library.entity.User;
import com.sandesh.library.service.UserService;

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


        @Autowired
    private UserService userService;
        // 🔥 GET USER BY EMAIL
    @GetMapping("/get_user_information")
    public User getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
}