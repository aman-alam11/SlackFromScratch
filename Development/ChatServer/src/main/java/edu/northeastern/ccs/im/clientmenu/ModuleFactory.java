package edu.northeastern.ccs.im.clientmenu;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.firstlevel.FirstLevelModel;
import edu.northeastern.ccs.im.clientmenu.login.Login;
import edu.northeastern.ccs.im.clientmenu.login.Registration;

public class ModuleFactory {


  public CoreOperation getModelFromFactory(CurrentLevel currentLevel) {
    switch (currentLevel) {

      case LOGIN_LEVEL:
        return new Login();

      case REGISTRATION:
        return new Registration();

      case LEVEL1:
        return new FirstLevelModel();
//        return new FirstLevelModel(ClientConstants.URL, ClientConstants.PORT);


      default:
        // This will return first level module when implemented as default
        return new DefaultOperation();
    }
  }
}
