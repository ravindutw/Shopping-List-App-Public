package com.ravinduw.apps.groceriesappbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @Column(name = "location_id")
    private String locationId;

    @Column(name = "location_name")
    private String LocationName;

}
