package edu.northeastern.ccs.im.clientmenu.models;

import java.util.List;

public class Search {

  private String searchName;
  private List<String> list;

  public Search(String searchName) {
    this.searchName = searchName;
  }

  public String getUsername() {
    return searchName;
  }

  public List<String> getListString(){
    return list;
  }

  public void setList(List<String> list) {
    this.list = list;
  }
}
