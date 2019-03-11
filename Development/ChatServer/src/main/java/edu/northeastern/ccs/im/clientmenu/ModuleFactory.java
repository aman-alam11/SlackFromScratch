package edu.northeastern.ccs.im.clientmenu;

import edu.northeastern.ccs.im.clientmenu.firstlevel.FirstLevelModel;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;

public class ModuleFactory {


  public Connection getModelFromFactory(CurrentLevel currentLevel) {
    switch (currentLevel) {

      case LEVEL1:
        return new FirstLevelModel(ClientConstants.URL, ClientConstants.PORT);

      case LEVEL2:
        // This will return second level module when implemented
        return new FirstLevelModel(ClientConstants.URL, ClientConstants.PORT);

      case LEVEL3:
        // This will return third level module when implemented
        return new FirstLevelModel(ClientConstants.URL, ClientConstants.PORT);


      default:
        // This will return first level module when implemented as default
        return new FirstLevelModel(ClientConstants.URL, ClientConstants.PORT);
    }
  }
}
