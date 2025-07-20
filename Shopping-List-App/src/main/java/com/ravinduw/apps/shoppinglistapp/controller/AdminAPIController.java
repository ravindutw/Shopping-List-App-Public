package com.ravinduw.apps.shoppinglistapp.controller;

import com.ravinduw.apps.shoppinglistapp.entity.User;
import com.ravinduw.apps.shoppinglistapp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-api/admin")
public class AdminAPIController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/new-user")
    public ResponseEntity<String> newUser(@RequestBody User user) {

        boolean status = adminService.createUser(user);

        if (!status) return ResponseEntity.badRequest().body("Error occurred while creating user");

        return ResponseEntity.ok("User created successfully");

    }

}
