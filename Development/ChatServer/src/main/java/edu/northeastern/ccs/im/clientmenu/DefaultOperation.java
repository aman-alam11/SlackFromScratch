package edu.northeastern.ccs.im.clientmenu;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;

public class DefaultOperation implements CoreOperation {


  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, Connection model) {
    this.mScanner = scanner;
  }
}
