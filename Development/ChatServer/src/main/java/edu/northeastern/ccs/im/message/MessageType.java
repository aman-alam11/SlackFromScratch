package edu.northeastern.ccs.im.message;

/**
 * Enumeration for the different types of messages.
 *
 * @author Maria Jump
 */
public enum MessageType {
  /**
   * Message sent by the user attempting to login using a specified username.
   */
  HELLO("HLO"),
  /**
   * Message sent by the user to start the logging out process and sent by the server once the
   * logout process completes.
   */
  QUIT("BYE"),
  /**
   * Message whose contents is broadcast to all connected users.
   */
  BROADCAST("BCT"),

  /**
   * Message whose contents are login credentials
   */
  LOGIN("LGN"),

  /**
   * Search if a user exists or not for chat
   */
  USER_SEARCH("SRH"),

  /**
   * Message whose contents are user to user chat and it is general message type.
   */
  USER_CHAT("UCT"),

  /**
   * Send this type before the starting of the chat, to let server know that user is about to start chat.
   */
  USER_CHAT_START("UCS"),

  /**
   * Send this after quitting the chat to let server know that client is not chatting now.
   */
  USER_CHAT_END("UCE"),

  /**
   * Message whose contents are user to group chat
   */
  GROUP_CHAT("GCT"),

  /**
   * Message whose contents are for user creation
   */
  CREATE_USER("CUR"),

  /**
   * Message whose contents are for group creation
   */
  CREATE_GROUP("CGR"),
  
  /**
   * Message whose contents are for group creation
   */
  ADD_USER_IN_GROUP("AGR"),

  /**
   * Message to prompt quit
   */
  LOG_OUT("LGO"),

  AUTH_ACK("ACK"),
  
   /**
   * Unread Messages.
   */
  UNREAD_MSG("UNM"),

  /**
   * To set all undelivered messages as delivered
   */
  DELIVER_UNREAD_MSG("DUR");

  /**
   * Store the short name of this message type.
   */
  private String abbreviation;

  /**
   * Define the message type and specify its short name.
   *
   * @param abbrev Short name of this message type, as a String.
   */
  private MessageType(String abbrev) {
    abbreviation = abbrev;
  }

  /**
   * Return a representation of this Message as a String.
   *
   * @return Three letter abbreviation for this type of message.
   */
  @Override
  public String toString() {
    return abbreviation;
  }
}
