package edu.northeastern.ccs.im.clientmenu;

import java.util.Scanner;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.ParentModel;

public class UnreadMessages implements CoreOperation {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, ParentModel model) {
    this.mScanner = scanner;
  }
}
