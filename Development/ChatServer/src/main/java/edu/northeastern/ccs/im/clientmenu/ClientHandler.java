package edu.northeastern.ccs.im.clientmenu;

import java.util.Scanner;
import java.util.function.Function;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.view.FrontEnd;

public final class ClientHandler {

  private Connection modelLayer;


  public ClientHandler(Connection model) {
    modelLayer = model;
    if (modelLayer == null) {
      throw new IllegalArgumentException("Model can't be null");
    }

    // Start with 1st Level as default
    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LOGIN_LEVEL);
  }


  public void initClientOperations(Scanner scanner) {
    while (scanner.hasNext()) {
      int userChoice = 0;
      String choiceString = "";

      try {
        choiceString = scanner.nextLine().trim().toLowerCase();
        userChoice = Integer.parseInt(choiceString);
      } catch (Exception e) {
        // Handle with default implementation
        if (choiceString.equals("\\q")) {
          return;
        } else {
          FrontEnd.getView().sendToView("Wrong input, try again.");
        }
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
