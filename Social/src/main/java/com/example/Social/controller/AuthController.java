package com.example.Social.controller;

import com.example.Social.model.ErrorResponse;
import com.example.Social.model.User;
import com.example.Social.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User data) {
        User user = userRepository.findByEmailAndPassword(data.getEmail(), data.getPassword());

        if (user != null) {
            return ResponseEntity.ok("Login Successful");
        } else {
            User existingUser = userRepository.findByEmail(data.getEmail());
            if (existingUser != null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Error", "Username/Password Incorrect");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Error", "User does not exist");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User data) {
        User existingUser = userRepository.findByEmail(data.getEmail());
        if (existingUser != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Forbidden, Account already exists");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } else {
            User user = userRepository.save(data);
            return ResponseEntity.ok("Account Creation Successful");
        }
    }
}