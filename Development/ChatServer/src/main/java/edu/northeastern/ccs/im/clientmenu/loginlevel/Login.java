package edu.northeastern.ccs.im.clientmenu.loginlevel;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.AsyncListener;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials.generateLoginCredentials;
import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

/**
 * This is the Login class which is being used for the Login level.
 */
public class Login extends CommonOperations implements AsyncListener {

  private Gson mGson;

  @Override
  public void passControl(Scanner scanner, Connection modelLayer) {

    if (modelLayer != null) {
      mGson = new Gson();

      FrontEnd.getView().sendToView("INPUT: Enter User Name");
      String username = scanner.nextLine().toLowerCase().trim();

      FrontEnd.getView().sendToView("INPUT: Enter password");
      String password = scanner.nextLine().trim();

      MessageJson messageJson = generateLoginCredentials(username, password, MessageType.LOGIN);
      modelLayer.sendMessage(messageJson);

      FrontEnd.getView().sendToView("\nLOADING\n");

      String resp = waitForResponseSocket(modelLayer);
      if (!StringUtil.isBlank(resp)) {
        this.listen(resp);
      } else {
        // TODO: Some default response
      }
    }
  }

  @Override
  public void listen(String message) {
    AckModel ackModel = mGson.fromJson(message, AckModel.class);

    if (!ackModel.isLogin()) {
      return;
    }

    if (!ackModel.isUserAuthenticated()) {
      FrontEnd.getView().sendToView("ERROR: Login Failed, " + ackModel.getErrorMessage());
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LOGIN_LEVEL);
    } else {

      FrontEnd.getView().sendToView("WELCOME: " + GenerateLoginCredentials.getUsername());

      // User is authenticated by server
      // Send user forward
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);
    }
  }
}
