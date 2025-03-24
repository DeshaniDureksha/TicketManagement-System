package com.example.ticket.controller;

import com.example.ticket.model.ConfigurationManager;
import com.example.ticket.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @Autowired
    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * Save or update the configuration settings.
     *
     * @param configurationManager The configuration settings to be saved
     * @return ResponseEntity containing the saved configuration
     */
    @PostMapping("/save")
    public ResponseEntity<ConfigurationManager> saveConfiguration(@RequestBody ConfigurationManager configurationManager) {
        try {
            ConfigurationManager savedConfig = configurationService.saveConfiguration(configurationManager);
            return ResponseEntity.ok(savedConfig);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Load the current configuration settings.
     *
     * @return ResponseEntity containing the current configuration
     */
    @GetMapping("/load")
    public ResponseEntity<ConfigurationManager> loadConfiguration() {
        ConfigurationManager config = configurationService.loadConfiguration();
        if (config != null) {
            return ResponseEntity.ok(config);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Reset the configuration settings to default.
     *
     * @return ResponseEntity containing the default configuration
     */
    @PostMapping("/reset")
    public ResponseEntity<ConfigurationManager> resetConfiguration() {
        ConfigurationManager defaultConfig = configurationService.resetConfiguration();
        return ResponseEntity.ok(defaultConfig);
    }
}
