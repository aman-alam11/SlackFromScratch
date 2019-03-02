package edu.northeastern.ccs.im.client;

public class mainClass {

  public static void main(String[] args) {
    TopModelLayer topModelLayer =  new TopModelLayer();
    ClientHandler clientHandler = new ClientHandler(topModelLayer);
    clientHandler.initClientOperations();
  }
}
