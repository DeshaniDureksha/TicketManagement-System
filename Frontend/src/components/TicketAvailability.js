import React, { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const TicketAvailability = () => {
    const [totalTickets, setTotalTickets] = useState(0); // Tracks total tickets
    const [availableTickets, setAvailableTickets] = useState(0); // Tracks currently available tickets

    useEffect(() => {
        // Establish WebSocket connection
        const socket = new SockJS("http://localhost:8080/websocket");
        const stompClient = new Client({
            webSocketFactory: () => socket,
            debug: console.log,
            onConnect: () => {
                // Subscribe to ticket availability updates
                stompClient.subscribe("/topic/ticketAvailability", (message) => {
                    const ticketData = JSON.parse(message.body);
                    // Update state only if ticket data has changed
                    if (
                        ticketData.totalTickets !== totalTickets ||
                        ticketData.availableTickets !== availableTickets
                    ) {
                        setTotalTickets(ticketData.totalTickets);
                        setAvailableTickets(ticketData.availableTickets);
                    }
                });
            },
        });

        stompClient.activate(); // Activate WebSocket connection

        // Cleanup WebSocket on component unmount
        return () => stompClient.deactivate();
    }, [totalTickets, availableTickets]); // Dependencies to ensure updates only when these values change

    // Calculate percentage of available tickets
    const calculateProgress = () => {
        if (totalTickets === 0) return 0; // Avoid division by zero
        return Math.round((availableTickets / totalTickets) * 100);
    };

    return (
        <div
            style={{
                padding: "20px",
                borderRadius: "8px",
                backgroundColor: "#fdf5e6", // Light background
                color: "#5d4037", // Text color
                fontFamily: "Arial, sans-serif",
            }}
        >
            <h3
                style={{
                    textAlign: "center",
                    color: "#8b4513",
                    borderBottom: "2px solid #c4a484",
                    paddingBottom: "10px",
                    marginBottom: "20px",
                }}
            >
                Ticket Availability
            </h3>

            <div style={{ margin: "20px 0", textAlign: "center" }}>
                <p style={{ fontSize: "18px", fontWeight: "bold", marginBottom: "10px" }}>
                    Available Tickets
                </p>
                <div
                    style={{
                        backgroundColor: "#8b4513", // Progress bar background
                        borderRadius: "10px",
                        height: "30px",
                        position: "relative",
                        overflow: "hidden",
                        margin: "10px auto",
                        width: "80%", // Bar width
                    }}
                >
                    <div
                        style={{
                            backgroundColor: "#d2691e", // Filled progress bar color
                            width: `${calculateProgress()}%`, // Progress width based on percentage
                            height: "100%",
                            transition: "width 0.5s ease", // Smooth progress bar transition
                        }}
                    ></div>
                    <span
                        style={{
                            position: "absolute",
                            top: "50%",
                            left: "50%",
                            transform: "translate(-50%, -50%)", // Center the text
                            color: "#fff",
                            fontWeight: "bold",
                        }}
                    >
                        {availableTickets} {/* Display available tickets */}
                    </span>
                </div>
            </div>

            <div style={{ textAlign: "center", fontSize: "18px", fontWeight: "bold" }}>
                <p>Total Tickets</p>
                <p style={{ fontSize: "22px", color: "#8b4513" }}>{totalTickets}</p>
            </div>
        </div>
    );
};

export default TicketAvailability;
