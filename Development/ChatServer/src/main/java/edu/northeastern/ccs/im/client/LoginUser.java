package edu.northeastern.ccs.im.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import edu.northeastern.ccs.im.view.FrontEnd;

public class LoginUser implements CoreOperation {

  private Scanner scanner;
  private TopModelLayer modelLayer;
  private Map<Integer, Function<Scanner, CoreOperation>> mLoginMap;

  private static final int USER_INPUT_UNREAD_MESSAGES = 1;
  private static final int USER_INPUT_CREATE_GROUP = 2;
  private static final int USER_INPUT_CHAT = 3;

  public LoginUser(Scanner scanner) {
    this.scanner = scanner;
  }

  @Override
  public void passControl(TopModelLayer userModelTopLayer) {

    modelLayer = userModelTopLayer;
    if (modelLayer == null) {
      throw new IllegalArgumentException("Model can't be null");
    }

    // A default initial size
    mLoginMap = new HashMap<>();
    OptionsFactory optionsFactory = new OptionsFactoryImpl();

    // Add all operations of same level here below this
    mLoginMap.put(USER_INPUT_UNREAD_MESSAGES, optionsFactory::getUnreadMessages);
    mLoginMap.put(USER_INPUT_CREATE_GROUP, optionsFactory::getCreateGroup);
    mLoginMap.put(USER_INPUT_CHAT, optionsFactory::getUserChat);
    initLogin();
  }

  private void initLogin() {

    String username = "";
    String password = "";

    FrontEnd frontEnd = FrontEnd.getView();
    frontEnd.sendToView("Login: ");


    try {
      frontEnd.sendToView("Enter Username: ");
      username = scanner.next();
      frontEnd.sendToView("Enter Password: ");
      password = scanner.next();
    }
    catch (Exception e) {
      frontEnd.sendToView("Not allowed");
    }

    if (doAuth(username, password)) {
      frontEnd.sendToView("Welcome " + username + "!");
      frontEnd.sendToView("1. Unread Messages");
      frontEnd.sendToView("2. Create Group");
      frontEnd.sendToView("3. Chat");
      frontEnd.sendToView("Enter From above Options: ");

      while (scanner.hasNext()) {
        int userChoice = 0;
        try {
          userChoice = Integer.parseInt(scanner.next().toLowerCase().trim());
        } catch (Exception e) {
          // Handle with default implementation
          frontEnd.sendToView(  " Wrong input, try again.");
        }

        CoreOperation initialCoreOperation;
        Function<Scanner, CoreOperation> mTransformationFunction = mLoginMap.getOrDefault(userChoice,
                null);

        if (mTransformationFunction == null) {
          // Handle with some default operation
          new DefaultOperation().passControl(modelLayer);
        } else {
          // Apply Transformation
          initialCoreOperation = mTransformationFunction.apply(scanner);
          initialCoreOperation.passControl(modelLayer);
        }
      }
    }

    else {
      return;
    }
  }

  private boolean doAuth(String username, String password) {
    return false;
  }
}
