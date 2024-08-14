package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {
    @BeforeEach
    void setUp() {
        Flight.clearFlightRegistry();  // 在每个测试开始前清理飞行注册
    }

    @AfterEach
    void tearDown() {
        Flight.clearFlightRegistry();  // 在每个测试后清理飞行注册
    }

    @Test
    void testValidFlightCreation() {
        // 用有效值创建航班以测试正常情况
        assertDoesNotThrow(() -> {
            Flight flight = new Flight(1, "Tokyo", "New York", "JL123", "Japan Airlines",
                    Timestamp.valueOf("2024-12-31 10:30:00"), Timestamp.valueOf("2024-12-31 14:30:00"), new Airplane(1, "Boeing 787", 50, 150, 10));
        });
    }

    @Test
    void testMissingFields() {
        // 测试当任一字段为null或为空时应抛出异常
        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(2, null, "London", "BA123", "British Airways",
                    Timestamp.valueOf("2024-12-31 15:30:00"), Timestamp.valueOf("2024-12-31 19:30:00"), new Airplane(1, "Boeing 787", 50, 150, 10));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(2, "", "London", "BA123", "British Airways",
                    Timestamp.valueOf("2024-12-31 15:30:00"), Timestamp.valueOf("2024-12-31 19:30:00"), new Airplane(1, "Boeing 787", 50, 150, 10));
        });
    }

    @Test
    void testFlightIDUniqueness() {
        // 首次创建航班
        Flight flight1 = new Flight(3, "Beijing", "Shanghai", "CZ456", "China Southern",
                Timestamp.valueOf("2024-12-31 16:30:00"), Timestamp.valueOf("2024-12-31 20:30:00"), new Airplane(9999, "Boeing 737", 10, 100, 20));

        // 尝试用同一个ID再次创建航班应抛出异常
        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(3, "Beijing", "Shanghai", "CZ789", "China Southern",
                    Timestamp.valueOf("2024-12-31 21:30:00"), Timestamp.valueOf("2024-12-31 23:30:00"), new Airplane(9999, "Boeing 737", 10, 100, 20));
        });
    }

    @Test
    void testSetDepartToWithNull() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setDepartTo(null));
    }

    @Test
    void testSetDepartToWithEmptyString() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setDepartTo(""));
    }

    @Test
    void testSetDepartFromWithNull() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setDepartFrom(null));
    }

    @Test
    void testSetDepartFromWithEmptyString() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setDepartFrom(""));
    }

    @Test
    void testSetCodeWithNull() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setCode(null));
    }

    @Test
    void testSetCodeWithEmptyString() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setCode(""));
    }

    @Test
    void testSetCompanyWithNull() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setCompany(null));
    }

    @Test
    void testSetCompanyWithEmptyString() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setCompany(""));
    }

    @Test
    void testSetDateFromWithNull() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setDateFrom(null));
    }

    @Test
    void testSetDateToWithNull() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setDateTo(null));
    }

    @Test
    void testSetDateToWithInvalidDateOrder() {
        Flight flight = new Flight();
        flight.setDateFrom(Timestamp.valueOf("2024-12-31 10:30:00"));
        assertThrows(IllegalArgumentException.class, () -> flight.setDateTo(Timestamp.valueOf("2024-12-30 10:30:00")));
    }

    @Test
    void testSetAirplaneWithNull() {
        Flight flight = new Flight();
        assertThrows(IllegalArgumentException.class, () -> flight.setAirplane(null));
    }

    @Test
    void testToString() {
        Flight flight = new Flight(1, "Tokyo", "New York", "JL123", "Japan Airlines",
                Timestamp.valueOf("2024-12-31 10:30:00"), Timestamp.valueOf("2024-12-31 14:30:00"), new Airplane(1, "Boeing 787", 50, 150, 10));
        String expected = "Flight{flightID=1, departTo='Tokyo', departFrom='New York', code='JL123', company='Japan Airlines', dateFrom=2024-12-31 10:30:00.0, dateTo=2024-12-31 14:30:00.0, airplane=Airplane{model=Boeing 787', business sits=50', economy sits=150', crew sits=10'}}";
        assertEquals(expected, flight.toString());
    }

    @Test
    void testDateAndTimeFormat() {
        // 验证日期时间格式是否正确处理
        assertDoesNotThrow(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            sdf.setLenient(false);
            Timestamp dateFrom = new Timestamp(sdf.parse("31/12/24 10:30:00").getTime());
            Timestamp dateTo = new Timestamp(sdf.parse("31/12/24 14:30:00").getTime());

            new Flight(4, "Sydney", "Perth", "QF987", "Qantas",
                    dateFrom, dateTo, new Airplane(9999, "Boeing 737", 10, 100, 20));
        });
    }

    @Test
    void testInvalidDateAndTimeFormat() {
        // 验证日期时间格式不正确时抛出异常
        assertThrows(ParseException.class, () -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            sdf.setLenient(false);
            Timestamp dateFrom = new Timestamp(sdf.parse("2024-12-31 10:30:00").getTime());  // 错误的格式
            Timestamp dateTo = new Timestamp(sdf.parse("2024-12-31 14:30:00").getTime());  // 错误的格式

            new Flight(5, "Los Angeles", "San Francisco", "UA123", "United Airlines",
                    dateFrom, dateTo, new Airplane(9999, "Boeing 737", 10, 100, 20));
        });
    }
}
