package Test;

import com.mycompany.ticketingcli.TicketPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketPoolTest {
    private TicketPool pool;

    @BeforeEach
    public void setUp() {
        pool = new TicketPool("Vendor A", 10, 20, 5, 3, "Concert");
    }

    // Test case to test add tickets when the space available
    @Test
    public void testAddTicketsWhenSpaceAvailable() {
        pool.addTickets(5);
        assertEquals(5, pool.getTicketPoolSize(), "Pool size should be 5 after adding tickets.");
    }

    // Test case to add tickets when the pool is full
    @Test
    public void testAddTicketsWhenPoolFull() {
        pool.addTickets(10); 
        pool.addTickets(5); 
        assertEquals(10, pool.getTicketPoolSize(), "Pool size should not exceed max capacity.");
    }

    // Test case to purchase ticket when the pool is empty
    @Test
    public void testPurchaseTicketWhenPoolIsEmpty() {
        pool.purchaseTicket(); 
        assertEquals(0, pool.getTicketPoolSize(), "Pool size should remain 0 when empty.");
    }

    // Test case to purchase ticket when tickets are avilable
    @Test
    public void testPurchaseTicketWhenTicketsAvailable() {
        pool.addTickets(10);
        pool.purchaseTicket(); 
        assertEquals(7, pool.getTicketPoolSize(), "Pool size should decrease by retrieval rate.");
    }

}
