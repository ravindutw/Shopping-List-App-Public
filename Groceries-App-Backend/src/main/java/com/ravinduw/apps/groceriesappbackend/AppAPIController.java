package com.ravinduw.apps.groceriesappbackend;

import com.ravinduw.apps.groceriesappbackend.auth.AuthUtils;
import com.ravinduw.apps.groceriesappbackend.entity.ItemRequest;
import com.ravinduw.apps.groceriesappbackend.entity.ToggleRequest;
import com.ravinduw.apps.groceriesappbackend.entity.User;
import com.ravinduw.apps.groceriesappbackend.repository.UserRepo;
import org.springframework.security.web.csrf.CsrfToken;
import core.Display;
import core.Item;
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

    @PostMapping("/add")
    public ResponseEntity<String> addGroceries(@RequestBody ItemRequest request) {

        String name = request.getName();
        User user = userRepo.findByusername(AuthUtils.getUserName());

        try {
            Item item = new Item(user);
            item.newItem(name, "HKU");
            return new ResponseEntity<>("Item added successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while adding item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleItem(@RequestBody ToggleRequest request) {

        String id = request.getId();
        User user = userRepo.findByusername(AuthUtils.getUserName());

        try {
            Item item = new Item(id, user);
            item.checkItem();
            return new ResponseEntity<>("Item toggled successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred while toggling item", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/fetch")
    public String  getGroceries(HttpServletRequest request) {

        User user = userRepo.findByusername(AuthUtils.getUserName());
        Display display = new Display(user, "HKU");

        try {
            return String.valueOf(display.getItems());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}


@RestController
class CsrfController {
    @GetMapping("/csrf-token")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
