package edu.northeastern.ccs.im.clientmenu.loginlevel;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.util.Scanner;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.client.communication.AsyncListener;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials.generateLoginCredentials;
import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class Registration extends CommonOperations implements AsyncListener {

  @Override
  public void passControl(Scanner scanner, Connection model) {

    if (model != null) {
      // Take user details to register the user.
      FrontEnd.getView().sendToView("Hi, Please Enter the following details to register.");
      FrontEnd.getView().sendToView("Enter User Name");
      String username = scanner.nextLine().toLowerCase().trim();

      // Take password and confirm password.
      FrontEnd.getView().sendToView("Enter Password");
      String password = scanner.nextLine().trim();

      FrontEnd.getView().sendToView("Enter Password Again");
      String passwordCheck = scanner.nextLine().trim();

      /**
       * Check if the password and confirm password are same,
       * then create a message json including the username, password
       * and a message type.
       * If the passwords don't match then return the message to user.
       */
      if (password.equals(passwordCheck)) {
        MessageJson messageJson = generateLoginCredentials(username, password, MessageType.CREATE_USER);
        model.sendMessage(messageJson);
        // Wait for response synchronously
        String resp = waitForResponseSocket(model);
        if (!StringUtil.isBlank(resp)) {
          this.listen(resp);
        } else {
          // TODO: Some default response
        }
      } else {
        FrontEnd.getView().sendToView("Passwords do not match! Please try again");
      }
    }
  }

  @Override
  public void listen(String message) {
    AckModel ackModel = new Gson().fromJson(message, AckModel.class);
    if (ackModel.isLogin()) {
      return;
    }

    if (!ackModel.isUserAuthenticated()) {
      FrontEnd.getView().sendToView("Registration Failed, " + ackModel.getErrorMessage());
      FrontEnd.getView().sendToView("Taking to back to registration");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LOGIN_LEVEL);
    } else {
      FrontEnd.getView().sendToView("Account Created Successfully!");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);
    }
  }
}
