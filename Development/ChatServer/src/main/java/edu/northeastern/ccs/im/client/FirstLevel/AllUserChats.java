package edu.northeastern.ccs.im.client.FirstLevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.CoreOperation;
import edu.northeastern.ccs.im.client.CurrentLevel;
import edu.northeastern.ccs.im.client.ParentModel;
import edu.northeastern.ccs.im.client.SharedUtil;

public class AllUserChats implements FirstLevelModule {


  private Scanner scanner;

  public AllUserChats(Scanner scanner) {
    this.scanner = scanner;
  }

  @Override
  public void doFirstLevelOperations() {

  }

  @Override
  public void passControl(CurrentLevel currentLevel, SharedUtil sharedProperty) {

  }
}
