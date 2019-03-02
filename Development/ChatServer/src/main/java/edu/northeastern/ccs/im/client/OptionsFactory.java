package edu.northeastern.ccs.im.client;

import java.util.Scanner;

/**
 * Factory to create CoreOperations for client.
 */
public interface OptionsFactory {

  /**
   * Login User Object is returned.
   * @param scanner - Scanner input for user input.
   * @return - CoreOperation Object of required Class.
   */
   CoreOperation getLoginUser(Scanner scanner);

  /**
   * Register User Object is returned.
   * @param scanner - Scanner input for user input.
   * @return - CoreOperation Object of required Class.
   */
   CoreOperation getRegisterUser(Scanner scanner);

  /**
   * UnreadMessages Object is returned.
   * @param scanner - Scanner input for user input.
   * @return - CoreOperation Object of required Class.
   */
   CoreOperation getUnreadMessages(Scanner scanner);

  /**
   * UserChat Object is returned.
   * @param scanner - Scanner input for user input.
   * @return - CoreOperation Object of required Class.
   */
   CoreOperation getUserChat(Scanner scanner);

  /**
   * CreateGroup Object is returned.
   * @param scanner - Scanner input for user input.
   * @return - CoreOperation Object of required Class.
   */
   CoreOperation getCreateGroup(Scanner scanner);
}
