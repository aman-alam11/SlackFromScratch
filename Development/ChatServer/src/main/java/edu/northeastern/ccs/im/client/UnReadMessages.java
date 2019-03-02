package edu.northeastern.ccs.im.client;

import java.util.Scanner;

public class UnReadMessages implements CoreOperation {


  private Scanner scanner;

  public UnReadMessages(Scanner scanner) {
    this.scanner = scanner;
  }


  @Override
  public void passControl(ParentModel userModelTopLayer) {

  }
}
