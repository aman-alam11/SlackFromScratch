package edu.northeastern.ccs.im.clientmenu;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.client.communication.SocketConnection;
import edu.northeastern.ccs.im.clientmenu.clientutils.ClientConstants;

public class
MainClass {

  public static void main(String[] args) {

    Connection topModelLayer = SocketConnection.getInstance(ClientConstants.URL, ClientConstants.PORT);
    ClientHandler clientHandler = new ClientHandler(topModelLayer);
    Scanner scanner = new Scanner(System.in);
    clientHandler.initClientOperations(scanner);
  }
}
