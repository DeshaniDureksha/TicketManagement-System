package com.example.ticket.repo;

import com.example.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the Ticket entity.
 */
@Repository // Indicates that this interface is a Spring-managed repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {
    // JpaRepository provides out-of-the-box CRUD operations and query methods for Ticket entities.
}
