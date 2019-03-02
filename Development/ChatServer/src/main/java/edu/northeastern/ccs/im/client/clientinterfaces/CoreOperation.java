package edu.northeastern.ccs.im.client.clientinterfaces;

import java.util.Scanner;

@FunctionalInterface
public interface CoreOperation {

  /**
   * This is the entry method for all the classes/functionality at the same level and hence where
   * control will be passed.
   */
  void passControl(Scanner scanner, ParentModel model);
}
