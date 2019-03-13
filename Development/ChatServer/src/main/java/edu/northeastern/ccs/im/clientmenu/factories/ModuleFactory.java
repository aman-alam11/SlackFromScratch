package edu.northeastern.ccs.im.clientmenu.factories;

import edu.northeastern.ccs.im.clientmenu.DefaultOperation;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.loginlevel.Login;
import edu.northeastern.ccs.im.clientmenu.loginlevel.Registration;

public class ModuleFactory {


  public CoreOperation getModelFromFactory(CurrentLevel currentLevel) {
    switch (currentLevel) {

      case LOGIN_LEVEL:
        return new Login();

      case REGISTRATION:
        return new Registration();

//      case LEVEL1:
//        return new FirstLevelModel();


      default:
        // This will return first level module when implemented as default
        return new DefaultOperation();
    }
  }
}
