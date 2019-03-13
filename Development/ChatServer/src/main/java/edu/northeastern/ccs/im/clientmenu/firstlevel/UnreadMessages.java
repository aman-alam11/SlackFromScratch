package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UnreadMessages implements CoreOperation {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, Connection connectionModelLayer) {
    this.mScanner = scanner;
    FrontEnd.getView().sendToView(String.valueOf(connectionModelLayer.hasNext()));

//    while(model.hasNext()) {
//      FrontEnd.getView().sendToView("User: " + model.next().getFromUser());
//      FrontEnd.getView().sendToView("Message: " + model.next().getMessage());
//    }
  }
}
