package com.example.ticket.repo;

import com.example.ticket.model.ConfigurationManager;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on the ConfigurationManager entity.
 */
public interface ConfigurationRepo extends JpaRepository<ConfigurationManager, Long> {
    // JpaRepository provides basic CRUD operations and query methods out of the box.
}
