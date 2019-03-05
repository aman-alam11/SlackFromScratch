package edu.northeastern.ccs.im.clientmenu.login;

import java.net.InetSocketAddress;
import java.util.Scanner;

import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.ParentModel;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.view.FrontEnd;

public class Registration extends CommonOperations {

  @Override
  public void passControl(Scanner scanner, ParentModel model) {

    FrontEnd.getView().sendToView("Hi, Please Enter the following details to register");
    FrontEnd.getView().sendToView("Enter User Name");
    String username = scanner.nextLine().trim();
    FrontEnd.getView().sendToView("Enter Password");
    String password = scanner.nextLine().trim();
    FrontEnd.getView().sendToView("Enter Password Again");
    String passwordCheck = scanner.nextLine().trim();

    if (password.equals(passwordCheck)) {
      SessionFactory sessionFactory = SessionFactory.getInstance(username, password);
      if (sessionFactory.createAccount()) {
        FrontEnd.getView().sendToView("Account Created Successfully!");
      }
      else {
        FrontEnd.getView().sendToView("Sorry! Something went wrong!");
      }
    }

    else {
      FrontEnd.getView().sendToView("Passwords do not match!");
    }
    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LOGIN_LEVEL);
  }
}
