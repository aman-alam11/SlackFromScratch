package edu.northeastern.ccs.im.client;

import edu.northeastern.ccs.im.client.firstlevel.FirstLevelModel;
import edu.northeastern.ccs.im.client.clientinterfaces.ParentModel;
import edu.northeastern.ccs.im.client.clientutils.CurrentLevel;

public class ModuleFactory {


  public ParentModel getModelFromFactory(CurrentLevel currentLevel) {
    switch (currentLevel) {

      case LEVEL1:
        return new FirstLevelModel();

      case LEVEL2:
        // This will return second level module when implemented
        return new FirstLevelModel();

      case LEVEL3:
        // This will return third level module when implemented
        return new FirstLevelModel();


      default:
        // This will return first level module when implemented as default
        return new FirstLevelModel();
    }
  }
}
