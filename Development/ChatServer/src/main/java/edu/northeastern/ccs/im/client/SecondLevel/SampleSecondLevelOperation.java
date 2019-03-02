package edu.northeastern.ccs.im.client.SecondLevel;

import edu.northeastern.ccs.im.client.CoreOperation;
import edu.northeastern.ccs.im.client.CurrentLevel;
import edu.northeastern.ccs.im.client.ModuleFactory;
import edu.northeastern.ccs.im.client.SharedUtil;

public class SampleSecondLevelOperation implements CoreOperation {

  private SecondLevelModel model;

  @Override
  public void passControl(CurrentLevel currentLevel, SharedUtil sharedProperty) {
    model = (SecondLevelModel) new ModuleFactory().getModelFromFactory(currentLevel);

    // Do this based on the operation
    new UserSecondLevel().doSecondLevelOperations(model);
  }
}