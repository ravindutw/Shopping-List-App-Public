package com.ravinduw.apps.groceriesappbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Location> locations = new ArrayList<>();

}
