package edu.northeastern.ccs.im.model;

public class AckModel {
	
	private String errorMessage;
	private ErrorCodes errorCode;
	private boolean isUserAuthenticated = false;
	private boolean isLogin;
	public AckModel() {
		
	}
	public AckModel(boolean isUserAuth, String errorMsg, boolean isLogin) {
		this.isUserAuthenticated = isUserAuth;
		this.errorMessage = errorMsg;
		this.isLogin = isLogin;
	}
	
	public boolean isUserAuthenticated(){
		return isUserAuthenticated;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}


	public boolean isLogin() {
		return isLogin;
	}
	
	public ErrorCodes getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodes errorCode) {
		this.errorCode = errorCode;
	}
}