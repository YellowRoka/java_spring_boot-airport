package hu.webuni.airport.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.springframework.data.annotation.Id;

@Entity
public class D extends C{

	protected String d;
	
	
	
}
