package com.ravinduw.apps.groceriesappbackend.repository;

import com.ravinduw.apps.groceriesappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
}
