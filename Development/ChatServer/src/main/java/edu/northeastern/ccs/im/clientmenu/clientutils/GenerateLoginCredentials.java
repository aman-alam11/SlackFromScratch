package edu.northeastern.ccs.im.clientmenu.clientutils;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.LoginCredentials;

public class GenerateLoginCredentials {

  public MessageJson generateLoginCredentials(String username, String password, MessageType messageType) {
    LoginCredentials loginCredentials = new LoginCredentials(username, password);
    String jsonLoginCredentials = new Gson().toJson(loginCredentials);
    return new MessageJson(username, messageType, jsonLoginCredentials);
  }
}
