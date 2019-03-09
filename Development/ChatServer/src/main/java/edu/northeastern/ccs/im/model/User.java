package edu.northeastern.ccs.im.model;

import java.io.Serializable;

public class User implements Serializable{
	private String userName;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	
	public User(String userName, String password, String email) {
		this.userName = userName;
		this.password = password;
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
