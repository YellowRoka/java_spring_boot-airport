package hu.webuni.airport.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.mapper.AirportMapper;
import hu.webuni.airport.model.Airport;
import hu.webuni.airport.service.AirportService;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

	@Autowired
	AirportService airportService;
	
	@Autowired
	AirportMapper airportMapper;
	
	
	@GetMapping
	public List<AirportDto> getAll(){
		return airportMapper.airportsToDtos(airportService.findAll());
	}
	
	
	@GetMapping("/{id}")
	public AirportDto getById(@PathVariable long id) {
		Airport airport = airportService.findById(id);
		
		if(airport != null)
			return airportMapper.airportsToDto(airport);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public AirportDto createAirport(@ RequestBody @Valid AirportDto airportDto) {
		Airport airport = airportService.save(airportMapper.dtoToAirport(airportDto));
		return airportMapper.airportsToDto(airport);
	}
	
	
	@PutMapping("/{id}")
	public AirportDto modifyAirprt(@PathVariable long id , @RequestBody AirportDto airportDto) {
		Airport airport = airportService.findById(id);
		if(null == airport)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		Airport moderAirport = airportMapper.dtoToAirport(airportDto);
		
		Airport moddedAirport = airportService.modify(id,moderAirport);
		
		return airportMapper.airportsToDto(moddedAirport);
	}
	

	@DeleteMapping("/{id}")
	public void deleteAirport(@PathVariable long id) {
		airportService.delete(id);
	}

}
