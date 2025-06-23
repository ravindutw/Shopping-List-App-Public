package com.ravinduw.apps.groceriesappbackend;

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
    public JSONArray getGroceries(HttpServletRequest request) {

        Display display = new Display(null, "HKU");
        try {
            return display.getItems();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @PostMapping("/groceries/add")
    public ResponseEntity<String> addGroceries(@RequestBody JSONObject requestBody, HttpServletResponse response) {
        try {
            String name = requestBody.getString("name");
            RegularUser user = new RegularUser("admin");
            Item item = new Item(user);
            item.newItem(name);
            return new ResponseEntity<>("Item added successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while adding item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
