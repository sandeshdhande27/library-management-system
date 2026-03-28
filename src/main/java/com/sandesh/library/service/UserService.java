package com.sandesh.library.service;

import java.util.Optional;
import com.sandesh.library.entity.User;
import com.sandesh.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Calendar; // ✅ IMPORTANT IMPORT

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user) {

        // Check if email exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already registered";
        }

        // Current date
        Date now = new Date();

        // ✅ Correct setter names
        user.setMembershipStartDate(now);

        // Add months
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        // Handle null safety
        int months = (user.getMembershipMonths() != null) ? user.getMembershipMonths() : 0;

        cal.add(Calendar.MONTH, months);

        user.setMembershipEndDate(cal.getTime());

        // Set created date
        user.setCreatedAt(now);

        userRepository.save(user);

        return "User registered successfully";
    }


    public String loginUser(String email, String password) {

    Optional<User> userOpt = userRepository.findByEmail(email);

    if (userOpt.isEmpty()) {
        return "Invalid email or password";
    }

    User user = userOpt.get();

    if (!user.getPassword().equals(password)) {
        return "Invalid email or password";
    }

    return "Login successful";
}




    public User getUserByEmail(String email) {
        return userRepository.findUserByEmailForDashboard(email).orElse(null);
    }




}