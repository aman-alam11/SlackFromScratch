package edu.northeastern.ccs.im.clientmenu.login;

import com.google.gson.Gson;

import java.util.Scanner;
import java.util.function.Function;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.DefaultOperation;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.LoginCredentials;
import edu.northeastern.ccs.im.view.FrontEnd;

/**
 * This is the Login class which is being used for the Login level
 */
public class Login extends CommonOperations implements AsyncListener {
  private Gson mGson;
  
  
  @Override
  void listen(String message) {
	  
  }

  @Override
  public void passControl(Scanner scanner, Connection model) {

    //flag to check stop while loop when login has successful
    boolean loginFlag = true;
    mGson = new Gson();
  
    //limit number of logins to 3
    int limit = 0;

    //TODO login after wrong attempt not working
    while (loginFlag && (limit < 3)) {
      FrontEnd.getView().sendToView("Enter User Name");
      String username = scanner.nextLine().trim();
      FrontEnd.getView().sendToView("Enter password");
      String password = scanner.nextLine().trim();

      
      if (sessionFactory.login()) {
        loginFlag = false;
        LoginCredentials loginCredentials = new LoginCredentials(username, password);
        String jsonLoginCredentials = mGson.toJson(loginCredentials);
        MessageJson messageJson = new MessageJson(username, MessageType.LOGIN, jsonLoginCredentials);
        model.sendMessage(messageJson);
        FrontEnd.getView().sendToView("Welcome " + username);
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);

        while (scanner.hasNext()) {
          int userChoice = 0;
          String choiceString  = "";
          try {
            choiceString = scanner.nextLine().trim();
            userChoice = Integer.parseInt(choiceString);

          } catch (Exception e) {
            // Handle with default implementation

            if (choiceString.equals("logout")) {

              //TODO logout not working
              sessionFactory.logoutUser();
              InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LOGIN_LEVEL);
              MessageJson messageLogout = new MessageJson(username, MessageType.LOG_OUT, "");
              model.sendMessage(messageLogout);
              return;
            }
            else {
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
            new DefaultOperation().passControl(scanner, model);
          } else {
            // Apply Transformation
            initialCoreOperation = mTransformationFunction.apply(scanner);

            initialCoreOperation.passControl(scanner, model);
          }
        }
      }

      else {
        if (limit == 2) {

          FrontEnd.getView().sendToView("Login Failed, Redirect to Main Menu");
        }
        else {
          FrontEnd.getView().sendToView("Login Failed, Try Again! Attempt: "
                  +  (limit + 2));
        }
        limit++;
      }
    }
    if (loginFlag) {
      FrontEnd.getView().showMainMenu();
    }
  }
}
