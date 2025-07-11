package com.ravinduw.apps.groceriesappbackend.repository;

import com.ravinduw.apps.groceriesappbackend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location, String> {

    Location findByLocationId(String locationId);

}
