package fit5171.monash.edu;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Flight {
    private static Set<Integer> flightIDs = new HashSet<>();
    private int flightID;
    private String departTo;
    private String departFrom;
    private String code;
    private String company;
    private Timestamp dateFrom;
    private Timestamp dateTo;
    Airplane airplane;

    public Flight() {}

    public Flight(int flight_id, String departTo, String departFrom, String code, String company, Timestamp dateFrom, Timestamp dateTo, Airplane airplane) {
        if (departTo == null || departTo.isEmpty() || departFrom == null || departFrom.isEmpty() ||
                code == null || code.isEmpty() || company == null || company.isEmpty() ||
                dateFrom == null || dateTo == null || airplane == null) {
            throw new IllegalArgumentException("All fields must be filled");
        }

        if (dateFrom.after(dateTo)) {
            throw new IllegalArgumentException("dateFrom cannot be after dateTo");
        }

        if (!flightIDs.add(flight_id)) {
            throw new IllegalArgumentException("Flight ID already exists in the system");
        }

        this.flightID = flight_id;
        this.departTo = departTo;
        this.departFrom = departFrom;
        this.code = code;
        this.company = company;
        this.airplane = airplane;
        this.dateTo = dateTo;
        this.dateFrom = dateFrom;
    }

    public static void clearFlightRegistry() {
        flightIDs.clear();
    }

    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightid) {
        this.flightID = flightid;
    }

    public String getDepartTo() {
        return departTo;
    }

    public void setDepartTo(String departTo) {
        if (departTo == null || departTo.isEmpty()) {
            throw new IllegalArgumentException("Invalid departure destination");
        }
        this.departTo = departTo;
    }

    public String getDepartFrom() {
        return departFrom;
    }

    public void setDepartFrom(String departFrom) {
        if (departFrom == null || departFrom.isEmpty()) {
            throw new IllegalArgumentException("Invalid departure origin");
        }
        this.departFrom = departFrom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Invalid flight code");
        }
        this.code = code;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        if (company == null || company.isEmpty()) {
            throw new IllegalArgumentException("Invalid company name");
        }
        this.company = company;
    }

    public Timestamp getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Timestamp dateFrom) {
        if (dateFrom == null) {
            throw new IllegalArgumentException("Invalid dateFrom information provided");
        }
        this.dateFrom = dateFrom;
    }

    public Timestamp getDateTo() {
        return dateTo;
    }

    public void setDateTo(Timestamp dateTo) {
        if (dateTo == null) {
            throw new IllegalArgumentException("Invalid dateTo information provided");
        }
        if (dateFrom != null && dateFrom.after(dateTo)) {
            throw new IllegalArgumentException("dateFrom cannot be after dateTo");
        }
        this.dateTo = dateTo;
    }

    public void setAirplane(Airplane airplane) {
        if (airplane == null) {
            throw new IllegalArgumentException("Invalid airplane information provided");
        }
        this.airplane = airplane;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightID=" + flightID +
                ", departTo='" + departTo + '\'' +
                ", departFrom='" + departFrom + '\'' +
                ", code='" + code + '\'' +
                ", company='" + company + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", airplane=" + airplane +
                '}';
    }
}
