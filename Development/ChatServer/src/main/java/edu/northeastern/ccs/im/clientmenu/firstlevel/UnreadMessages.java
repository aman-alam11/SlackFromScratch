package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UnreadMessages implements CoreOperation {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, Connection connectionModelLayer) {
    this.mScanner = scanner;

    System.out.println("Unread messages");
  }
}
