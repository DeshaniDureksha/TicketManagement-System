package com.example.ticket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity // Marks this class as a JPA entity mapped to a database table
public class Ticket {

    @Id // Denotes the primary key field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate primary key values
    private Long id;

    @Column(nullable = false) // Ensures the column cannot have null values
    private String name; // The name of the event or ticket

    @Column(nullable = false) // Ensures the column cannot have null values
    private double price; // Price of the ticket

    @Column(nullable = false) // Ensures the column cannot have null values
    private boolean isAvailable; // Indicates if the ticket is available

    // Default constructor required by JPA
    public Ticket() {
    }

    // Parameterized constructor for creating instances with specific values
    public Ticket(String name, double price, boolean isAvailable) {
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters for accessing and modifying fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        // Provides a readable string representation of the object for logging or debugging
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
