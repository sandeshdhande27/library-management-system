package com.sandesh.library.controller;

import com.sandesh.library.entity.User;
import com.sandesh.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        // ✅ Simple validation
        if (user.getName() == null ||
            user.getEmail() == null ||
            user.getPassword() == null ||
            user.getMembershipMonths() == null) {

            return "Required parameters missing";
        }

        return userService.registerUser(user);
    }

    @PostMapping("/login")
public String login(@RequestBody User user) {

    if (user.getEmail() == null || user.getPassword() == null) {
        return "Required parameters missing";
    }

    return userService.loginUser(user.getEmail(), user.getPassword());
}


}