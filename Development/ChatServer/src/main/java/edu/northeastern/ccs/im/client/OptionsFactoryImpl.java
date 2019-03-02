package edu.northeastern.ccs.im.client;

import java.util.Scanner;

public class OptionsFactoryImpl implements OptionsFactory {

  @Override
  public CoreOperation getLoginUser(Scanner scanner) {
    return new LoginUser(scanner);
  }

  @Override
  public CoreOperation getRegisterUser(Scanner scanner) {
    return new RegisterUser(scanner);
  }

  @Override
  public CoreOperation getUnreadMessages(Scanner scanner) {
    return new UnReadMessages(scanner);
  }

  @Override
  public CoreOperation getUserChat(Scanner scanner) {
    return new UserChat(scanner);
  }

  @Override
  public CoreOperation getCreateGroup(Scanner scanner) {
    return new CreateGroup(scanner);
  }
}
