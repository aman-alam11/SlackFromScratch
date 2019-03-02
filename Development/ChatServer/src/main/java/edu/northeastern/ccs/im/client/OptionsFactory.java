package edu.northeastern.ccs.im.client;

import java.util.Scanner;

/**
 * Factory to create CoreOperations for client.
 */
public interface OptionsFactory {

  /**
   * UnreadMessages Object is returned.
   * @param scanner - Scanner input for user input.
   * @return - CoreOperation Object of required Class.
   */
   CoreOperation getUnreadMessages(Scanner scanner);

  /**
   * AllUserChats Object is returned.
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
