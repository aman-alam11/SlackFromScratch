package edu.northeastern.ccs.im.view;

@SuppressWarnings("all")
public class FrontEnd {

  private static FrontEnd mFrontEnd;


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
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Welcome to Chatter Application \n");
    stringBuilder.append("1. Login \n");
    stringBuilder.append("2. Registration \n");
    stringBuilder.append("Or Enter \\q to quit \n");
    stringBuilder.append("INPUT: Enter From above Options: \n");
    printToStream(stringBuilder.toString());
  }


  public void showUserLevelOptions() {
    System.out.println("1. Unread Messages");
    System.out.println("2. Chat user");
    System.out.println("3. Group Options");
    System.out.println("Or Enter \\q to quit");
    System.out.println("INPUT: Enter From above Options: ");
  }

  public void enterLines() {
    for(int i = 0; i < 100; i++ ){
      System.out.print("*");
    }
    System.out.println("\n");
  }

  public void showGroupLevelOptions() {
    System.out.println("1. Create Group");
    System.out.println("2. Update Group");
    System.out.println("3. Group Chat");
    System.out.println("4. Delete Group");
    System.out.println("Or Enter \\b to go back or \\q to quit");
    System.out.println("INPUT: Enter From above Options: ");
  }

  public void showGroupUsersCrudLevelOptions() {
    System.out.println("1. Add Users");
    System.out.println("2. Delete Users");
    System.out.println("Or Enter \\b to go back or \\q to quit");
    System.out.println("INPUT: Enter From above Options: ");
  }

  public void showUpdateGroupOptions() {
    FrontEnd.getView().sendToView("");
    System.out.println("1. Add/delete Users.");
    System.out.println("2. Add/delete Moderators.");
    System.out.println("3. Rename Group");
  }


  private static void printToStream(String string){
    System.out.println(string);
  }

    public void showModeratorsOptions() {
        System.out.println("1. Add Moderators.");
        System.out.println("2. Delete Moderators.");
    }
}
