package edu.northeastern.ccs.im.client;

import java.util.Scanner;

public class RegisterUser implements CoreOperation {


  private Scanner scanner;

  public RegisterUser(Scanner scanner) {
    this.scanner = scanner;
  }

  @Override
  public void passControl(TopModelLayer userModelTopLayer) {


  }
}
