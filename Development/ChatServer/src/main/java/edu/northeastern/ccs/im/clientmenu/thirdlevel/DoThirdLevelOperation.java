package edu.northeastern.ccs.im.clientmenu.thirdlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.ParentModel;

public class DoThirdLevelOperation extends CommonOperations {


  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, ParentModel model) {
    this.mScanner = scanner;
    System.out.println("Passed Control to Level 3");
  }
}
