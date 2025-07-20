package com.ravinduw.apps.shoppinglistapp.repository;

import com.ravinduw.apps.shoppinglistapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
    User findByUserName(String userName);
}
