package hu.webuni.airport.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import javax.persistence.Id;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)//TABLE_PER_CLASS)//.JOINED)//SINGLE_TABLE)
public class A {

	@Id
	@GeneratedValue
	protected long a1;
	protected String a2;
	
	
}
