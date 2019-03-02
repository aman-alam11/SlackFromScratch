package edu.northeastern.ccs.im.client;

import edu.northeastern.ccs.im.client.FirstLevel.FirstLevelModel;
import edu.northeastern.ccs.im.client.SecondLevel.SecondLevelModel;

public class ModuleFactory {

  public ParentModel getModelFromFactory(CurrentLevel currentLevel) {
    switch (currentLevel) {
      case LEVEL1:
        return new FirstLevelModel();

      case LEVEL2:
        return new SecondLevelModel();

      case LEVEL3:
        return new FirstLevelModel();

      default:
        return new FirstLevelModel();
    }
  }
}
