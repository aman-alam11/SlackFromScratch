package edu.northeastern.ccs.im.clientmenu;

import java.util.Scanner;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.OptionsFactory;

public class OptionsFactoryImpl implements OptionsFactory {
  @Override
  public CoreOperation getUnreadMessages(Scanner scanner) {
    return null;
  }

  @Override
  public CoreOperation getUserChat(Scanner scanner) {
    return null;
  }

}
