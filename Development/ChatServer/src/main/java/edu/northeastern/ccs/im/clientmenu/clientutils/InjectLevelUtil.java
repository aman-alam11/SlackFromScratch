package edu.northeastern.ccs.im.clientmenu.clientutils;

import org.omg.CORBA.MARSHAL;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Function;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.factories.ModuleFactory;
import edu.northeastern.ccs.im.clientmenu.grouplevel.AddUserToGroup;
import edu.northeastern.ccs.im.clientmenu.grouplevel.CreateGroup;
import edu.northeastern.ccs.im.clientmenu.grouplevel.DeleteUsersFromGroup;
import edu.northeastern.ccs.im.clientmenu.grouplevel.GroupChat;
import edu.northeastern.ccs.im.clientmenu.grouplevel.GroupLayer;
import edu.northeastern.ccs.im.clientmenu.grouplevel.UpdateGroup;
import edu.northeastern.ccs.im.clientmenu.userlevel.UnreadMessages;
import edu.northeastern.ccs.im.clientmenu.userlevel.UserSearchModelLayer;
import edu.northeastern.ccs.im.view.FrontEnd;

public final class InjectLevelUtil {

  private static InjectLevelUtil mInjectUtil;
  private static Map<Integer, Function<Scanner, CoreOperation>> mClientOptionsMap;
  private ModuleFactory moduleFactory;
  private static Map<CurrentLevel,CurrentLevel> levelMap;
  private static CurrentLevel currentLevel;


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
    levelMap.put(CurrentLevel.LEVEL1,CurrentLevel.LEVEL1);
    levelMap.put(CurrentLevel.GROUP_LEVEL,CurrentLevel.LEVEL1);
    levelMap.put(CurrentLevel.GROUP_USERS_CRUD_LEVEL,CurrentLevel.GROUP_LEVEL);
  }

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

      case LEVEL1:
        setCurrentLevel(CurrentLevel.LEVEL1);
        injectFirstLevel();
        break;

      case GROUP_LEVEL:
        setCurrentLevel(CurrentLevel.GROUP_LEVEL);
        injectGroupLevel();
        break;

      case GROUP_USERS_CRUD_LEVEL:
        setCurrentLevel(CurrentLevel.GROUP_USERS_CRUD_LEVEL);
        injectGroupUsersCrudLevel();
        break;

      default:
        setCurrentLevel(CurrentLevel.LOGIN_LEVEL);
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
    mClientOptionsMap.put(3, scanner -> new GroupLayer());
  }


  private void injectGroupLevel() {
    FrontEnd.getView().showGroupLevelOptions();
    mClientOptionsMap.put(1, scanner -> new CreateGroup());
    mClientOptionsMap.put(2, scanner -> new UpdateGroup());
    mClientOptionsMap.put(3, scanner -> new GroupChat());
  }

  private void injectGroupUsersCrudLevel() {
    FrontEnd.getView().showGroupUsersCrudLevelOptions();
    mClientOptionsMap.put(1, scanner -> new AddUserToGroup());
    mClientOptionsMap.put(2, scanner -> new DeleteUsersFromGroup());
  }

}
