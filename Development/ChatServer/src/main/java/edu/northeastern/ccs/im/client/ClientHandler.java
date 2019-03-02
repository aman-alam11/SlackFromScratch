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

  private ParentModel modelLayer;


  public ClientHandler(ParentModel model) {
    modelLayer = model;
    if (modelLayer == null) {
      throw new IllegalArgumentException("Model can't be null");
    }

    mClientMap = new HashMap<>(INITIAL_CLIENT_QUOTA);
  }


  public void initClientOperations() {

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNext()) {
      int userChoice = 0;
      try {
        userChoice = Integer.parseInt(scanner.next().toLowerCase().trim());
        if (userChoice == USER_INPUT_QUIT) {
          return;
        }
      } catch (Exception e) {
        // Handle with default implementation
        FrontEnd.getView().sendToView("Wrong input, try again.");
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
