package com.example.ticket.service;

import com.example.ticket.model.TicketPool;

/**
 * Represents a thread simulating a vendor adding tickets to the ticket pool.
 */
public class VendorThread implements Runnable {
    private final TicketPool ticketPool; // Shared ticket pool
    private final LogService logService; // Service for logging activities
    private final int vendorId; // Unique identifier for the vendor

    /**
     * Constructor to initialize the vendor thread.
     *
     * @param ticketPool The shared ticket pool.
     * @param logService The logging service.
     * @param vendorId   The unique vendor ID.
     */
    public VendorThread(TicketPool ticketPool, LogService logService, int vendorId) {
        this.ticketPool = ticketPool;
        this.logService = logService;
        this.vendorId = vendorId; // Initialize vendor ID
    }

    /**
     * Simulates vendor behavior in a separate thread.
     * Continuously adds tickets to the pool while the system is running.
     */
    @Override
    public void run() {
        while (ticketPool.isRunning()) { // Keep running while the system is active
            if (ticketPool.addTicket(vendorId)) { // Attempt to add a ticket
                // Log successful ticket addition
                logService.addLog("Vendor " + vendorId + " added a ticket. Available tickets: " + ticketPool.getAvailableTickets());
            }
            try {
                // Wait for the next ticket addition based on configured release rate
                Thread.sleep(ticketPool.getTicketReleaseRate());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
                break;
            }
        }
    }
}
