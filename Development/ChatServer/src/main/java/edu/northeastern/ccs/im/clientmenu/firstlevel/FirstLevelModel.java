package edu.northeastern.ccs.im.clientmenu.firstlevel;


import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;

public class FirstLevelModel implements CoreOperation {


  @Override
  public void passControl(Scanner scanner, Connection connectionModelLayer) {
    System.out.println("Inside First LevelModel");
  }

}
