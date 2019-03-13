package edu.northeastern.ccs.im.clientmenu.login;

import com.google.gson.Gson;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.AsyncListener;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.client.communication.SocketConnection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
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
@SuppressWarnings("ALL")
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
    ((SocketConnection) modelLayer).registerListener(this, MessageType.AUTH_ACK);
    MessageJson messageJson = new GenerateLoginCredentials().generateLoginCredentials(username,
            password,
            MessageType.LOGIN);
    modelLayer.sendMessage(messageJson);
    FrontEnd.getView().showLoadingView();
  }

  @Override
  public void listen(String message) {
    AckModel ackModel = mGson.fromJson(message, AckModel.class);

    if (!ackModel.isLogin()) {
      return;
    }

    if (!ackModel.isUserAuthenticated()) {
      FrontEnd.getView().sendToView("Login Failed, " + ackModel.getErrorMessage());
    } else {

      FrontEnd.getView().sendToView("Welcome ");

      // User is authenticated by server
      // Send user forward
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);

//                int userChoice = 0;
//                String choiceString = "";
//                try {
//                    choiceString = scanner.nextLine().trim();
//                    userChoice = Integer.parseInt(choiceString);
//
//                } catch (Exception e) {
//                    // Handle with default implementation
//                    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);
//                }
//
//                CoreOperation initialCoreOperation;
//                Function<Scanner, CoreOperation> mTransformationFunction = InjectLevelUtil.getOptionsMap()
//                        .getOrDefault(userChoice, null);
//
//                if (mTransformationFunction == null) {
//                    // Handle with some default operation
//                    new DefaultOperation().passControl(scanner, model);
//                } else {
//                    // Apply Transformation
//                    initialCoreOperation = mTransformationFunction.apply(scanner);
//
//                    initialCoreOperation.passControl(scanner, model);
//                }
    }
  }

}
