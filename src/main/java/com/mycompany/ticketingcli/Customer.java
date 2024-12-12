package com.mycompany.ticketingcli;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate; 
    private final int retrievalInterval; 

    public Customer(TicketPool ticketPool, int retrievalRate, int retrievalInterval) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run() {
        try {
            while (isRunning()) {
                purchaseTicket();
                sleepBeforeNextRetrieval();
            }
        } catch (InterruptedException e) {
            handleInterruption();
        }
    }

    private boolean isRunning() {
        return !Thread.currentThread().isInterrupted();
    }

    private void purchaseTicket() {
        ticketPool.purchaseTicket();
    }

    private void sleepBeforeNextRetrieval() throws InterruptedException {
        Thread.sleep(retrievalInterval);
    }

    private void handleInterruption() {
        System.out.println("Customer thread interrupted.");
        Thread.currentThread().interrupt(); // Restore interrupt status
    }

}
