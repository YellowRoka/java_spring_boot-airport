package hu.webuni.airport.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.airport.model.LogEntry;
import hu.webuni.airport.repository.LogEntryRepository;

@Service
public class LogEntryService {

	@Autowired
	LogEntryRepository logEntryRepository;
	
	public void createLog(String description){
		callBackendSystem();
		logEntryRepository.save(new LogEntry(description));
	}
	
	private void callBackendSystem() {
		if(new Random().nextInt(3) == 1) {
			throw new RuntimeException();
		}
	}
	
}
