package edu.northeastern.ccs.im.clientmenu.clientutils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.firstlevel.AllGroupChats;
import edu.northeastern.ccs.im.clientmenu.firstlevel.AllUserChats;
import edu.northeastern.ccs.im.clientmenu.firstlevel.ChatNewUser;
import edu.northeastern.ccs.im.clientmenu.firstlevel.CreateGroup;
import edu.northeastern.ccs.im.clientmenu.secondlevel.ChatUser;
import edu.northeastern.ccs.im.clientmenu.thirdlevel.DoThirdLevelOperation;
import edu.northeastern.ccs.im.view.FrontEnd;

public final class InjectLevelUtil {

  private static InjectLevelUtil mInjectUtil;
  private static Map<Integer, Function<Scanner, CoreOperation>> mClientOptionsMap;

  public static Map<Integer, Function<Scanner, CoreOperation>> getOptionsMap() {
    return Collections.unmodifiableMap(mClientOptionsMap);
  }

  private InjectLevelUtil() {
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
        break;

      case LEVEL1:
        injectFirstLevel();
        break;

      case LEVEL2:
        injectSecondLevel();
        break;

      case LEVEL3:
        injectThirdLevel();
        break;

      default:
        injectFirstLevel();
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
  }

  private void injectFirstLevel() {
    FrontEnd.getView().showClientOptions();
    mClientOptionsMap.put(1, scanner -> new ChatNewUser());
    mClientOptionsMap.put(2, scanner -> new CreateGroup());
    mClientOptionsMap.put(3, scanner -> new AllUserChats());
    mClientOptionsMap.put(4, scanner -> new AllGroupChats());
  }

  private void injectSecondLevel() {
    mClientOptionsMap.put(1, scanner -> new ChatUser());
  }

  private void injectThirdLevel() {
    mClientOptionsMap.put(1, scanner -> new DoThirdLevelOperation());
  }


}
