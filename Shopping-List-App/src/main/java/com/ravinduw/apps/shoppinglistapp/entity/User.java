package com.ravinduw.apps.shoppinglistapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ReplaceWithYourTableName") // Replace with your actual table name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private String role;

    @Column(name = "password")
    private String password;

}
