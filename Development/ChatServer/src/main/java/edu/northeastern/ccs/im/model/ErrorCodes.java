package edu.northeastern.ccs.im.model;

public enum ErrorCodes {
	
	G801("Group Name Already Exists"),
	G802("User Already In Group"),
	DB000("Could not persist");
	
	private String errorMessage;
	private ErrorCodes(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() {
		return this.errorMessage;
	}

}
