package com.ravinduw.apps.groceriesappbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String role;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String location;

    public User(String username, String name, String role) {
        this.username = username;
        this.name = name;
        this.role = role;
    }

}
