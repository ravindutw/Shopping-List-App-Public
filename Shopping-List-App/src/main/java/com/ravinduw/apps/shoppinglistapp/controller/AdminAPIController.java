package com.ravinduw.apps.shoppinglistapp.controller;

import com.ravinduw.apps.shoppinglistapp.entity.User;
import com.ravinduw.apps.shoppinglistapp.repository.UserRepo;
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

}
