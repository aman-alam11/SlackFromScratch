package edu.northeastern.ccs.im.model;

public class RecallModel {

  private String toUser;
  private int num;

  public RecallModel(String toUser, int num) {
    this.toUser = toUser;
    this.num = num;
  }

  public String getToUser() {
    return toUser;
  }

  public int getNum() {
    return num;
  }
}
