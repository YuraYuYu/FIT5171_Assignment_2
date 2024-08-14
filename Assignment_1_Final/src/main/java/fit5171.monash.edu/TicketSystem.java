package fit5171.monash.edu;

public class TicketSystem {

    Passenger passenger;
    Ticket ticket;
    Flight flight;

    public TicketSystem() {
        passenger = new Passenger("John", "Doe", 30, "Man", "john.doe@example.com", "0412345678", "AB1234567", "1234567890123456", 123);
        ticket = new Ticket(123456, 300, null, false, passenger);
        flight = new Flight();
    }

    public String buyTicket(Integer ticket_id) throws Exception {
        if (ticket_id == null) {
            throw new IllegalArgumentException("Ticket ID cannot be null");
        }

        Ticket validTicket = TicketCollection.getTicketInfo(ticket_id);
        if (validTicket == null) {
            throw new Exception("This ticket does not exist.");
        }

        if (validTicket.ticketStatus()) {
            throw new Exception("Ticket already booked");
        }

        Passenger inputPassenger = new Passenger("John", "Doe", 30, "Man", "john.doe@example.com",
                                                 "0412345678", "AB1234567", "1234567890123456", 123);

        validatePassengerInformation(validTicket.getPassenger(), inputPassenger);

        Flight flight = validTicket.getFlight();
        if (flight == null) {
            throw new Exception("Flight does not exist.");
        }

        Airplane airplane = flight.getAirplane();
        if (airplane == null) {
            throw new Exception("Airplane does not exist.");
        }

        if (airplane.getBusinessSitsNumber() <= 0 && airplane.getEconomySitsNumber() <= 0) {
            throw new Exception("No seats available on this flight");
        }

        if (validatePassengerInformation(validTicket.getPassenger(), inputPassenger)) {
            validTicket.setTicketStatus(true);
        }

        if (validTicket.getClassVip()) {
            airplane.setBusinessSitsNumber(airplane.getBusinessSitsNumber() - 1);
        } else {
            airplane.setEconomySitsNumber(airplane.getEconomySitsNumber() - 1);
        }

        return String.format("Ticket purchased successfully for $%.2f", (double) validTicket.getPrice());
    }

    public String buyTicket2(int ticket_id_first, int ticket_id_second) throws Exception {
        Ticket validTicketFirst = TicketCollection.getTicketInfo(ticket_id_first);
        Ticket validTicketSecond = TicketCollection.getTicketInfo(ticket_id_second);

        if (validTicketFirst == null || validTicketSecond == null) {
            throw new Exception("This ticket does not exist.");
        }

        Passenger inputPassenger = new Passenger("John", "Doe", 30, "Man", "john.doe@example.com", "0412345678", "AB1234567", "1234567890123456", 123);
        validatePassengerInformation(validTicketFirst.getPassenger(), inputPassenger);
        validatePassengerInformation(validTicketSecond.getPassenger(), inputPassenger);

        Flight flightFirst = validTicketFirst.getFlight();
        Airplane airplaneFirst = flightFirst.getAirplane();

        Flight flightSecond = validTicketSecond.getFlight();
        Airplane airplaneSecond = flightSecond.getAirplane();

        ticket = TicketCollection.getTicketInfo(ticket_id_first);
        ticket.setTicketStatus(true);

        if (ticket.getClassVip()) {
            airplaneFirst.setBusinessSitsNumber(airplaneFirst.getBusinessSitsNumber() - 1);
        } else {
            airplaneFirst.setEconomySitsNumber(airplaneFirst.getEconomySitsNumber() - 1);
        }

        Ticket ticketSecond = TicketCollection.getTicketInfo(ticket_id_second);
        ticketSecond.setTicketStatus(true);

        if (ticketSecond.getClassVip()) {
            airplaneSecond.setBusinessSitsNumber(airplaneSecond.getBusinessSitsNumber() - 1);
        } else {
            airplaneSecond.setEconomySitsNumber(airplaneSecond.getEconomySitsNumber() - 1);
        }

        return String.format("Ticket purchased successfully for $%.2f", (double) (ticket.getPrice() + ticketSecond.getPrice()));
    }

