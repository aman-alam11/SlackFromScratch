package edu.northeastern.ccs.im.clientmenu.clientutils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.factories.ModuleFactory;
import edu.northeastern.ccs.im.clientmenu.userlevel.UnreadMessages;
import edu.northeastern.ccs.im.clientmenu.userlevel.UserSearchModelLayer;
import edu.northeastern.ccs.im.view.FrontEnd;

public final class InjectLevelUtil {

  private static InjectLevelUtil mInjectUtil;
  private static Map<Integer, Function<Scanner, CoreOperation>> mClientOptionsMap;
  private ModuleFactory moduleFactory;

  public static Map<Integer, Function<Scanner, CoreOperation>> getOptionsMap() {
    return Collections.unmodifiableMap(mClientOptionsMap);
  }

  private InjectLevelUtil() {
    moduleFactory = new ModuleFactory();
  }

  public static InjectLevelUtil getInstance() {
    if (mInjectUtil == null) {
      mInjectUtil = new InjectLevelUtil();
      mClientOptionsMap = new HashMap<>();
    }

    return mInjectUtil;
  }


  public void injectLevel(CurrentLevel currentLevel) {
    clearMap();

    // Update HashMap based on the levels

    switch (currentLevel) {
      case LOGIN_LEVEL:
        injectLoginLevel();
        break;

      case LEVEL1:
        injectFirstLevel();
        break;

      default:
        injectLoginLevel();
    }
    // Feed the appropriate options
  }

  private static void clearMap() {
    if (mClientOptionsMap == null) {
      mClientOptionsMap = new HashMap<>();
    }
    mClientOptionsMap.clear();
  }


  private void injectLoginLevel() {
    FrontEnd.getView().showMainMenu();
    mClientOptionsMap.put(1, scanner -> moduleFactory.getModelFromFactory(CurrentLevel.LOGIN_LEVEL));
    mClientOptionsMap.put(2, scanner -> moduleFactory.getModelFromFactory(CurrentLevel.REGISTRATION));
  }

  private void injectFirstLevel() {
    FrontEnd.getView().showUserLevelOptions();
    mClientOptionsMap.put(1, scanner -> new UnreadMessages());
    mClientOptionsMap.put(2, scanner -> new UserSearchModelLayer());
  }

}
