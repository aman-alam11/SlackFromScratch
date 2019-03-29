package edu.northeastern.ccs.im.model;

import java.util.ArrayList;
import java.util.List;

public class GroupSearchModel {

  private String toChatWithGroupName;
  private List<String> groupList;

  public GroupSearchModel(String toChatWithGroupName) {
    this.toChatWithGroupName = toChatWithGroupName;
    groupList = new ArrayList<>();
  }

  public String getToChatWithGroupName() {
    return toChatWithGroupName;
  }

  public List<String> getListGroupString(){
    return groupList;
  }

  public void setGroupList(List<String> groupList) {
    this.groupList = groupList;
  }
}
