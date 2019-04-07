package edu.northeastern.ccs.im.clientmenu.clientutils;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.factories.ModuleFactory;
import edu.northeastern.ccs.im.clientmenu.grouplevel.AddModeratorGroup;
import edu.northeastern.ccs.im.clientmenu.grouplevel.AddUserToGroupModelLayer;
import edu.northeastern.ccs.im.clientmenu.grouplevel.CreateGroupModelLayer;
import edu.northeastern.ccs.im.clientmenu.grouplevel.DeleteGroupModelLayer;
import edu.northeastern.ccs.im.clientmenu.grouplevel.DeleteModeratorGroup;
import edu.northeastern.ccs.im.clientmenu.grouplevel.DeleteUsersFromGroup;
import edu.northeastern.ccs.im.clientmenu.grouplevel.GroupLayer;
import edu.northeastern.ccs.im.clientmenu.grouplevel.GroupSearchModelLayer;
import edu.northeastern.ccs.im.clientmenu.grouplevel.UpdateGroupModelLayer;
import edu.northeastern.ccs.im.clientmenu.superuser.SuperUser;
import edu.northeastern.ccs.im.clientmenu.userlevel.UnreadMessages;
import edu.northeastern.ccs.im.clientmenu.userlevel.UserSearchModelLayer;
import edu.northeastern.ccs.im.view.FrontEnd;

public final class InjectLevelUtil {

  private static InjectLevelUtil mInjectUtil;
  private static Map<Integer, Function<Scanner, CoreOperation>> mClientOptionsMap;
  private ModuleFactory moduleFactory;
  private static Map<CurrentLevel,CurrentLevel> levelMap;
  private static CurrentLevel currentLevel;
  private boolean isSuperUser;
  private int maxCount = -1;


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
      levelMap = new EnumMap<>(CurrentLevel.class);
      initializeBackMenuOptions();
    }

    return mInjectUtil;
  }

  public  Map<CurrentLevel, CurrentLevel> getLevelMap() {
    return levelMap;
  }

  private static void initializeBackMenuOptions() {
    levelMap.put(CurrentLevel.LOGIN_LEVEL,CurrentLevel.LOGIN_LEVEL);
    levelMap.put(CurrentLevel.USER_LEVEL,CurrentLevel.USER_LEVEL);
    levelMap.put(CurrentLevel.GROUP_LEVEL,CurrentLevel.USER_LEVEL);
    levelMap.put(CurrentLevel.GROUP_USERS_CRUD_LEVEL,CurrentLevel.GROUP_LEVEL);
  }

  /**
   * To keep track of the current level for go back function.
   * @return Current level of the interface.
   */
  public CurrentLevel getCurrentLevel() {
    return currentLevel;
  }

  private static void setCurrentLevel(CurrentLevel current) {
    currentLevel = current;
  }


  public void injectLevel(CurrentLevel currentLevel) {
    clearMap();
    // Update HashMap based on the levels

    switch (currentLevel) {
      case LOGIN_LEVEL:
        setCurrentLevel(CurrentLevel.LOGIN_LEVEL);
        injectLoginLevel();
        break;

      case USER_LEVEL:
        setCurrentLevel(CurrentLevel.USER_LEVEL);
        injectUserLevel();
        break;

      case GROUP_LEVEL:
        setCurrentLevel(CurrentLevel.GROUP_LEVEL);
        injectGroupLevel();
        break;

      case GROUP_USERS_CRUD_LEVEL:
        setCurrentLevel(CurrentLevel.GROUP_USERS_CRUD_LEVEL);
        injectGroupUsersCrudLevel();
        break;


      case GROUP_USERS_MODERATOR_OPERATIONS:
        setCurrentLevel(CurrentLevel.GROUP_USERS_MODERATOR_OPERATIONS);
        injectUpdateModeratorsLevel();
        break;

      case DEFAULT_LEVEL:
        injectDefaultLevel();
        break;

      default:
        injectLoginLevel();
        setCurrentLevel(CurrentLevel.LOGIN_LEVEL);
    }
    // Feed the appropriate options
  }

  private void injectDefaultLevel() {
    FrontEnd.getView().showDefaultErrorMessage();
    injectUserLevel();
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

  private void injectUserLevel() {
    FrontEnd.getView().showUserLevelOptions();
    mClientOptionsMap.put(1, scanner -> new UnreadMessages());
    mClientOptionsMap.put(2, scanner -> new UserSearchModelLayer());
    mClientOptionsMap.put(3, scanner -> new GroupLayer());

    if (isSuperUser) {
      mClientOptionsMap.put(99, scanner -> new SuperUser());
      FrontEnd.getView().sendToView("\n \nPress 99 Super User Options: Tap into conversations\n \n");
    }
  }


  private void injectGroupLevel() {
    FrontEnd.getView().showGroupLevelOptions();
    mClientOptionsMap.put(1, scanner -> new CreateGroupModelLayer());
    mClientOptionsMap.put(2, scanner -> new UpdateGroupModelLayer());
    mClientOptionsMap.put(3, scanner -> new GroupSearchModelLayer());
    mClientOptionsMap.put(4, scanner -> new DeleteGroupModelLayer());
  }

  private void injectGroupUsersCrudLevel() {
    FrontEnd.getView().showGroupUsersCrudLevelOptions();
    mClientOptionsMap.put(1, scanner -> new AddUserToGroupModelLayer());
    mClientOptionsMap.put(2, scanner -> new DeleteUsersFromGroup());
  }

  private void injectUpdateModeratorsLevel() {
    FrontEnd.getView().showModeratorsOptions();
    mClientOptionsMap.put(1, scanner -> new AddModeratorGroup());
    mClientOptionsMap.put(2, scanner -> new DeleteModeratorGroup());
  }

  public void setSuperUser(boolean superUser) {
    // Allow Update only once during program
    if(maxCount < 0){
      maxCount++;
      isSuperUser = superUser;
    }
  }
}
