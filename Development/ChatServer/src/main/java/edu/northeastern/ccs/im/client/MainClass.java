package edu.northeastern.ccs.im.client;

import edu.northeastern.ccs.im.client.firstlevel.FirstLevelModel;
import edu.northeastern.ccs.im.client.clientinterfaces.ParentModel;

public class MainClass {

  public static void main(String[] args) {
    ParentModel topModelLayer =  new FirstLevelModel();
    ClientHandler clientHandler = new ClientHandler(topModelLayer);
    clientHandler.initClientOperations();
  }
}
