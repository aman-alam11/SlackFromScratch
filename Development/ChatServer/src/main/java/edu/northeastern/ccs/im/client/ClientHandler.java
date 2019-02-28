package edu.northeastern.ccs.im.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public final class ClientHandler {

  private static final int INITIAL_CLIENT_QUOTA = 8;
  private static final int USER_INPUT_CREATE_GROUP = 1;

  private Map<Integer, Function<Scanner, CoreOperation>> mClientMap;

  private TopModelLayer modelLayer;


  public ClientHandler(TopModelLayer model) {
    modelLayer = model;
    if (modelLayer == null) {
      throw new IllegalArgumentException("Model can't be null");
    }

    // A default initial size
    mClientMap = new HashMap<>(INITIAL_CLIENT_QUOTA);

    // Add all operations of same level here below this
    mClientMap.put(USER_INPUT_CREATE_GROUP, scanner -> new CreateGroup());
  }


  public void initClientOperations() {

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNext()) {
      int userChoice;
      try {
        userChoice = Integer.parseInt(scanner.next().toLowerCase().trim());
      } catch (Exception e) {
        // Handle with default implementation
        return;
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
