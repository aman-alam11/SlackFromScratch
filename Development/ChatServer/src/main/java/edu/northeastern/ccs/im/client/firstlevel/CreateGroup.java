package edu.northeastern.ccs.im.client.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.client.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.client.clientinterfaces.ParentModel;

public class CreateGroup extends CommonOperations {


  private Scanner mScanner;

  /**
   * This is where the control is passed to when a create group operation is performed.
   */
  @Override
  public void passControl(Scanner scanner, ParentModel model) {
    this.mScanner = scanner;
  }
}
