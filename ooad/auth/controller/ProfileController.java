package com.project.ooad.auth.controller;

import com.project.ooad.auth.model.User;
import com.project.ooad.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private AuthService authService;

    @GetMapping("/view")
    public ResponseEntity<User> viewProfile(@RequestParam String username) {
        User user = authService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestParam String username, @RequestBody User updatedUser) {
        authService.updateUser(username, updatedUser);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestParam String username) {
        authService.deleteUser(username);
        return ResponseEntity.ok("Account deleted successfully");
    }
}