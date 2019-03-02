package edu.northeastern.ccs.im.client;

import java.util.Scanner;

public class CreateGroup implements CoreOperation {

  private Scanner scanner;

  public CreateGroup(Scanner scanner) {
    this.scanner = scanner;
  }

  /**
   * This is where the control is passed to when a create group operation is performed.
   *
   * @param userModelTopLayer The Model that deals with the top most options in the menu in command
   *                          prompt.
   */
  @Override
  public void passControl(TopModelLayer userModelTopLayer) {
    //TODO: Do operations for this layer here
  }
}
