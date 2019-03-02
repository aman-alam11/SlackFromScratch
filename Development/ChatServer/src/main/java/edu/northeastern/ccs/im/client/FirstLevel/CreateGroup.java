package edu.northeastern.ccs.im.client.FirstLevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.CurrentLevel;
import edu.northeastern.ccs.im.client.SharedUtil;

public class CreateGroup implements FirstLevelModule {

  private Scanner scanner;

  public CreateGroup(Scanner scanner) {
    this.scanner = scanner;
  }


  @Override
  public void doFirstLevelOperations() {

  }


  /**
   * This is where the control is passed to when a create group operation is performed.
   *
   */
  @Override
  public void passControl(CurrentLevel currentLevel, SharedUtil sharedProperty) {

  }
}
