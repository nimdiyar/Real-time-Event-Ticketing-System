package com.mycompany.ticketingcli;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseRate;
    private final int releaseInterval; 

    // Constructor to initialize the Vendor
    public Vendor(TicketPool ticketPool, int releaseRate,int releaseInterval ) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.releaseInterval = releaseInterval; 
    }

    @Override
    public void run() {
        // Using a try-catch block to properly handle interruptions
        try {
            while (isRunning()) {
                releaseTickets();
                sleepBeforeNextRelease();
            }
        } catch (InterruptedException e) {
            handleInterruption();
        }
    }

    private boolean isRunning() {
        return !Thread.currentThread().isInterrupted();
    }

    private void releaseTickets() {
        ticketPool.addTickets(releaseRate);
    }

    private void sleepBeforeNextRelease() throws InterruptedException {
        Thread.sleep(releaseInterval);
    }

    private void handleInterruption() {
        System.out.println("Vendor thread interrupted. Exiting...");
        Thread.currentThread().interrupt(); // Restore interrupt status
    }

}
