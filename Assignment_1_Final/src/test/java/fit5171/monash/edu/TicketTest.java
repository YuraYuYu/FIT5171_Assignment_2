package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {
    private Flight flight;
    private Passenger childPassenger;
    private Passenger elderPassenger;
    private Passenger adultPassenger;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        Flight.clearFlightRegistry(); // Ensuring no ID conflicts in tests

        flight = new Flight(1, "Tokyo", "New York", "JL123", "Japan Airlines",
                Timestamp.valueOf("2024-12-31 10:30:00"), Timestamp.valueOf("2024-12-31 14:30:00"), new Airplane(1, "Boeing 787", 50, 150, 10));

        childPassenger = new Passenger("John", "Doe", 10, "Man", "john.doe@example.com", "0412345678", "AB1234567", "1234567890123456", 123);
        elderPassenger = new Passenger("Jane", "Doe", 65, "Woman", "Jane.doe@example.com", "0412345678", "AB1234569", "1234567890123459", 123);
        adultPassenger = new Passenger("James", "Doe", 30, "Man", "James.doe@example.com", "0412345678", "AB1234560", "1234567890123459", 123);
    }

    @Test
    void testTicketStatusInitialization() {
        ticket = new Ticket(100, 200, flight, true, adultPassenger);
        assertFalse(ticket.ticketStatus(), "Ticket should initially be not purchased.");
    }

    @Test
    void testTicketPriceAndDiscountForChild() {
        ticket = new Ticket(101, 200, flight, false, childPassenger);
        double discountedPrice = 200 * 0.5;
        int expectedPrice = (int) (discountedPrice * 1.12);
        assertEquals(expectedPrice, ticket.getPrice(), "儿童票价应在折扣后加税。");
    }

    @Test
    void testTicketPriceForElder() {
        ticket = new Ticket(102, 200, flight, false, elderPassenger);
        assertEquals(0, ticket.getPrice(), "老年人的票价应为免费。");
    }

    @Test
    void testTicketPriceForAdult() {
        ticket = new Ticket(103, 200, flight, false, adultPassenger);
        int expectedPrice = (int) (200 * 1.12);
        assertEquals(expectedPrice, ticket.getPrice(), "成人票价应仅加服务税。");
    }

    @Test
    void testServiceTaxApplication() {
        ticket = new Ticket(104, 100, flight, false, adultPassenger);
        ticket.setPrice(100); // 重新设置价格以确保服务税正确应用
        assertEquals(112, ticket.getPrice(), "Service tax should be applied correctly, 12% of 100 is 12, so new price should be 112.");
    }

    @Test
    void testValidTicketInformation() {
        ticket = new Ticket(105, 100, flight, true, adultPassenger);
        assertNotNull(ticket.getFlight(), "Flight information should be valid.");
        assertNotNull(ticket.getPassenger(), "Passenger information should be valid.");
    }

    // 测试 ticketStatus 方法更详细的用例
    @Test
    void testTicketStatusPurchased() {
        ticket = new Ticket(106, 200, flight, false, adultPassenger);
        ticket.setTicketStatus(true);
        assertTrue(ticket.ticketStatus(), "Ticket should be marked as purchased.");
    }

    // 测试 toString 方法
    @Test
    void testTicketToString() {
        ticket = new Ticket(107, 200, flight, false, adultPassenger);
        String expectedString = "Ticket{" + '\n' +
                "Price=" + ticket.getPrice() + "KZT, " + '\n' +
                flight + '\n' + "Vip status=" + ticket.getClassVip() + '\n' +
                adultPassenger + '\n' + "Ticket was purchased=" + ticket.ticketStatus() + "\n}";
        assertEquals(expectedString, ticket.toString(), "Ticket toString method should return the correct string.");
    }

    @AfterEach
    void tearDown() {
        // 清理代码，如有必要
    }
}
