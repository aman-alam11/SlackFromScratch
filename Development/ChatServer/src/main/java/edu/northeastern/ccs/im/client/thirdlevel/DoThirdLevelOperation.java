package edu.northeastern.ccs.im.client.thirdlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.client.clientinterfaces.ParentModel;

public class DoThirdLevelOperation extends CommonOperations {


  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, ParentModel model) {
    this.mScanner = scanner;
    System.out.println("Passed Control to Level 3");
  }
}
