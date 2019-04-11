package edu.northeastern.ccs.im.view;

@SuppressWarnings({"squid:S106"})
public class FrontEnd {

  private static final String ENTER_BACK_QUIT = "Or Enter \\b to go back or \\q to quit";
  private static FrontEnd mFrontEnd;
  private static final String INPUT_ABOVE_OPTIONS = "INPUT: Enter From above Options: ";


  private FrontEnd() {
  }

  public static FrontEnd getView() {
    if (mFrontEnd == null) {
      mFrontEnd = new FrontEnd();
    }

    return mFrontEnd;
  }


  public void sendToView(String stringToDisplay) {
    if (stringToDisplay == null) {
      return;
    }
    printToStream(stringToDisplay);
  }

  public void sendToViewSameLine(String stringToDisplay) {
    if (stringToDisplay == null) {
      return;
    }
    System.out.print(stringToDisplay);
  }


  public void showMainMenu() {
    String stringBuilder = "Welcome to Chatter Application \n" +
            "1. Login \n" +
            "2. Registration \n" +
            "Or Enter \\q to quit \n" +
            "INPUT: Enter From above Options: \n";
    printToStream(stringBuilder);
  }


  // Send false as default here

  /**
   * For normal purposes, send false here. This 4th option is only for super user here.
   *
   */
  public void showUserLevelOptions() {
    System.out.println("1. Unread Messages");
    System.out.println("2. Chat user");
    System.out.println("3. Group Options");
    System.out.println("4. Follow User options");
    System.out.println("Or Enter \\q to quit");
    System.out.println(INPUT_ABOVE_OPTIONS);
  }

  public void enterLines() {
    for (int i = 0; i < 100; i++) {
      System.out.print("*");
    }
    System.out.println("\n");
  }

  public void showGroupLevelOptions() {
    System.out.println("1. Create Group");
    System.out.println("2. Update Group");
    System.out.println("3. Group Chat");
    System.out.println("4. Delete Group");
    System.out.println(ENTER_BACK_QUIT);
    System.out.println(INPUT_ABOVE_OPTIONS);
  }

  public void showFollowUserLevelOptions() {
    System.out.println("1. Follow a User");
    System.out.println("2. List my followers");
    System.out.println(ENTER_BACK_QUIT);
    System.out.println(INPUT_ABOVE_OPTIONS);
  }

  public void showFollwUserLevelOptions(){
    System.out.println("1. Follow a user");
    System.out.println("2. List my followers");
  }

  public void showGroupUsersCrudLevelOptions() {
    System.out.println("1. Add Users");
    System.out.println("2. Delete Users");
    System.out.println(ENTER_BACK_QUIT);
    System.out.println(INPUT_ABOVE_OPTIONS);
  }

  public void showUpdateGroupOptions() {
    FrontEnd.getView().sendToView("");
    System.out.println("1. Add/delete Users.");
    System.out.println("2. Add/delete Moderators.");
    System.out.println("3. Rename Group");
  }

  public void showDefaultErrorMessage() {
    System.out.println("Uh Oh! This is embarrassing. Something went wrong.\n " +
            "Sending you back to main menu");
  }


  private static void printToStream(String string) {
    System.out.println(string);
  }

  public void showModeratorsOptions() {
    System.out.println("1. Add Moderators.");
    System.out.println("2. Delete Moderators.");
  }

  public void showSuperUserOperations() {
    System.out.println("1. Get All Conversations for a particular User for user to user chat");
    System.out.println("2. Get All Conversations for a particular User for all group chats");
    // Basically all unread messages
    System.out.println("3. Get All Conversations for a user for both user to user chat and group");
    System.out.println("4. Get All Conversations for a particular Group");
  }
}
