package com.bank.cli;

import com.bank.db.DatabaseManager;
import com.bank.cli.display.MenuDisplay;

/**
 * Main entry point for the CLI Banking Application.
 * This class initializes the application and starts the main menu loop.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to CLI Banking Application ===");
        System.out.println("Initializing application...");
        
        try {
            // Initialize database
            DatabaseManager dbManager = DatabaseManager.getInstance();
            
            if (!dbManager.isConnected()) {
                System.err.println("Failed to connect to database. Exiting...");
                System.exit(1);
            }
            
            System.out.println("Application initialized successfully!");
            System.out.println("==========================================");
            
            // Start the main menu
            MenuDisplay menuDisplay = new MenuDisplay();
            menuDisplay.showMainMenu();
            
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            // Cleanup resources
            try {
                DatabaseManager.getInstance().close();
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
    }
}
