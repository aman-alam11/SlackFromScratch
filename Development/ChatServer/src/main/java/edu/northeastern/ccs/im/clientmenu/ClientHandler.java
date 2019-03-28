package edu.northeastern.ccs.im.clientmenu;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.clientmenu.models.UserChat;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

public final class ClientHandler {

  private Connection modelLayer;
  private static final String QUIT = "\\q";
  private static final String BACK = "\\b";


  public ClientHandler(Connection model) {
    modelLayer = model;
    if (modelLayer == null) {
      FrontEnd.getView().sendToView("INFO: Server is not Running, Exiting.");
      FrontEnd.getView().sendToView("Bye!!");
      return;
    }

    // Start with 1st Level as default
    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LOGIN_LEVEL);
  }


  public void initClientOperations(Scanner scanner) {
    while (scanner.hasNext()) {
      int userChoice = 0;
      String choiceString = "";
      boolean inputValidate = true;

      try {
        choiceString = scanner.nextLine().trim().toLowerCase();
        userChoice = Integer.parseInt(choiceString);
      } catch (Exception e) {
        // Handle with default implementation
        inputValidate = false;
        if (choiceString.equalsIgnoreCase(QUIT)) {
            UserChat userChat = new UserChat();
            MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.LOG_OUT,
                    new Gson().toJson(userChat));
            modelLayer.sendMessage(messageJson);
            modelLayer.terminate();
            FrontEnd.getView().sendToView("Bye!!");
            return;
          }

        else if(choiceString.equalsIgnoreCase(BACK)) {
          Map<CurrentLevel,CurrentLevel> map = InjectLevelUtil.getInstance().getLevelMap();
          CurrentLevel currentLevel = InjectLevelUtil.getInstance().getCurrentLevel();
          if(currentLevel == CurrentLevel.LOGIN_LEVEL || currentLevel == CurrentLevel.USER_LEVEL) {
            FrontEnd.getView().sendToView("INFO: Cannot go back from this level!");
          }
          else {
            CurrentLevel level = map.get(currentLevel);
            InjectLevelUtil.getInstance().injectLevel(level);
          }
        }

        else {
          FrontEnd.getView().sendToView("ERROR: Wrong Input.");
        }
      }

      if (inputValidate) {
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
}
