package edu.northeastern.ccs.im.clientmenu.models;

import java.util.List;

public class UserSearch {

  private String mUsername;
  private List<String> usersList;

  public UserSearch(String username) {
    this.mUsername = username;
  }

  public String getUsername() {
    return mUsername;
  }

  public List<String> getListUserString(){
    return usersList;
  }

  public void setUsersList(List<String> usersList) {
    this.usersList = usersList;
  }
}
