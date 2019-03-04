package edu.northeastern.ccs.im.clientmenu;

import edu.northeastern.ccs.im.clientmenu.firstlevel.FirstLevelModel;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.ParentModel;

public class MainClass {

  public static void main(String[] args) {
    ParentModel topModelLayer =  new FirstLevelModel();
    ClientHandler clientHandler = new ClientHandler(topModelLayer);
    clientHandler.initClientOperations();
  }
}
