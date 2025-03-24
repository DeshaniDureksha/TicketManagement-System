package com.example.ticket.controller;

import com.example.ticket.service.LogService;
import com.example.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from localhost:3000 (frontend)
@RequestMapping("/api/ticket") // Base URL for ticket-related endpoints
public class TicketController {

    @Autowired
    private TicketService ticketService; // Service for ticket-related operations

    @Autowired
    private LogService logService; // Service for logging system actions

    private TicketConfiguration currentConfiguration; // Stores the current system configuration

    @PostMapping("/configure")
    public ResponseEntity<String> saveConfiguration(@RequestBody TicketConfiguration config) {
        // Validate configuration
        if (config.getTotalTickets() <= 0 ||
                config.getTicketReleaseRate() <= 1000 ||
                config.getCustomerRetrievalRate() <= 1000 ||
                config.getMaxTicketCapacity() > config.getTotalTickets()) {
            return ResponseEntity.badRequest().body("Invalid configuration provided.");
        }

        currentConfiguration = config; // Save the configuration
        logService.addLog("Configuration updated: " + config); // Log the update
        return ResponseEntity.ok("Configuration saved successfully");
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        if (currentConfiguration == null) {
            return ResponseEntity.badRequest().body("Configuration not set");
        }

        if (ticketService.isSystemRunning()) {
            return ResponseEntity.badRequest().body("System is already running");
        }

        // Start the system with the current configuration
        ticketService.startSystem(
                currentConfiguration.getTotalTickets(),
                currentConfiguration.getTicketReleaseRate(),
                currentConfiguration.getCustomerRetrievalRate(),
                currentConfiguration.getMaxTicketCapacity()
        );

        logService.addLog("System started with configuration: " + currentConfiguration);
        return ResponseEntity.ok("System started successfully");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        ticketService.stopSystem(); // Stop the system
        logService.addLog("System stopped"); // Log the action
        return ResponseEntity.ok("System stopped");
    }

    @PostMapping("/vendor/add")
    public ResponseEntity<String> addTickets(@RequestParam int numberOfTickets, @RequestParam int vendorId) {
        try {
            ticketService.addTickets(numberOfTickets, vendorId); // Add tickets from a vendor
            return ResponseEntity.ok("Vendor " + vendorId + " added " + numberOfTickets + " tickets successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getSystemStatus() {
        // Check if the system is running or stopped
        String status = ticketService.isSystemRunning() ? "running" : "stopped";
        return ResponseEntity.ok("System is currently " + status);
    }

    @GetMapping("/tickets")
    public ResponseEntity<TicketStats> getTicketStats() {
        // Return current ticket statistics
        TicketStats stats = new TicketStats(ticketService.getTotalTickets(), ticketService.getAvailableTickets());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs() {
        return ResponseEntity.ok(logService.getLogs()); // Return system logs
    }

    // DTO for storing ticket configuration
    public static class TicketConfiguration {
        private int totalTickets;
        private int ticketReleaseRate;
        private int customerRetrievalRate;
        private int maxTicketCapacity;

        // Getters and setters for configuration properties
        public int getTotalTickets() {
            return totalTickets;
        }

        public void setTotalTickets(int totalTickets) {
            this.totalTickets = totalTickets;
        }

        public int getTicketReleaseRate() {
            return ticketReleaseRate;
        }

        public void setTicketReleaseRate(int ticketReleaseRate) {
            this.ticketReleaseRate = ticketReleaseRate;
        }

        public int getCustomerRetrievalRate() {
            return customerRetrievalRate;
        }

        public void setCustomerRetrievalRate(int customerRetrievalRate) {
            this.customerRetrievalRate = customerRetrievalRate;
        }

        public int getMaxTicketCapacity() {
            return maxTicketCapacity;
        }

        public void setMaxTicketCapacity(int maxTicketCapacity) {
            this.maxTicketCapacity = maxTicketCapacity;
        }

        @Override
        public String toString() {
            return "Total Tickets=" + totalTickets +
                    ", Ticket Release Rate=" + ticketReleaseRate + "ms" +
                    ", Customer Retrieval Rate=" + customerRetrievalRate + "ms" +
                    ", Max Ticket Capacity=" + maxTicketCapacity;
        }
    }

    // DTO for ticket statistics
    public static class TicketStats {
        private int totalTickets;
        private int availableTickets;

        public TicketStats(int totalTickets, int availableTickets) {
            this.totalTickets = totalTickets;
            this.availableTickets = availableTickets;
        }

        public int getTotalTickets() {
            return totalTickets;
        }

        public int getAvailableTickets() {
            return availableTickets;
        }
    }
}
