package hu.webuni.airport.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class AirportServiceIT {

	@Autowired
	AirportService airportService;

	@Autowired
	AirportRepository airportRepository;
	
	@Autowired
	FlightRepository flightRepository;
	
	@BeforeEach
	public void init() {
		flightRepository.deleteAll();
		airportRepository.deleteAll();
	}
	@Test
	void testCreateFlight() throws Exception{
		
		String flightNumber = "ABC123";
		long takeOffAirportId = createAirport("air1","iata1");
		long landingAirportId = createAirport("air2","iata2");
		LocalDateTime date = LocalDateTime.now();
		Flight flightId = airportService.createFlight(flightNumber, takeOffAirportId, landingAirportId, date);
		
		Optional<Flight> savedFlight = flightRepository.findById(flightId.getId());
		assertThat(savedFlight).isNotEmpty();
		
		assertThat(savedFlight.get().getFlightNumber()).isEqualTo(flightNumber);
		//assertThat(savedFlight.get().getTakeOffTime()).isCloseTo(date, new TemporalUnitWithinOffset(1, ChronoUnit.MICROS));
		assertThat(savedFlight.get().getTakeOffTime()).isCloseTo(date, Assertions.within(1, ChronoUnit.MICROS));
		assertThat(savedFlight.get().getTakeOff().getId()).isEqualTo(takeOffAirportId);
		assertThat(savedFlight.get().getLanding().getId()).isEqualTo(landingAirportId);
	}

	private long createAirport(String name, String iata) {
		return airportRepository.save(new Airport(name, iata)).getId();
	}
	
	@Test
	void testFindFlightsByExample() throws Exception{
		long a1Id = createAirport("airp1", "iata1");
		long a2Id = createAirport("airp2", "iata2");
		long a3Id = createAirport("airp3", "2iata");
		long a4Id = createAirport("airp4", "3iata1");
		
		LocalDateTime takeOff = LocalDateTime.of(2021,04,23,8,0,0);
		
		long flight1 = createFlight("ABC123",a1Id,a3Id, takeOff);
		long flight2 = createFlight("ABC1234",a2Id,a3Id, takeOff.plusHours(2));
		long flight3 = createFlight("BC123",a1Id,a3Id, takeOff);
		long flight4 = createFlight("ABC123",a1Id,a3Id, takeOff.plusHours(1));
		
		createFlight("ABC123", a3Id, a3Id, takeOff);
		
		Flight example = new Flight();
		example.setFlightNumber("ABC123");
		example.setTakeOffTime(takeOff);
		example.setTakeOff(new Airport("sasa","iata"));
		
		List<Flight> foundFlights = this.airportService.findFlightByExample(example);
		
		assertThat(foundFlights.stream().map(Flight::getId).collect(Collectors.toList())).contains(flight1, flight2);
	}

	private long createFlight(String flightNumber, long takeOffAirportId, long landingAirportId, LocalDateTime date) {
		return airportService.createFlight(flightNumber, takeOffAirportId, landingAirportId, date).getId();
		
	}
}
