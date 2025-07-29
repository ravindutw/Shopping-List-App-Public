package com.ravinduw.apps.shoppinglistapp.service;

import com.ravinduw.apps.shoppinglistapp.auth.AuthUtils;
import com.ravinduw.apps.shoppinglistapp.entity.User;
import com.ravinduw.apps.shoppinglistapp.repository.UserRepo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ravinduw.apps.shoppinglistapp.utils.Utils;

@Service
@NoArgsConstructor
public class AdminService {

    @Autowired
    private UserRepo userRepo;

    public boolean createUser(User user) {

        user.setPassword(AuthUtils.encodePw(user.getPassword()));

        String lastUserID = userRepo.getLastUserID();
        if(lastUserID != null){
            user.setUserId(Utils.nextId(lastUserID));
        } else {
            user.setUserId("USER-001");
        }

        userRepo.save(user);

        return true;

    }

}
