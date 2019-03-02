package edu.northeastern.ccs.im.client;

import java.util.Scanner;
import java.util.function.Function;

import edu.northeastern.ccs.im.client.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.client.clientinterfaces.ParentModel;
import edu.northeastern.ccs.im.client.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.client.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.view.FrontEnd;

public final class ClientHandler {

  private static final int USER_INPUT_QUIT = 3;
  private ParentModel modelLayer;


  public ClientHandler(ParentModel model) {
    modelLayer = model;
    if (modelLayer == null) {
      throw new IllegalArgumentException("Model can't be null");
    }

    // Start with 1st Level as default
    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);

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
      Function<Scanner, CoreOperation> mTransformationFunction =
              InjectLevelUtil
                      .getOptionsMap()
                      .getOrDefault(userChoice, null);

      if (mTransformationFunction == null) {
        // Handle with some default operation
        new DefaultOperation().passControl(scanner, modelLayer);
      } else {
        // Apply Transformation
        initialCoreOperation = mTransformationFunction.apply(scanner);
        initialCoreOperation.passControl(scanner, modelLayer);
      }
    }
  }
}
