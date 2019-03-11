package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;

public class UnreadMessages implements CoreOperation {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, Connection model) {
    this.mScanner = scanner;

    System.out.println("Unread messages");
  }
}
