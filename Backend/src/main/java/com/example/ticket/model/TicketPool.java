package com.example.ticket.model;

import com.example.ticket.service.LogService;
import com.example.ticket.controller.WebSocketController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private final int totalTickets; // Total tickets in the pool
    private final int maxCapacity; // Maximum tickets that can be available at a time
    private final int ticketReleaseRate; // Rate of releasing tickets (ms)
    private final int customerRetrievalRate; // Rate of ticket retrieval by customers (ms)
    private int availableTickets; // Current number of available tickets
    private boolean running; // System state (running or stopped)

    private final Lock lock = new ReentrantLock(); // Ensures thread-safe operations
    private final LogService logService; // Logs system activity
    private final WebSocketController webSocketController; // Sends updates via WebSocket

    // Constructor initializes ticket pool with configuration and dependencies
    public TicketPool(int totalTickets, int maxCapacity, int ticketReleaseRate, int customerRetrievalRate,
                      LogService logService, WebSocketController webSocketController) {
        this.totalTickets = totalTickets;
        this.maxCapacity = maxCapacity;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.availableTickets = Math.min(totalTickets, maxCapacity); // Initial ticket count
        this.logService = logService;
        this.webSocketController = webSocketController;
        this.running = false; // System starts in a stopped state
    }

    // Starts the ticket system
    public void start() {
        running = true;
        logService.addLog("System started.");
    }

    // Stops the ticket system
    public void stop() {
        running = false;
        logService.addLog("System stopped.");
    }

    // Adds a single ticket by a vendor, if capacity allows
    public boolean addTicket(int vendorId) {
        lock.lock();
        try {
            if (availableTickets < maxCapacity) {
                availableTickets++;
                String logMessage = "Vendor " + vendorId + " added a ticket. Available tickets: " + availableTickets;
                logService.addLog(logMessage);
                webSocketController.sendLogUpdate("/topic/logs", logMessage); // Notify via WebSocket
                notifyTicketAvailability(); // Broadcast ticket availability
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    // Allows a customer to buy a ticket if available
    public boolean buyTicket(int customerId) {
        lock.lock();
        try {
            if (availableTickets > 0) {
                availableTickets--;
                String logMessage = "Customer " + customerId + " bought a ticket. Available tickets: " + availableTickets;
                logService.addLog(logMessage);
                webSocketController.sendLogUpdate("/topic/logs", logMessage); // Notify via WebSocket
                notifyTicketAvailability(); // Broadcast ticket availability
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    // Adds multiple tickets by a vendor, up to max capacity
    public boolean addMultipleTickets(int numberOfTickets, int vendorId) {
        lock.lock();
        try {
            int ticketsAdded = 0;
            for (int i = 0; i < numberOfTickets; i++) {
                if (availableTickets < maxCapacity) {
                    availableTickets++;
                    ticketsAdded++;
                } else {
                    break; // Stop if max capacity is reached
                }
            }

            if (ticketsAdded > 0) {
                String logMessage = "Vendor " + vendorId + " added " + ticketsAdded + " tickets. Available tickets: " + availableTickets;
                logService.addLog(logMessage);
                webSocketController.sendLogUpdate("/topic/logs", logMessage); // Notify via WebSocket
                notifyTicketAvailability(); // Broadcast ticket availability
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    // Broadcasts ticket availability to connected WebSocket clients
    private void notifyTicketAvailability() {
        String availabilityUpdate = String.format("{\"totalTickets\": %d, \"availableTickets\": %d}",
                totalTickets, availableTickets);
        webSocketController.sendLogUpdate("/topic/ticketAvailability", availabilityUpdate);
    }

    // Returns whether the system is running
    public boolean isRunning() {
        return running;
    }

    // Getters for ticket pool configuration and state
    public int getTotalTickets() {
        return totalTickets;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
}
