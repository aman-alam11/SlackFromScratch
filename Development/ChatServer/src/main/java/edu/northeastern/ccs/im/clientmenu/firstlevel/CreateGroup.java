package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;

public class CreateGroup extends CommonOperations {


  private Scanner mScanner;

  /**
   * This is where the control is passed to when a create group operation is performed.
   */
  @Override
  public void passControl(Scanner scanner, Connection model) {
    this.mScanner = scanner;
    System.out.println("create group");
  }
}
