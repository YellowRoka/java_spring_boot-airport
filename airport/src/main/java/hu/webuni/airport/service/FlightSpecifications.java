package hu.webuni.airport.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.airport.model.Airport_;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.model.Flight_;

public class FlightSpecifications {

	public static Specification<Flight> hasId(long id){
		return(root,cq,cb)->cb.equal(root.get(Flight_.id),id);
	}

	public static Specification<Flight> hasFlightNumber(String number) {
		//return(root,cq,cb)->cb.equal(root.get(Flight_.flightNumber),number);
		return(root,cq,cb)->cb.like(root.get(Flight_.flightNumber),number +"%");
	}
	
	public static Specification<Flight> hasTakeOffTime(LocalDateTime time) {
		LocalDateTime startOfD = LocalDateTime.of(time.toLocalDate(),LocalTime.of(0, 0));
		return(root,cq,cb)->cb.between(root.get(Flight_.takeOffTime),startOfD,startOfD.plusDays(1));
	}
	
	public static Specification<Flight> hasTakeOffIATA(String iata) {
		return(root,cq,cb)->cb.like(root.get(Flight_.takeOff).get(Airport_.iata),iata + "%");
	}
	
}
