package com.mycompany.ticketingcli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Configuration implements Serializable {
    private static int ticketIdCounter = 1; 
    private final int eventTicketId;
    private String vendorName;
    private String title; 
    private int maxTicketCapacity;
    private int ticketReleaseRate;
    private int ticketReleaseInterval; 
    private int customerRetrievalRate;
    private int customerRetrievalInterval;
    private int totalTickets;

    public Configuration() {
        // Generate a random ticket ID between 1 and 1,000,000 (inclusive)
        this.eventTicketId = ThreadLocalRandom.current().nextInt(1, 1_000_001); 
    }

// Helper method to prompt for integer input with validation
private int promptForIntegerInput(Scanner scanner, String promptMessage) {
    int value = -1;
    while (value < 0) {  // Ensure that value is a positive integer
        System.out.print(promptMessage);
        try {
            value = Integer.parseInt(scanner.nextLine());  // Attempt to parse the integer
            if (value < 0) {
                System.out.println("Please enter a positive integer.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
        }
    }
    return value;
}

// Loading the main prompt for user input
public void promptForInput(Scanner scanner) {
    // Prompt for textual input
    System.out.print("Enter Event Title: ");
    this.title = scanner.nextLine();
    this.title = scanner.nextLine();
    
    System.out.print("Enter Vendor Name: ");
    this.vendorName = scanner.nextLine();

    // Prompt for numeric input using the helper method
    this.totalTickets = promptForIntegerInput(scanner, "Enter Total Number of Tickets: ");
    this.maxTicketCapacity = promptForIntegerInput(scanner, "Enter Max Ticket Capacity: ");
    this.ticketReleaseRate = promptForIntegerInput(scanner, "Enter Ticket Release Rate: ");
    this.ticketReleaseInterval = promptForIntegerInput(scanner, "Enter Ticket Release Interval (in ms): ");
    this.customerRetrievalRate = promptForIntegerInput(scanner, "Enter Customer Retrieval Rate: ");
    this.customerRetrievalInterval = promptForIntegerInput(scanner, "Enter Customer Retrieval Interval (in ms): ");
}

    public int getEventTicketId() {
        return eventTicketId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getTitle() { 
        return title;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getTicketReleaseInterval() {
        return ticketReleaseInterval;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getCustomerRetrievalInterval() {
        return customerRetrievalInterval;
    }

    public int getTotalTickets() { 
        return totalTickets;
    }

    // Display the loaded configurations
    @Override
    public String toString() {
        return String.format("""
                             ===== Event Configuration =====
                             Event Title            : %s
                             Event Ticket ID        : %d
                             Vendor Name            : %s
                             Max Ticket Capacity    : %d
                             Ticket Release Rate    : %d
                             Release Interval       : %d ms
                             Customer Retrieval Rate: %d
                             Retrieval Interval     : %d ms
                             Total Tickets          : %d
                             
                             ===========================""",
                title, eventTicketId, vendorName, maxTicketCapacity, ticketReleaseRate, ticketReleaseInterval,
                customerRetrievalRate, customerRetrievalInterval, totalTickets
        );
    }


    // Save configurations to a JSON file, preserving existing configurations
    public static void saveToJsonFile(String filename, List<Configuration> configurations) throws IOException {
        List<Configuration> existingConfigurations = loadFromJsonFile(filename); // Load existing configurations
        existingConfigurations.addAll(configurations); // Add new configurations to the existing list

        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(existingConfigurations, writer); // Save the updated list to the file
            System.out.println(String.format("Configurations successfully saved to: %s", filename));
        } catch (IOException e) {
            System.err.println("Error saving configurations: " + e.getMessage());
            throw e;  // Re-throwing the exception after logging the error
        }
    }

    // Load configurations from a JSON file
    public static List<Configuration> loadFromJsonFile(String filename) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filename)) {
            Type listType = new TypeToken<List<Configuration>>() {}.getType();
            List<Configuration> configurations = gson.fromJson(reader, listType);
            if (configurations == null) {
                configurations = new ArrayList<>();  // Return an empty list if file is empty or invalid
            }
            System.out.println(String.format("Configurations successfully loaded from: %s", filename));
            return configurations;
        } catch (IOException e) {
            System.err.println("Error loading configurations: " + e.getMessage());
            throw e;  // Re-throwing the exception after logging the error
        }
    }


}
