package com.ravinduw.apps.groceriesappbackend.repository;

import com.ravinduw.apps.groceriesappbackend.model.SessionControl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepo extends JpaRepository<SessionControl, String> {
}
