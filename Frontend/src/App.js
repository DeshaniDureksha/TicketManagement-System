import React, { useEffect, useState } from "react";
import SystemLog from "./components/SystemLog";
import TicketAvailability from "./components/TicketAvailability";
import ConfigurationForm from "./components/ConfigurationForm";
import ControlPanel from "./components/ControlPanel";
import "./App.css";

const App = () => {
    const [isConfigSaved, setIsConfigSaved] = useState(false); // Tracks if configuration has been saved
    const [systemStatus, setSystemStatus] = useState("stopped"); // Tracks whether the system is running or stopped

    useEffect(() => {
        const fetchSystemStatus = () => {
            fetch("http://localhost:8080/api/ticket/status")
                .then((response) => response.text())
                .then((data) => {
                    const status = data.includes("running") ? "running" : "stopped"; // Determine system status
                    setSystemStatus(status);
                })
                .catch((error) => console.error("Error fetching system status:", error));
        };

        fetchSystemStatus(); // Initial fetch on mount
        const interval = setInterval(fetchSystemStatus, 2000); // Poll system status every 2 seconds
        return () => clearInterval(interval); // Clear polling on unmount
    }, []);

    // Handles saving the system configuration
    const handleConfigurationSaved = (config) => {
        fetch("http://localhost:8080/api/ticket/configure", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(config),
        })
            .then((response) => response.text())
            .then((data) => {
                alert(`localhost:3000 says: ${data}`);
                setIsConfigSaved(true); // Mark configuration as saved
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("localhost:3000 says: Failed to save configuration");
            });
    };

    // Handles starting the system
    const handleStartSystem = () => {
        if (!isConfigSaved) { // Ensure configuration is saved before starting
            alert("localhost:3000 says: Please enter and save the configuration details first!");
            return;
        }

        fetch("http://localhost:8080/api/ticket/start", { method: "POST" })
            .then((response) => response.text())
            .then(() => {
                alert("localhost:3000 says: System started successfully!");
                setSystemStatus("running"); // Update system status to running
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("localhost:3000 says: Error starting system: " + error.message);
            });
    };

    // Handles stopping the system
    const handleStopSystem = () => {
        fetch("http://localhost:8080/api/ticket/stop", { method: "POST" })
            .then((response) => response.text())
            .then(() => {
                alert("localhost:3000 says: System stopped successfully!");
                setSystemStatus("stopped"); // Update system status to stopped
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("localhost:3000 says: Error stopping system: " + error.message);
            });
    };

    return (
        <div className="app-container">
            {/* Navigation Bar */}
            <nav className="navbar">
                <h1>Event Manager</h1>
                <ul>
                    <li><a href="#configuration">Configuration</a></li>
                    <li><a href="#control">Control</a></li>
                    <li><a href="#availability">Availability</a></li>
                    <li><a href="#logs">Logs</a></li>
                </ul>
            </nav>
            <main className="main-content">
                {/* Configuration Section */}
                <section id="configuration">
                    <h2>System Configuration</h2>
                    <ConfigurationForm onConfigurationSaved={handleConfigurationSaved} />
                </section>

                {/* Control Panel Section */}
                <section id="control">
                    <h2>Control Panel</h2>
                    <ControlPanel
                        onStartSystem={handleStartSystem}
                        onStopSystem={handleStopSystem}
                        isStartDisabled={systemStatus === "running"} // Disable start button if running
                        isStopDisabled={systemStatus === "stopped"} // Disable stop button if stopped
                    />
                </section>

                {/* Ticket Availability Section */}
                <section id="availability">
                    <h2>Ticket Availability</h2>
                    <TicketAvailability />
                </section>

                {/* Logs Section */}
                <section id="logs">
                    <h2>System Log</h2>
                    <SystemLog />
                </section>
            </main>
        </div>
    );
};

export default App;