    public void chooseTicket(String city1, String city2, int ticket_id_first, int ticket_id_second) throws Exception {
        Flight flight = FlightCollection.getFlightInfo(city1, city2);

        if (flight != null) {
            // Direct ticket logic
            buyTicket(ticket_id_first);
            throw new Exception("Successfully purchased a direct ticket.");
        } else {
            // Connecting flight logic
            Flight depart_to = FlightCollection.getFlightInfo(city2);
            if (depart_to != null) {
                String connectCity = depart_to.getDepartFrom();
                Flight flightConnectingTwoCities = FlightCollection.getFlightInfo(city1, connectCity);

                if (flightConnectingTwoCities != null) {
                    // Ensure ticket IDs are valid
                    if (ticket_id_first == 0 || ticket_id_second == 0) {
                        throw new Exception("Invalid ticket ID provided.");
                    }

                    // Fetch tickets and validate
                    Ticket ticketFirst = TicketCollection.getTicketInfo(ticket_id_first);
                    Ticket ticketSecond = TicketCollection.getTicketInfo(ticket_id_second);

                    if (ticketFirst == null || ticketSecond == null) {
                        throw new Exception("One of the provided tickets does not exist.");
                    }

                    if (ticketFirst.getFlight().getFlightID() != flightConnectingTwoCities.getFlightID()
                            || ticketSecond.getFlight().getFlightID() != depart_to.getFlightID()) {
                        throw new Exception("The provided tickets do not correspond to the correct flights.");
                    }

                    // Purchase connecting flights
                    buyTicket2(ticket_id_first, ticket_id_second);
                    throw new Exception("Successfully purchased transfer tickets.");
                }
            }
            throw new Exception("There are no possible variants.");
        }
    }

    public void bookTicket(Ticket ticket) {
        ticket.setTicketStatus(true);
    }

    public boolean validatePassengerInformation(Passenger ticketPassenger, Passenger inputPassenger) throws Exception {
        if (ticketPassenger == null || inputPassenger == null) {
            throw new IllegalArgumentException("Passenger information does not match");
        }
        if (!ticketPassenger.getFirstName().equals(inputPassenger.getFirstName()) ||
            !ticketPassenger.getSecondName().equals(inputPassenger.getSecondName()) ||
            ticketPassenger.getAge() != inputPassenger.getAge() ||
            !ticketPassenger.getGender().equals(inputPassenger.getGender()) ||
            !ticketPassenger.getEmail().equals(inputPassenger.getEmail()) ||
            !ticketPassenger.getPhoneNumber().equals(inputPassenger.getPhoneNumber()) ||
            !ticketPassenger.getPassport().equals(inputPassenger.getPassport())) {
            throw new IllegalArgumentException("Passenger information does not match");
        }
        return true;
    }

    public boolean validateFlightInformation(Flight flight) throws Exception {
        if (flight == null) {
            throw new IllegalArgumentException("Flight information is missing");
        }
        if (flight.getDepartFrom() == null || flight.getDepartFrom().isEmpty() ||
            flight.getDepartTo() == null || flight.getDepartTo().isEmpty() ||
            flight.getCode() == null || flight.getCode().isEmpty() ||
            flight.getCompany() == null || flight.getCompany().isEmpty() ||
            flight.getDateFrom() == null || flight.getDateTo() == null ||
            flight.getAirplane() == null) {
            throw new IllegalArgumentException("Invalid flight information provided");
        }
        return true;
    }

    public boolean validateTicketInformation(Ticket ticket) throws Exception {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket information is missing");
        }
        if (ticket.getFlight() == null ||
            ticket.getPassenger() == null ||
            ticket.getTicket_id() <= 0 ||
            ticket.getPrice() < 0) {
            throw new IllegalArgumentException("Invalid ticket information provided");
        }
        return true;
    }

}