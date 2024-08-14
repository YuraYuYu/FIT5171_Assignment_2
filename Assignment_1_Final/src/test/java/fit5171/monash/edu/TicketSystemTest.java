package fit5171.monash.edu;
// Written by Ya Yu
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class TicketSystemTest {
    private TicketSystem ticketSystem;
    private Flight flight1;
    private Flight flight2;
    private Flight flight3;
    private Passenger passenger1;
    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;

    @BeforeEach
    public void setup() {
        Flight.clearFlightRegistry();
        TicketCollection.clearTickets();
        ticketSystem = new TicketSystem();

        Airplane airplane1 = new Airplane(1, "Boeing 737", 10, 100, 20);
        Airplane airplane2 = new Airplane(2, "Boeing 787", 50, 150, 10);
        Airplane fullAirplane = new Airplane(3, "Boeing 737", 0, 0, 10);
        flight1 = new Flight(1, "Sydney", "Melbourne", "SYD-MEL", "Qantas",
            java.sql.Timestamp.valueOf("2024-12-31 16:30:00"), java.sql.Timestamp.valueOf("2024-12-31 18:30:00"), airplane1);
        flight2 = new Flight(2, "Melbourne", "Beijing", "MEL-BJ", "Qantas",
                java.sql.Timestamp.valueOf("2024-12-31 10:30:00"), java.sql.Timestamp.valueOf("2024-12-31 14:30:00"), airplane2);
        flight3= new Flight(4, "Sydney", "Melbourne", "SYD-MEL", "Qantas",
            java.sql.Timestamp.valueOf("2024-12-31 16:30:00"), java.sql.Timestamp.valueOf("2024-12-31 18:30:00"), fullAirplane);
        passenger1 = new Passenger("John", "Doe", 30, "Man", "john.doe@example.com", "0412345678", "AB1234567", "1234567890123456", 123);
        ticket1 = new Ticket(123456, 300, flight1, false, passenger1);
        ticket2 = new Ticket(1234567, 300, flight2, false, passenger1);
        ticket3 = new Ticket(12345678, 300, flight1, false, passenger1);
        FlightCollection.addFlight(flight1);
        FlightCollection.addFlight(flight2);
        FlightCollection.addFlight(flight3);
        TicketCollection.addTicket(ticket1);
        TicketCollection.addTicket(ticket2);
        TicketCollection.addTicket(ticket3);
        ticket1.setTicketStatus(false);
        ticket2.setTicketStatus(false);
        ticket3.setTicketStatus(false);
    }

     @Test
    @DisplayName("Test buying transfer tickets")
    public void testBuyingTransferTickets() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            ticketSystem.chooseTicket("Beijing", "Sydney", 1234567, 12345678);
        });
        assertEquals("Successfully purchased transfer tickets.", exception.getMessage());
    }

    @Test
    @DisplayName("Test buying direct ticket")
    public void testBuyingDirectTicket() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            ticketSystem.chooseTicket("Melbourne", "Sydney", 123456, 0);
        });
        assertEquals("Successfully purchased a direct ticket.", exception.getMessage());
    }

    @Test
    @DisplayName("Test no possible variants")
    public void testNoPossibleVariants() {
        Exception exception = assertThrows(Exception.class, () -> {
            ticketSystem.chooseTicket("Beijing", "Shanghai", 0, 0);
        });
        assertEquals("There are no possible variants.", exception.getMessage());
    }

    @Test
    @DisplayName("Choose Already Booked Ticket Test")
    void chooseAlreadyBookedTicket_Test() {
        ticketSystem.bookTicket(ticket3);

        Exception exception = assertThrows(Exception.class, () -> {
            ticketSystem.buyTicket(12345678);
        });

        assertEquals("Ticket already booked", exception.getMessage());
    }

    @Test
    @DisplayName("Validate Passenger Information Mismatch Test")
    public void validatePassengerInformationMismatch_Test() throws Exception {
        Passenger mismatchedPassenger = new Passenger("Jane", "Smith", 30, "Woman", "jane.smith@example.com", "0412345679", "987654321", "1234567890123236", 321);
        assertTrue(ticketSystem.validatePassengerInformation(ticket1.getPassenger(), ticket1.getPassenger()));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketSystem.validatePassengerInformation(mismatchedPassenger, ticket1.getPassenger());
        });
        assertEquals("Passenger information does not match", exception.getMessage());
    }

    @Test
    @DisplayName("Validate Flight Information Test")
    public void testValidateFlightInformation() throws Exception {
        Flight invalidFlight = new Flight();
        assertTrue(ticketSystem.validateFlightInformation(flight1));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketSystem.validateFlightInformation(invalidFlight);
        });
        assertEquals("Invalid flight information provided", exception.getMessage());
    }

    @Test
    @DisplayName("Validate Ticket Information Test")
    public void testValidateTicketInformation() throws Exception {
        assertTrue(ticketSystem.validateTicketInformation(ticket1));
        Ticket invalidTicket = new Ticket(-1, 0, flight2, false, passenger1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketSystem.validateTicketInformation(invalidTicket);
        });
        assertEquals("Invalid ticket information provided", exception.getMessage());
        Ticket invalidTicket5 = new Ticket(0, 0, flight2, false, passenger1);
        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> {
            ticketSystem.validateTicketInformation(invalidTicket5);
        });
        assertEquals("Invalid ticket information provided", exception5.getMessage());

        Ticket invalidTicket2 = null;
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            ticketSystem.validateTicketInformation(invalidTicket2);
        });
        assertEquals("Ticket information is missing", exception2.getMessage());

        Ticket invalidTicket3 = new Ticket(1, 300, null, false, passenger1);
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            ticketSystem.validateTicketInformation(invalidTicket3);
        });
        assertEquals("Invalid flight information provided", exception3.getMessage());

        Ticket invalidTicket4 = new Ticket(1, -1, flight1, false, passenger1);
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            ticketSystem.validateTicketInformation(invalidTicket4);
        });
        assertEquals("Invalid ticket information provided", exception4.getMessage());
    }

    @Test
    @DisplayName("Display Correct Value when Buying Ticket Test")
    void displayCorrectValue_When_Buying_Ticket_Test() throws Exception {
        double ticketPrice = ticket3.getPrice();
        String displayMessage = ticketSystem.buyTicket(12345678);
        String expectedOutput = String.format("Ticket purchased successfully for $%.2f", ticketPrice);
        assertEquals(expectedOutput, displayMessage);

        double ticketPrice2 = ticket1.getPrice() + ticket2.getPrice();
        String displayMessage2 = ticketSystem.buyTicket2(123456, 1234567);
        String expectedOutput2 = String.format("Ticket purchased successfully for $%.2f", ticketPrice2);
        assertEquals(expectedOutput2, displayMessage2);
    }

    @Test
    public void testNullInputToBuyTicket() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketSystem.buyTicket(null);
        });
        assertEquals("Ticket ID cannot be null", exception.getMessage());
    }

    @Test
    public void testPurchaseWithNoSeats() {
        Ticket fullTicket = new Ticket(123459, 300, flight3, true, passenger1);
        TicketCollection.addTicket(fullTicket);

        Exception exception = assertThrows(Exception.class, () -> {
            ticketSystem.buyTicket(123459);
        });
        assertEquals("No seats available on this flight", exception.getMessage());
    }

    @Test
    @DisplayName("Test null passenger validation")
    public void testNullPassengerValidation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketSystem.validatePassengerInformation(null, passenger1);
        });
        assertEquals("Passenger information does not match", exception.getMessage());
    }

    @Test
    @DisplayName("Test flight with invalid airplane ID")
    public void testInvalidAirplaneID() {
        Flight flight = new Flight();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flight.setAirplane(null);
        });
        assertEquals("Invalid airplane information provided", exception.getMessage());
    }
}

