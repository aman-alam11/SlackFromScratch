package edu.northeastern.ccs.im.clientmenu;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.client.communication.SocketConnection;

public class
MainClass {

  public static void main(String[] args) {

    Connection topModelLayer =  SocketConnection.getInstance(ClientConstants.URL, ClientConstants.PORT);
    ClientHandler clientHandler = new ClientHandler(topModelLayer);
    clientHandler.initClientOperations();
  }
}
