package edu.northeastern.ccs.im.client;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.client.clientinterfaces.ParentModel;

public class UnreadMessages implements CoreOperation {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, ParentModel model) {
    this.mScanner = scanner;
  }
}
