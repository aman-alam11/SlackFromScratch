package edu.northeastern.ccs.im.client;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.client.clientinterfaces.OptionsFactory;

public class OptionsFactoryImpl implements OptionsFactory {
  @Override
  public CoreOperation getUnreadMessages(Scanner scanner) {
    return null;
  }

  @Override
  public CoreOperation getUserChat(Scanner scanner) {
    return null;
  }

  @Override
  public CoreOperation getCreateGroup(Scanner scanner) {
    return null;
  }

}
