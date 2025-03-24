package com.example.ticket.service;

import com.example.ticket.controller.WebSocketController;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service for managing and broadcasting logs within the ticket system.
 */
@Service // Marks this as a Spring-managed service component
public class LogService {

    private final List<String> logs = Collections.synchronizedList(new ArrayList<>()); // Thread-safe log list
    private final WebSocketController webSocketController; // WebSocket controller for broadcasting logs

    /**
     * Constructor for LogService.
     *
     * @param webSocketController The WebSocket controller to send log updates.
     */
    public LogService(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }

    /**
     * Adds a new log entry with a timestamp.
     * The log is also sent to WebSocket subscribers and printed to the console.
     *
     * @param log The log message to add.
     */
    public void addLog(String log) {
        // Format log entry with timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestampedLog = LocalDateTime.now().format(formatter) + " - " + log;

        synchronized (logs) { // Ensure thread-safe modifications
            logs.add(timestampedLog);

            // Limit the log size to 1000 entries for memory efficiency
            if (logs.size() > 1000) {
                logs.remove(0);
            }
        }

        // Broadcast the log update via WebSocket
        webSocketController.sendLogUpdate("/topic/logs", timestampedLog);

        // Print log to console for debugging/monitoring
        System.out.println(timestampedLog);
    }

    /**
     * Retrieves all logs in a thread-safe manner.
     *
     * @return A copy of the current log list.
     */
    public List<String> getLogs() {
        synchronized (logs) {
            return new ArrayList<>(logs); // Return a copy to prevent external modification
        }
    }
}
