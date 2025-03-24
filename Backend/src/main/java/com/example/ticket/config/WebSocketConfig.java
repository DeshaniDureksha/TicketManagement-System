package com.example.ticket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // Marks this class as a configuration class for Spring
@EnableWebSocketMessageBroker // Enables WebSocket message handling with STOMP protocol
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configures the message broker
        config.enableSimpleBroker("/topic"); // Enables a simple in-memory message broker with the "/topic" prefix for subscriptions
        config.setApplicationDestinationPrefixes("/app"); // Prefix for messages from clients to the server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers the WebSocket endpoint for clients to connect
        registry.addEndpoint("/websocket") // Endpoint for WebSocket connections
                .setAllowedOrigins("http://localhost:3000") // Allow connections from the specified origin
                .withSockJS(); // Fallback to SockJS for browsers that don't support native WebSockets
    }
}
