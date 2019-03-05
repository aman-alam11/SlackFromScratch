package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.ParentModel;

public class GroupChat extends CommonOperations {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, ParentModel model) {
    this.mScanner = scanner;
    System.out.println("chat group");
  }
}
