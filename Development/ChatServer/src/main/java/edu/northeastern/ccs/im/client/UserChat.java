package edu.northeastern.ccs.im.client;

import java.util.Scanner;

public class UserChat implements CoreOperation {


  private Scanner scanner;

  public UserChat(Scanner scanner) {
    this.scanner = scanner;
  }

  @Override
  public void passControl(TopModelLayer userModelTopLayer) {

  }
}
