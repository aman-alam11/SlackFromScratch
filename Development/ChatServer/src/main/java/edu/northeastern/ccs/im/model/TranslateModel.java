package edu.northeastern.ccs.im.model;

public class TranslateModel {



  private String message;

  public String getFromUser() {
    return fromUser;
  }

  public void setFromUser(String fromUser) {
    this.fromUser = fromUser;
  }

  public String getToUser() {
    return toUser;
  }

  public void setToUser(String toUser) {
    this.toUser = toUser;
  }

  private String fromUser;
  private String toUser;

  public TranslateModel(String message, String toLanguage) {
    this.message = message;
    this.toLanguage = toLanguage;
  }


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getToLanguage() {
    return toLanguage;
  }

  public void setToLanguage(String toLanguage) {
    this.toLanguage = toLanguage;
  }

  private String toLanguage;

}
