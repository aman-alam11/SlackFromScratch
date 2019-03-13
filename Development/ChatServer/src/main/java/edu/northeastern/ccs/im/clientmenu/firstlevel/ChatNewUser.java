package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;

public class ChatNewUser extends CommonOperations {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, Connection connectionModelLayer) {
    this.mScanner = scanner;
    System.out.println("Passed Control to Chat New User Level 1");
  }
}
