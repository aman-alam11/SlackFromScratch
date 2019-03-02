package edu.northeastern.ccs.im.client.ThirdLevelModules;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.CurrentLevel;
import edu.northeastern.ccs.im.client.ModuleFactory;
import edu.northeastern.ccs.im.client.ParentModel;
import edu.northeastern.ccs.im.client.SharedUtil;

public class ThirdLevelOperations implements ThirdLevelModule {

  private Scanner mScanner;
  private ParentModel model;

  public ThirdLevelOperations(Scanner sc) {
    this.mScanner = sc;
  }

  @Override
  public void doThirdLevelOperations() {
    System.out.println("Inside Third Level");
  }

  @Override
  public void passControl(CurrentLevel currentLevel, SharedUtil sharedProperty) {
    model = new ModuleFactory().getModelFromFactory(currentLevel);
  }
}
