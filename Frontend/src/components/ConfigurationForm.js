import React, { useState } from "react";

const ConfigurationForm = ({ onConfigurationSaved }) => {
    const [config, setConfig] = useState({
        totalTickets: "",
        ticketReleaseRate: "",
        customerRetrievalRate: "",
        maxTicketCapacity: "",
    });
    const [errorMessage, setErrorMessage] = useState(""); // To display validation errors
    const [isSubmitting, setIsSubmitting] = useState(false); // Prevent multiple form submissions

    // Handle input changes, allowing only numeric values
    const handleInputChange = (e) => {
        const { name, value } = e.target;

        if (/^\d*$/.test(value)) { // Ensure input is numeric
            setConfig((prev) => ({ ...prev, [name]: value }));
            setErrorMessage(""); // Clear errors on valid input
        } else {
            setErrorMessage("Only numbers are allowed."); // Show error for invalid input
        }
    };

    // Handle form submission
    const handleSubmit = (e) => {
        e.preventDefault();
        if (isSubmitting) return; // Prevent duplicate submissions
        setIsSubmitting(true);

        const { totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity } = config;

        // Validate all fields are filled
        if (!totalTickets || !ticketReleaseRate || !customerRetrievalRate || !maxTicketCapacity) {
            setErrorMessage("All fields are required.");
            setIsSubmitting(false);
            return;
        }

        // Validate specific business rules
        if (parseInt(ticketReleaseRate, 10) <= 1000) {
            setErrorMessage("Ticket Release Rate must be greater than 1000 ms.");
            setIsSubmitting(false);
            return;
        }
        if (parseInt(customerRetrievalRate, 10) <= 1000) {
            setErrorMessage("Customer Retrieval Rate must be greater than 1000 ms.");
            setIsSubmitting(false);
            return;
        }
        if (parseInt(maxTicketCapacity, 10) > parseInt(totalTickets, 10)) {
            setErrorMessage("Max Ticket Capacity cannot exceed Total Tickets.");
            setIsSubmitting(false);
            return;
        }

        setErrorMessage(""); // Clear errors
        onConfigurationSaved(config); // Call callback with the validated configuration
        setIsSubmitting(false);
    };

    return (
        <form className="configuration-form" onSubmit={handleSubmit}>
            <div className="form-grid">
                {/* Total Tickets Field */}
                <div className="form-group">
                    <label>Total Tickets</label>
                    <input
                        type="text"
                        name="totalTickets"
                        value={config.totalTickets}
                        onChange={handleInputChange}
                        placeholder="Enter total tickets"
                    />
                </div>

                {/* Ticket Release Rate Field */}
                <div className="form-group">
                    <label>Ticket Release Rate (ms)</label>
                    <input
                        type="text"
                        name="ticketReleaseRate"
                        value={config.ticketReleaseRate}
                        onChange={handleInputChange}
                        placeholder="Enter ticket release rate"
                    />
                </div>

                {/* Customer Retrieval Rate Field */}
                <div className="form-group">
                    <label>Customer Retrieval Rate (ms)</label>
                    <input
                        type="text"
                        name="customerRetrievalRate"
                        value={config.customerRetrievalRate}
                        onChange={handleInputChange}
                        placeholder="Enter customer retrieval rate"
                    />
                </div>

                {/* Max Ticket Capacity Field */}
                <div className="form-group">
                    <label>Max Ticket Capacity</label>
                    <input
                        type="text"
                        name="maxTicketCapacity"
                        value={config.maxTicketCapacity}
                        onChange={handleInputChange}
                        placeholder="Enter max ticket capacity"
                    />
                </div>
            </div>

            {/* Error Message Display */}
            {errorMessage && <p className="error-message">{errorMessage}</p>}

            {/* Submit Button */}
            <button type="submit" disabled={isSubmitting}>
                Save Configuration
            </button>
        </form>
    );
};

export default ConfigurationForm;
