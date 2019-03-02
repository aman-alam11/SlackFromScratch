package edu.northeastern.ccs.im.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import edu.northeastern.ccs.im.view.FrontEnd;

public final class ClientHandler {

  private static final int INITIAL_CLIENT_QUOTA = 8;
  private static final int USER_INPUT_LOGIN = 1;
  private static final int USER_INPUT_REGISTRATION = 2;
  private static final int USER_INPUT_QUIT = 3;

  private Map<Integer, Function<Scanner, CoreOperation>> mClientMap;

  private TopModelLayer modelLayer;


  public ClientHandler(TopModelLayer model) {
    modelLayer = model;
    if (modelLayer == null) {
      throw new IllegalArgumentException("Model can't be null");
    }

    // A default initial size
    mClientMap = new HashMap<>(INITIAL_CLIENT_QUOTA);
    OptionsFactory optionsFactory = new OptionsFactoryImpl();

    // Add all operations of same level here below this
    mClientMap.put(USER_INPUT_LOGIN, optionsFactory::getLoginUser);
    mClientMap.put(USER_INPUT_REGISTRATION, optionsFactory::getRegisterUser);
  }


  public void initClientOperations() {

    Scanner scanner = new Scanner(System.in);
    FrontEnd frontEnd = FrontEnd.getView();
    frontEnd.sendToView("Welcome to Chatter Application");
    frontEnd.sendToView("1. Login");
    frontEnd.sendToView("2. Registration");
    frontEnd.sendToView("3. Quit");
    frontEnd.sendToView("Enter From above Options: ");

    while (scanner.hasNext()) {
      int userChoice = 0;
      try {
        userChoice = Integer.parseInt(scanner.next().toLowerCase().trim());
        if (userChoice == USER_INPUT_QUIT) {
          return;
        }
      } catch (Exception e) {
        // Handle with default implementation
        frontEnd.sendToView(  "Wrong input, try again.");
      }

      CoreOperation initialCoreOperation;
      Function<Scanner, CoreOperation> mTransformationFunction = mClientMap.getOrDefault(userChoice,
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
}
