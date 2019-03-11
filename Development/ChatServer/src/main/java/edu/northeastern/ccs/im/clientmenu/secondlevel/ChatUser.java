package edu.northeastern.ccs.im.clientmenu.secondlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;

public class ChatUser extends CommonOperations {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, Connection model) {
    this.mScanner = scanner;
    System.out.println("Passed Control to Chat User Level 2");
  }
}
