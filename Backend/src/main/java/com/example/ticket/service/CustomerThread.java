package com.example.ticket.service;

import com.example.ticket.model.TicketPool;

public class CustomerThread implements Runnable {
    private final TicketPool ticketPool;
    private final LogService logService;
    private final int customerId; // Customer ID

    public CustomerThread(TicketPool ticketPool, LogService logService, int customerId) {
        this.ticketPool = ticketPool;
        this.logService = logService;
        this.customerId = customerId; // Initialize customer ID
    }

    @Override
    public void run() {
        while (ticketPool.isRunning()) {
            if (ticketPool.buyTicket(customerId)) { // Pass customer ID to the method
                logService.addLog("Customer " + customerId + " bought a ticket. Available tickets: " + ticketPool.getAvailableTickets());
            } else {
                logService.addLog("Customer " + customerId + " tried to buy a ticket, but none were available.");
            }
            try {
                Thread.sleep(ticketPool.getCustomerRetrievalRate()); // Simulate customer buying tickets at regular intervals
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
