package edu.northeastern.ccs.im.clientmenu.clientinterfaces;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;

public interface CoreOperation {

  void passControl(Scanner scanner, Connection connectionLayerModel);

}
