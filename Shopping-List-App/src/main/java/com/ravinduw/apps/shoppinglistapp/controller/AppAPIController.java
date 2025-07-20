package com.ravinduw.apps.shoppinglistapp.controller;

import com.ravinduw.apps.shoppinglistapp.auth.AuthUtils;
import com.ravinduw.apps.shoppinglistapp.entity.ItemRequest;
import com.ravinduw.apps.shoppinglistapp.entity.ToggleRequest;
import com.ravinduw.apps.shoppinglistapp.entity.User;
import com.ravinduw.apps.shoppinglistapp.repository.UserRepo;
import com.ravinduw.apps.shoppinglistapp.service.DisplayService;
import com.ravinduw.apps.shoppinglistapp.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app-api/groceries")
public class AppAPIController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ItemService itemService;

    @Autowired
    private DisplayService displayService;

    @PostMapping("/add")
    public ResponseEntity<String> addGroceries(@RequestBody ItemRequest request) {

        String name = request.getName();
        User user = userRepo.findByUserName(AuthUtils.getUserName());

        try {

            if (name == null || name.isBlank()) {
                throw new Exception("Name cannot be empty or blank");
            }

            //ItemService itemService = new ItemService(user);
            itemService.newItem(user, name, "HKU");
            return new ResponseEntity<>("ItemService added successfully", HttpStatus.OK);

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return new ResponseEntity<>("Error occurred while adding item", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleItem(@RequestBody ToggleRequest request) {

        String id = request.getId();
        User user = userRepo.findByUserName(AuthUtils.getUserName());

        try {

            if (id == null || id.isBlank()) {
                throw new Exception("ID cannot be empty or blank");
            }

            //ItemService itemService = new ItemService(id, user);
            itemService.checkItem(id, user);
            return new ResponseEntity<>("ItemService toggled successfully", HttpStatus.OK);

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return new ResponseEntity<>("Error occurred while toggling item", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/fetch")
    public ResponseEntity<String> getGroceries(HttpServletRequest request) {

        User user = userRepo.findByUserName(AuthUtils.getUserName());
        //DisplayService displayService = new DisplayService(user, locationId);

        try {

            return new ResponseEntity<>(String.valueOf(displayService.getItems()), HttpStatus.OK);

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return new ResponseEntity<>("Error occurred while fetching items", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}
