package edu.northeastern.ccs.im.model;

import java.util.ArrayList;
import java.util.List;

public class AckModel {
	
	private StringBuilder errorMessage;
	private List<ErrorCodes> errorCodesList;
	private boolean isUserAuthenticated = false;
	private boolean isLogin;
	
	public AckModel() {
		errorCodesList = new ArrayList<>();
	}
	
	public AckModel(boolean isUserAuth, String errorMsg, boolean isLogin) {
		this.isUserAuthenticated = isUserAuth;
		this.errorMessage = new StringBuilder(errorMsg);
		this.isLogin = isLogin;
		errorCodesList = new ArrayList<>();
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
}