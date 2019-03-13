package edu.northeastern.ccs.im.clientmenu.firstlevel;


import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.client.communication.SocketConnection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.message.MessageJson;

public class FirstLevelModel implements CoreOperation {
//        implements Connection {

  private Connection connection;

//  public FirstLevelModel(String url, int port) {
//    connection = SocketConnection.getInstance(url, port);
//  }

  @Override
  public void passControl(Scanner scanner, Connection modelLayer) {

  }

//  @Override
//  public boolean isConnected() {
//    return connection.isConnected();
//  }
//
//  @Override
//  public boolean hasNext() {
//    return connection.hasNext();
//  }
//
//  @Override
//  public MessageJson next() {
//    return connection.next();
//  }
//
//  @Override
//  public boolean sendMessage(MessageJson message) {
//    return connection.sendMessage(message);
//  }
}
