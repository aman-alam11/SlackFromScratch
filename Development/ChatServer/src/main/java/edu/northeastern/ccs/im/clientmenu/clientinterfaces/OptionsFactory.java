package edu.northeastern.ccs.im.clientmenu.clientinterfaces;

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
   * UserChat Object is returned.
   * @param scanner - Scanner input for user input.
   * @return - CoreOperation Object of required Class.
   */
   CoreOperation getUserChat(Scanner scanner);

}
