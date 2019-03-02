package edu.northeastern.ccs.im.client.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.client.clientinterfaces.ParentModel;

public class ChatNewUser extends CommonOperations {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, ParentModel model) {
    this.mScanner = scanner;
    System.out.println("Passed Control to Chat New User Level 1");
  }
}
