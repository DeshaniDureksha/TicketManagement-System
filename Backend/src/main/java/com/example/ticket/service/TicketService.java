package com.example.ticket.service;

import com.example.ticket.model.TicketPool;
import org.springframework.stereotype.Service;
import com.example.ticket.controller.WebSocketController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service for managing the ticketing system, including vendors and customers.
 */
@Service // Marks this as a Spring-managed service component
public class TicketService {
    private TicketPool ticketPool; // Represents the shared pool of tickets
    private final LogService logService; // Service for logging system events
    private final WebSocketController webSocketController; // WebSocket controller for broadcasting updates
    private ExecutorService executorService; // Thread pool for managing vendor and customer threads

    /**
     * Constructor for injecting dependencies.
     *
     * @param logService          The logging service.
     * @param webSocketController The WebSocket controller.
     */
    public TicketService(LogService logService, WebSocketController webSocketController) {
        this.logService = logService;
        this.webSocketController = webSocketController;
    }

    /**
     * Starts the ticketing system with the specified configuration.
     *
     * @param totalTickets        Total number of tickets in the system.
     * @param ticketReleaseRate   Rate at which vendors release tickets (in ms).
     * @param customerRetrievalRate Rate at which customers retrieve tickets (in ms).
     * @param maxTicketCapacity   Maximum number of tickets available at any given time.
     */
    public void startSystem(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        // Stop the system if already running
        if (ticketPool != null && ticketPool.isRunning()) {
            stopSystem();
        }

        // Initialize the ticket pool with the configuration
        ticketPool = new TicketPool(totalTickets, maxTicketCapacity, ticketReleaseRate, customerRetrievalRate, logService, webSocketController);
        ticketPool.start();

        int numberOfVendors = 500; // Number of vendor threads
        int numberOfCustomers = 500; // Number of customer threads

        // Create a thread pool for vendors and customers
        executorService = Executors.newFixedThreadPool(numberOfVendors + numberOfCustomers);

        // Create and submit vendor threads
        for (int i = 1; i <= numberOfVendors; i++) {
            final int vendorId = i;
            executorService.submit(() -> {
                while (ticketPool.isRunning()) {
                    ticketPool.addTicket(vendorId); // Add tickets to the pool
                    try {
                        Thread.sleep(ticketPool.getTicketReleaseRate()); // Simulate ticket release interval
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Handle thread interruption
                        break;
                    }
                }
            });
        }

        // Create and submit customer threads
        for (int i = 1; i <= numberOfCustomers; i++) {
            final int customerId = i;
            executorService.submit(() -> {
                while (ticketPool.isRunning()) {
                    ticketPool.buyTicket(customerId); // Customers try to buy tickets
                    try {
                        Thread.sleep(ticketPool.getCustomerRetrievalRate()); // Simulate ticket retrieval interval
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Handle thread interruption
                        break;
                    }
                }
            });
        }

        // Log system startup
        logService.addLog("System started with configuration: Total Tickets = " + totalTickets +
                ", Ticket Release Rate = " + ticketReleaseRate + "ms, Customer Retrieval Rate = " +
                customerRetrievalRate + "ms, Max Ticket Capacity = " + maxTicketCapacity);
    }

    /**
     * Stops the ticketing system and shuts down all threads.
     */
    public void stopSystem() {
        if (ticketPool != null) {
            ticketPool.stop(); // Stop the ticket pool
        }
        if (executorService != null) {
            executorService.shutdownNow(); // Terminate all threads
        }
        logService.addLog("System stopped."); // Log system shutdown
    }

    /**
     * Adds multiple tickets to the pool from a vendor.
     *
     * @param numberOfTickets The number of tickets to add.
     * @param vendorId        The ID of the vendor adding tickets.
     */
    public void addTickets(int numberOfTickets, int vendorId) {
        if (ticketPool != null && ticketPool.isRunning()) {
            ticketPool.addMultipleTickets(numberOfTickets, vendorId); // Add tickets to the pool
            logService.addLog("Vendor " + vendorId + " added " + numberOfTickets + " tickets.");
        } else {
            throw new IllegalStateException("System is not running. Cannot add tickets.");
        }
    }

    /**
     * Checks if the system is currently running.
     *
     * @return True if the system is running, false otherwise.
     */
    public boolean isSystemRunning() {
        return ticketPool != null && ticketPool.isRunning();
    }

    /**
     * Gets the total number of tickets in the system.
     *
     * @return The total tickets.
     */
    public int getTotalTickets() {
        return ticketPool != null ? ticketPool.getTotalTickets() : 0;
    }

    /**
     * Gets the number of available tickets.
     *
     * @return The available tickets.
     */
    public int getAvailableTickets() {
        return ticketPool != null ? ticketPool.getAvailableTickets() : 0;
    }
}
