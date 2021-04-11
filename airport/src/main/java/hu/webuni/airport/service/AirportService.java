package hu.webuni.airport.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.webuni.airport.model.Airport;


public class AirportService {

	
	private Map<Long, Airport> airports = new HashMap<>();
	
	{
		airports.put(1L, new Airport(1,"abc","xyz"));
		airports.put(2L, new Airport(2,"abc","xyz"));
	}
	
	
	public Airport save(Airport airport) {
		checkUniqueIata(airport.getIata());
		airports.put(airport.getId(),airport);
		return airport;
	}
	
	public Airport modify(long id, Airport airport) {
		//checkUniqueIata(airport.getIata());
		
		airport.setId(airport.getId());
		airports.put(id, airport);
		
		return airport;
	}
	
	private void checkUniqueIata(String iata) {
		Optional<Airport> airportWithSameIata =
				airports.values().stream()
				.filter(a -> a.getIata().equals(iata))
				.findAny();
		
		if(airportWithSameIata.isPresent())
			throw new NonUniqueIataException(iata);
	}
	
	public List<Airport> findAll(){
		return new ArrayList<>(airports.values());
	}
	
	public Airport findById(long id) {
		return airports.get(id);
	}
	
	public void delete(long id) {
		airports.remove(id);
	}
}