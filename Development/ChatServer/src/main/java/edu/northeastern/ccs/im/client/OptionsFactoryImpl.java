package edu.northeastern.ccs.im.client;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.FirstLevel.AllUserChats;
import edu.northeastern.ccs.im.client.FirstLevel.CreateGroup;

public class OptionsFactoryImpl implements OptionsFactory {

  @Override
  public CoreOperation getUnreadMessages(Scanner scanner) {
    return new UnReadMessages(scanner);
  }

  @Override
  public CoreOperation getUserChat(Scanner scanner) {
    return new AllUserChats(scanner);
  }

  @Override
  public CoreOperation getCreateGroup(Scanner scanner) {
    return new CreateGroup(scanner);
  }
}
