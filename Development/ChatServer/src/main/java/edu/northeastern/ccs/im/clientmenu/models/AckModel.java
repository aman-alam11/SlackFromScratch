package edu.northeastern.ccs.im.clientmenu.models;

public class AckModel {

  private String errorMessage;
  private boolean isUserAuthenticated = false;

  public AckModel(boolean isUserAuth, String errorMsg) {
    this.isUserAuthenticated = isUserAuth;
    this.errorMessage = errorMsg;
  }

  public boolean isUserAuthenticated() {
    return isUserAuthenticated;
  }

  public String getErrorMessage() {
    return errorMessage;
  }


}