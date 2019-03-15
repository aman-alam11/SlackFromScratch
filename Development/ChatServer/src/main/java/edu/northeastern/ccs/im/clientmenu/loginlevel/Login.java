package edu.northeastern.ccs.im.clientmenu.loginlevel;

import com.google.gson.Gson;


import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.AsyncListener;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientutils.AuthFlag;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.view.FrontEnd;

/**
 * This is the Login class which is being used for the Login level
 */
public class Login extends CommonOperations implements AsyncListener {

  private Gson mGson;


  @Override
  public void passControl(Scanner scanner, Connection modelLayer) {

    mGson = new Gson();

    FrontEnd.getView().sendToView("Enter User Name");
    String username = scanner.nextLine().toLowerCase().trim();

    FrontEnd.getView().sendToView("Enter password");
    String password = scanner.nextLine().trim();

    // Tell the server we are trying to authenticate user
    modelLayer.registerListener(this, MessageType.AUTH_ACK);
    MessageJson messageJson = new GenerateLoginCredentials().generateLoginCredentials(username,
            password,
            MessageType.LOGIN);
    modelLayer.sendMessage(messageJson);
    FrontEnd.getView().showLoadingView(false);
  }

  @Override
  public void listen(String message) {
    FrontEnd.getView().showLoadingView(true);
    AckModel ackModel = mGson.fromJson(message, AckModel.class);

    if (!ackModel.isLogin()) {
      return;
    }

    if (!ackModel.getUsername().equals(GenerateLoginCredentials.getUsername())) {
      if (!AuthFlag.isUserAuthenticated()) {
        FrontEnd.getView().sendToView("Login Failed, " + ackModel.getErrorMessage());
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LOGIN_LEVEL);
      }
      return;
    }

    if (!ackModel.isUserAuthenticated()) {
      FrontEnd.getView().sendToView("Login Failed, " + ackModel.getErrorMessage());
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LOGIN_LEVEL);
    } else {
      FrontEnd.getView().sendToView("Welcome ");

      // User is authenticated by server. Hence update flags as well for util
      new AuthFlag(true);

      // Send user forward
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);
    }
  }
}
