import React, { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const SystemLog = () => {
    const [logs, setLogs] = useState([]); // State to store logs
    const logSet = useRef(new Set()); // Tracks unique logs to prevent duplicates

    useEffect(() => {
        // Initialize WebSocket connection
        const socket = new SockJS("http://localhost:8080/websocket");
        const stompClient = new Client({
            webSocketFactory: () => socket, // Connect WebSocket
            debug: console.log, // Log WebSocket activity for debugging
            onConnect: () => {
                console.log("WebSocket connected!");
                // Subscribe to log updates
                stompClient.subscribe("/topic/logs", (message) => {
                    if (message.body) {
                        const newLog = message.body.trim();
                        if (!logSet.current.has(newLog)) {
                            logSet.current.add(newLog); // Add log to Set
                            setLogs((prevLogs) => [...prevLogs, newLog]); // Update state
                        }
                    }
                });
            },
        });

        stompClient.activate(); // Activate WebSocket connection

        // Cleanup: Deactivate WebSocket on unmount
        return () => stompClient.deactivate();
    }, []); // Run once on component mount

    return (
        <div
            style={{
                maxHeight: "300px",
                overflowY: "auto",
                border: "2px solid #c4a484",
                borderRadius: "8px",
                padding: "15px",
                backgroundColor: "#fdf5e6",
                color: "#5d4037",
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
                System Log
            </h3>
            {logs.length > 0 ? (
                logs.map((log, index) => (
                    <div
                        key={index} // Key required for React list rendering
                        style={{
                            borderBottom: "1px solid #c4a484",
                            padding: "10px 5px",
                            marginBottom: "5px",
                        }}
                    >
                        {log}
                    </div>
                ))
            ) : (
                // Message when no logs are available
                <p style={{ textAlign: "center", fontStyle: "italic", color: "#8b4513" }}>
                    No logs available. Waiting for updates...
                </p>
            )}
        </div>
    );
};

export default SystemLog;
