# Ticket Management System

A real-time ticket distribution system built with Spring Boot and React that simulates ticket allocation for events with configurable parameters.

## Overview

This system simulates ticket distribution for high-demand events with configurable parameters for ticket release rates and customer retrieval rates. It provides real-time updates via WebSockets and a user-friendly interface to control and monitor the ticket allocation process.

## Features

- **Real-time Updates**: WebSocket integration provides instant updates on ticket availability
- **Configurable Parameters**: Customize total tickets, release rates, and capacity
- **System Controls**: Start/stop system operations at any time
- **Vendor Integration**: Allow vendors to add tickets to the system
- **Monitoring**: Visual indicators of ticket availability and system logs
- **Thread-safe Operations**: Concurrent access handling with proper locking mechanisms

## Technical Stack

### Backend
- Java Spring Boot
- Spring WebSocket for real-time communication
- JPA/Hibernate for data persistence
- RESTful API design

### Frontend
- React.js
- WebSocket client for real-time updates
- Responsive UI design

## Screenshots

The system includes several key interfaces:
- Configuration Panel - Set up system parameters
- Control Panel - Start/stop the system and monitor operations
- Availability Dashboard - View real-time ticket statistics
- System Logs - Track all system activities

## How It Works

1. Configure the system with total tickets, release rates, and capacity
2. Start the system to begin ticket distribution
3. Monitor real-time ticket availability
4. Vendors can add tickets while the system is running
5. Customers can retrieve tickets based on availability
6. Review logs for all system activities

## Installation and Setup

### Prerequisites
- Java 17 or higher
- Maven
- Node.js and npm

### Backend Setup
```bash
git clone https://github.com/yourusername/ticket-management-system.git
cd ticket-management-system/backend
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd ../frontend
npm install
npm start
```

Access the application at http://localhost:3000

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/ticket/configure` | POST | Save system configuration |
| `/api/ticket/start` | POST | Start the ticket system |
| `/api/ticket/stop` | POST | Stop the ticket system |
| `/api/ticket/vendor/add` | POST | Add tickets from vendor |
| `/api/ticket/status` | GET | Get system running status |
| `/api/ticket/tickets` | GET | Get ticket statistics |
| `/api/ticket/logs` | GET | Get system logs |

## WebSocket Topics

- `/topic/logs` - Real-time system log updates
- `/topic/ticketAvailability` - Real-time ticket availability updates

