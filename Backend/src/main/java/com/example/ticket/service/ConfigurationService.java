package com.example.ticket.service;

import com.example.ticket.model.ConfigurationManager;
import com.example.ticket.repo.ConfigurationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing configurations related to the ticket system.
 */
@Service // Marks this class as a Spring-managed service component
public class ConfigurationService {

    @Autowired // Injects the ConfigurationRepo dependency
    private ConfigurationRepo configurationRepo;

    /**
     * Saves the given configuration to the database.
     *
     * @param configurationManager The configuration to save.
     * @return The saved ConfigurationManager object.
     */
    public ConfigurationManager saveConfiguration(ConfigurationManager configurationManager) {
        return configurationRepo.save(configurationManager); // Persist configuration to the database
    }

    /**
     * Loads the configuration from the database.
     * Assumes a single configuration entry with ID = 1.
     *
     * @return The loaded ConfigurationManager object, or null if not found.
     */
    public ConfigurationManager loadConfiguration() {
        return configurationRepo.findById(1L).orElse(null); // Fetch configuration with ID = 1
    }

    /**
     * Resets the configuration to default values and saves it to the database.
     *
     * @return The reset ConfigurationManager object.
     */
    public ConfigurationManager resetConfiguration() {
        // Create a default configuration with pre-defined values
        ConfigurationManager defaultConfig = new ConfigurationManager();
        defaultConfig.setTotalTickets(100);
        defaultConfig.setTicketReleaseRate(500);
        defaultConfig.setCustomerRetrievalRate(3000);
        defaultConfig.setMaxTicketCapacity(50);

        return configurationRepo.save(defaultConfig); // Save and return the default configuration
    }
}
