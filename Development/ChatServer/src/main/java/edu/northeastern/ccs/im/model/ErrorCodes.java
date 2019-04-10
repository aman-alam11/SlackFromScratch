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
  G809("Requester Is Not a Member Of Group"),
  DB000("Could not persist"),
  DB001("All messages seen by user"),
  IL001("Not a super user. Illegal Access");


  private String errorMessage;

  private ErrorCodes(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return this.errorMessage;
  }

}
