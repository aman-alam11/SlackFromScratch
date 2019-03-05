package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.ParentModel;

public class CreateGroup extends CommonOperations {


  private Scanner mScanner;

  /**
   * This is where the control is passed to when a create group operation is performed.
   */
  @Override
  public void passControl(Scanner scanner, ParentModel model) {
    this.mScanner = scanner;
    System.out.println("create group");
  }
}
