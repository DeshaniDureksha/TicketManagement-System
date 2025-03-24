import React from "react";

const ControlPanel = ({ onStartSystem, onStopSystem, isStartDisabled, isStopDisabled }) => {
    return (
        <div style={{ display: "flex", justifyContent: "center", gap: "20px", marginTop: "30px" }}>
            {/* Start System Button */}
            <button
                onClick={onStartSystem} // Trigger the start system action
                disabled={isStartDisabled} // Disable button when `isStartDisabled` is true
                style={{
                    backgroundColor: isStartDisabled ? "#555" : "#e30613", // Gray when disabled, red otherwise
                    cursor: isStartDisabled ? "not-allowed" : "pointer", // Show appropriate cursor state
                    padding: "15px 30px",
                    borderRadius: "8px",
                    fontSize: "18px",
                    fontWeight: "bold",
                    transition: "all 0.3s ease", // Smooth hover effects
                }}
            >
                Start System
            </button>

            {/* Stop System Button */}
            <button
                onClick={onStopSystem} // Trigger the stop system action
                disabled={isStopDisabled} // Disable button when `isStopDisabled` is true
                style={{
                    backgroundColor: isStopDisabled ? "#555" : "#1c1c1c", // Gray when disabled, black otherwise
                    cursor: isStopDisabled ? "not-allowed" : "pointer", // Show appropriate cursor state
                    padding: "15px 30px",
                    borderRadius: "8px",
                    fontSize: "18px",
                    fontWeight: "bold",
                    transition: "all 0.3s ease", // Smooth hover effects
                }}
            >
                Stop System
            </button>
        </div>
    );
};

export default ControlPanel;
