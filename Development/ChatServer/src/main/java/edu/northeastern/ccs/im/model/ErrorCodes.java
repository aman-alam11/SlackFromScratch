package edu.northeastern.ccs.im.model;

public enum ErrorCodes {
	
	G801("Group Name Already Exists"),
	G802("User Already In Group"),
	G803("Group Name Does Not Exists"),
	G804("No Users Provided"),
	G805("Some Users Are Invalid"),
	G806("Some Users Already Exists In Group"),
	G807("All Users Exists In Group"),
	G808("All Users Invalid"),
	DB000("Could not persist");
	
	private String errorMessage;
	private ErrorCodes(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() {
		return this.errorMessage;
	}

}
