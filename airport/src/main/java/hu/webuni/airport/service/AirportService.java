package hu.webuni.airport.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;

@Service
public class AirportService {
	
	//@PersistenceContext
	//EntityManager em;
	
	AirportRepository airportRepository;
	FlightRepository flightRepository;
	LogEntryService logEntryService;

	public AirportService(AirportRepository airportRepository, FlightRepository flightRepository, LogEntryService logEntryService) {
		super();
		this.airportRepository = airportRepository;
		this.flightRepository = flightRepository;
		this.logEntryService =logEntryService;
	}
	
	@Transactional
	public Airport save(Airport airport) {
		checkUniqueIata(airport.getIata(), airport.getId());
		//em.persist(airport);
		return airportRepository.save(airport);
	}
	
	@Transactional
	public Airport update(Airport airport) {
		checkUniqueIata(airport.getIata(), airport.getId());
		//return em.merge(airport);
		if(airportRepository.existsById(airport.getId())) {
			logEntryService.createLog(String.format("Airport modification with id: %d, new name is: %s", airport.getId(),airport.getName()) );
			//callBackendSystem(); időzítés random elszállásra, log hiba tesztre
			return airportRepository.save(airport);
		}
		else {
			throw new NoSuchElementException();
		}
	} 
	private void callBackendSystem() {
		if(new Random().nextInt(3) == 1) {
			throw new RuntimeException();
		}
	}
		
	private void checkUniqueIata(String iata, Long id) {
		boolean forUpdate = (id!=null);
		//TypedQuery<Long> query = em.createNamedQuery(forUpdate ?  "Airport.countByIataAndIdNotIn" : "Airport.countByIata", Long.class)
		//	.setParameter("iata", iata);
		
		//if(forUpdate)
		//	query.setParameter("id", id);
		
		//long count = query.getSingleResult();
		
		Long count = forUpdate?
				airportRepository.countByIataAndIdNot(iata, id) : 
				airportRepository.countByIata(iata);
		
		if(count>0)
			throw new NonUniqueIataException(iata);
	}
	
	public List<Airport> findAll(){
		//return em.createQuery("SELECT a FROM Airport a",Airport.class).getResultList();
		return airportRepository.findAll();
	}
	
	public Optional<Airport> findById(long id) {
		//return em.find(Airport.class, id);
		return airportRepository.findById(id);
	}
	
	@Transactional
	public void delete(long id) {
		//em.remove(findById(id));
		airportRepository.deleteById(id);
	}
	

	@Transactional
	public Flight createFlight(String flightNumber, long takeOffAirportId, long landingAirportId, LocalDateTime date) {
		Flight flight = new Flight();
		
		flight.setFlightNumber(flightNumber);
		flight.setTakeOff(airportRepository.findById(takeOffAirportId).get());
		flight.setLanding(airportRepository.findById(landingAirportId).get());
		
		//LocalDateTime date = LocalDateTime.of(2021, 4,10,0,0);
		flight.setTakeOffTime(date);	
		return flightRepository.save(flight);
	}
	
	/**
	 * @param example
	 * @return
	 */
	public List<Flight> findFlightByExample(Flight example){
		long id = example.getId();
		String number = example.getFlightNumber();
		String iata = null;
		Airport takeOff = example.getTakeOff();
		LocalDateTime time = example.getTakeOffTime();
		
		if(takeOff != null) {
			iata = takeOff.getIata();	
		}
		
		Specification<Flight> spec = Specification.where(null);
		if(id>0) {
			spec = spec.and(FlightSpecifications.hasId(id));
		}
		
		if (StringUtils.hasText(number)) {
			spec = spec.and(FlightSpecifications.hasFlightNumber(number));
		}
		
		if (StringUtils.hasText(iata)) {
			spec = spec.and(FlightSpecifications.hasTakeOffIATA(iata));
		}
		
		if (time != null) {
			spec = spec.and(FlightSpecifications.hasTakeOffTime(time));
		}
		
		return flightRepository.findAll(spec, Sort.by("id"));
	}
}