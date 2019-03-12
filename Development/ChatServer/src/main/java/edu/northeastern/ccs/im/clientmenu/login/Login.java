package edu.northeastern.ccs.im.clientmenu.login;

import java.util.Scanner;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.client.communication.AsyncListener;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.LoginCredentials;
import edu.northeastern.ccs.im.view.FrontEnd;

/**
 * This is the Login class which is being used for the Login level
 */
@SuppressWarnings("ALL")
public class Login extends CommonOperations implements AsyncListener {
    private Gson mGson;


    @Override
    public void passControl(Scanner scanner, Connection model) {

        //flag to check stop while loop when login has successful
        boolean loginFlag = true;
        mGson = new Gson();

        //limit number of logins to 3
        int limit = 0;

        //TODO login after wrong attempt not working
        FrontEnd.getView().sendToView("Enter User Name");
        String username = scanner.nextLine().trim();
        FrontEnd.getView().sendToView("Enter password");
        String password = scanner.nextLine().trim();

        LoginCredentials loginCredentials = new LoginCredentials(username, password);
        String jsonLoginCredentials = mGson.toJson(loginCredentials);
        MessageJson messageJson = new MessageJson(username, MessageType.LOGIN, jsonLoginCredentials);
        model.sendMessage(messageJson);
    }

    @Override
    public void listen(String message) {
        AckModel ackModel = mGson.fromJson(message, AckModel.class);
        if (!ackModel.isUserAuthenticated()) {
            FrontEnd.getView().sendToView("Login Failed, " + ackModel.getErrorMessage());
            // TODO: Inject Level here
            return;
        } else {

            FrontEnd.getView().sendToView("Welcome ");
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);

//            while (scanner.hasNext()) {
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
//            }
        }
    }
}
