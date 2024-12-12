package com.mycompany.ticketingcli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketingCLI {

    private static volatile boolean isRunning = false;
    private static TicketPool ticketPool = null;

    public static void main(String[] args) {
        List<Configuration> configurations = new ArrayList<>();
        Thread vendorThread = null;
        Thread customerThread = null;

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Ticket Management System =====");
            System.out.println("1. Add Configuration");
            System.out.println("2. Load Configurations");
            System.out.println("3. Display Configurations");
            System.out.println("4. Start Simulation");
            System.out.println("5. Stop Simulation");
            System.out.println("6. Exit\n");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            System.out.println();

            if (option == 1) {
                // Add Configuration
                Configuration config = new Configuration();
                config.promptForInput(scanner);
                configurations.add(config);

                try {
                    Configuration.saveToJsonFile("configurations.json", configurations);
                    System.out.println("Configuration added and saved.\n");
                } catch (IOException e) {
                    System.out.println("Failed to save configurations: " + e.getMessage() + "\n");
                }
            } else if (option == 2) {
                // Load Configurations
                try {
                    configurations = Configuration.loadFromJsonFile("configurations.json");
                    System.out.println("Configurations loaded successfully.\n");
                } catch (IOException e) {
                    System.out.println("Failed to load configurations: " + e.getMessage() + "\n");
                }
            } else if (option == 3) {
                // Display Configurations
                if (configurations.isEmpty()) {
                    System.out.println("No configurations available.\n");
                } else {
                    System.out.println("Available Configurations:");
                    configurations.forEach(System.out::println);
                    System.out.println();
                }
            } else if (option == 4) {
                // Start Simulation
                if (configurations.isEmpty()) {
                    System.out.println("Please add or load configurations before starting a simulation.\n");
                } else {
                    System.out.print("Enter Event Ticket ID to start simulation: ");
                    int ticketId = scanner.nextInt();

                    Configuration selectedConfig = configurations.stream()
                            .filter(c -> c.getEventTicketId() == ticketId)
                            .findFirst()
                            .orElse(null);

                    if (selectedConfig == null) {
                        System.out.println("Invalid Event Ticket ID.\n");
                    } else if (isRunning) {
                        System.out.println("Simulation is already running.\n");
                    } else {
                        // Initialize Ticket Pool with max ticket capacity
                        ticketPool = new TicketPool(
                                selectedConfig.getVendorName(),
                                selectedConfig.getMaxTicketCapacity(),
                                selectedConfig.getTotalTickets(),
                                selectedConfig.getTicketReleaseRate(),
                                selectedConfig.getCustomerRetrievalRate(),
                                selectedConfig.getTitle()
                        );
                        System.out.println("Ticket pool initialized with a capacity of " + selectedConfig.getMaxTicketCapacity() + ".");

                        isRunning = true;

                        // Create vendor and customer threads
                        vendorThread = new Thread(new Vendor(ticketPool, selectedConfig.getTicketReleaseRate(), selectedConfig.getTicketReleaseInterval()));
                        customerThread = new Thread(new Customer(ticketPool, selectedConfig.getCustomerRetrievalRate(), selectedConfig.getCustomerRetrievalInterval()));

                        vendorThread.start();
                        customerThread.start();

                        System.out.println("Vendor and Customer threads started.");

                        // Monitor Thread for ticket pool status
                        new Thread(() -> {
                            while (isRunning) {
                                try {
                                    Thread.sleep(2000); // Monitor every 2 seconds
                                    if (ticketPool != null) {
                                        int currentTicketCount = ticketPool.getTicketPoolSize();

                                        // Stop simulation when current ticket count becomes 0
                                        if (currentTicketCount == 0 && !ticketPool.isSimulationComplete()) {
                                            ticketPool.stopSimulation();
                                            isRunning = false;
                                            System.out.println("Simulation stopped automatically as ticket pool is empty.\n");
                                        }
                                    }
                                } catch (InterruptedException e) {
                                    System.out.println("Monitoring interrupted.");
                                }
                            }
                        }).start();
                    }
                }
            } else if (option == 5) {
                // Stop Simulation
                if (!isRunning) {
                    System.out.println("Simulation is not running.\n");
                } else {
                    isRunning = false;
                    if (vendorThread != null) {
                        vendorThread.interrupt();
                    }
                    if (customerThread != null) {
                        customerThread.interrupt();
                    }

                    if (ticketPool != null) {
                        ticketPool.stopSimulation();
                    }

                    System.out.println("Simulation stopped.\n");
                }
            } else if (option == 6) {
                // Exit
                System.out.println("Exiting the system. Goodbye!");
                break;
            } else {
                System.out.println("Invalid option. Please select a valid option from the menu.\n");
            }
        }
    }
}
