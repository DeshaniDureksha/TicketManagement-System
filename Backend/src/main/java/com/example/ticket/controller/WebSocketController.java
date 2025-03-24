package com.example.ticket.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller // Marks this class as a Spring MVC Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    // Constructor-based dependency injection for SimpMessagingTemplate
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sends a log update message to a specific WebSocket topic.
     *
     * @param topic   The WebSocket topic to send the message to (e.g., "/topic/logs").
     * @param message The message content to be sent.
     */
    public void sendLogUpdate(String topic, String message) {
        messagingTemplate.convertAndSend(topic, message); // Publishes the message to the given topic
    }
}
