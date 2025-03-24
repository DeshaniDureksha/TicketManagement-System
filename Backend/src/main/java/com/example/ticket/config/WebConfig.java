package com.example.ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Marks this class as a configuration class for Spring
public class WebConfig {

    @Bean // Declares a Spring bean for configuring CORS
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Configures CORS to allow requests from "http://localhost:3000"
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins("http://localhost:3000"); // Restrict to a specific origin
            }
        };
    }
}
