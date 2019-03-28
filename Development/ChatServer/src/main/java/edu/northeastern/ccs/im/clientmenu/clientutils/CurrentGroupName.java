package edu.northeastern.ccs.im.clientmenu.clientutils;

public class CurrentGroupName {

  private CurrentGroupName() {
    //private constructor
  }

  public static String getGroupName(){
    return (groupName == null) ? "" : groupName;
  }

  public static void setGroupName(String groupName1) {
    groupName = groupName1;
  }

  private static  String groupName;
}
