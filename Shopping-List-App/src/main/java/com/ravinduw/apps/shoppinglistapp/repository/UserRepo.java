package com.ravinduw.apps.shoppinglistapp.repository;

import com.ravinduw.apps.shoppinglistapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, String> {

    User findByUserName(String userName);

    @Query("SELECT u.userId FROM User u ORDER BY u.userId DESC")
    List<String> findAllUserIdsOrderByIdDesc();

    default String getLastUserID() {
        List<String> ids = findAllUserIdsOrderByIdDesc();
        return ids.isEmpty() ? null : ids.getFirst();
    }

}
