package edu.northeastern.ccs.im.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A model to map the user response to Login/Registration related operations.
 */
public class AckModel {
	
	private StringBuilder errorMessage;
	private List<ErrorCodes> errorCodesList;
	private boolean isUserAuthenticated = false;
	private boolean isLogin;
	// A field to enable user related operations
	private boolean isSuperUser;
	
	public AckModel() {
		errorCodesList = new ArrayList<>();
		this.errorMessage = new StringBuilder();
	}
	
	public AckModel(boolean isUserAuth, String errorMsg, boolean isLogin) {
		this.isUserAuthenticated = isUserAuth;
		this.errorMessage = new StringBuilder(errorMsg);
		this.isLogin = isLogin;
		errorCodesList = new ArrayList<>();
		// isSuperUser is false by default
		isSuperUser = false;
	}

	public AckModel(boolean isUserAuth, String errorMsg, boolean isLogin, boolean isSuperUser) {
		this.isUserAuthenticated = isUserAuth;
		this.errorMessage = new StringBuilder(errorMsg);
		this.isLogin = isLogin;
		errorCodesList = new ArrayList<>();
		this.isSuperUser = isSuperUser;
	}
	
	public boolean isUserAuthenticated(){
		return isUserAuthenticated;
	}
	
	public String getErrorMessage() {
		return errorMessage.toString();
	}


	public boolean isLogin() {
		return isLogin;
	}
	
	public List<ErrorCodes> getErrorCodeList() {
		return errorCodesList;
	}

	public void addErrorCode(ErrorCodes errorCode) {
		this.errorCodesList.add(errorCode);
	}
	
	public void appendErrorMessage(String msg) {
		this.errorMessage.append(" ").append(msg);
	}

	public Boolean getIsSuperUser() {
		return isSuperUser;
	}
}