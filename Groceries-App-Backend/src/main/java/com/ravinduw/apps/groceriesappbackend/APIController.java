package com.ravinduw.apps.groceriesappbackend;

import com.ravinduw.apps.groceriesappbackend.model.ItemRequest;
import com.ravinduw.apps.groceriesappbackend.model.ToggleRequest;
import core.Display;
import core.Item;
import core.RegularUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIController {

    @GetMapping("/groceries")
    public String  getGroceries(HttpServletRequest request) {

        RegularUser user = new RegularUser("admin");

        Display display = new Display(user, "HKU");
        try {
            return String.valueOf(display.getItems());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @PostMapping("/groceries/add")
    public ResponseEntity<String> addGroceries(@RequestBody ItemRequest request, HttpServletResponse response) {

        System.out.println(request.getName());
        String name = request.getName();
        RegularUser user = new RegularUser("admin");
        try {
            Item item = new Item(user);
            item.newItem(name, "HKU");
            return new ResponseEntity<>("Item added successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while adding item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/groceries/toggle")
    public ResponseEntity<String> toggleItem(@RequestBody ToggleRequest request, HttpServletResponse response) {

        String id = request.getId();
        RegularUser user = new RegularUser("admin");

        try {
            Item item = new Item(id, user);
            item.checkItem();
            return new ResponseEntity<>("Item toggled successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while toggling item", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
