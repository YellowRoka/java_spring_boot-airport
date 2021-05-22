package hu.webuni.airport.dto;

import java.time.LocalDateTime;

public class LoginDto {

	private long id;
	
	private LocalDateTime ts;
	private String description;
	private String username;
	private String password;
	
	public LoginDto() {}
	
	public LoginDto(String description, String username) {
		//super();
		this.description = description;
		this.username = username;
		this.ts = LocalDateTime.now();
	}



	public long getId() {
		return id;
	}

	public LocalDateTime getTs() {
		return ts;
	}
	public void setTs(LocalDateTime ts) {
		this.ts = ts;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
