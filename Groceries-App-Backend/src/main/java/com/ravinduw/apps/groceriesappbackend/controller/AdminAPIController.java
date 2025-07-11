package com.ravinduw.apps.groceriesappbackend.controller;

import com.ravinduw.apps.groceriesappbackend.entity.User;
import com.ravinduw.apps.groceriesappbackend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-api/admin")
public class AdminAPIController {

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/new-user")
    public ResponseEntity<String> newUser(User user) {

        userRepo.save(user);

        return ResponseEntity.ok("User created successfully");

    }

    @GetMapping
    public ResponseEntity<String> getAllUsers() {

        return ResponseEntity.ok(userRepo.findAll().toString());

    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {

        userRepo.deleteById(id);

        return ResponseEntity.ok("User deleted successfully");

    }

}
