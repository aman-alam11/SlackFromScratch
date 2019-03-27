package edu.northeastern.ccs.im.view;

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


  public void showMainMenu() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Welcome to Chatter Application \n");
    stringBuilder.append("1. Login \n");
    stringBuilder.append("2. Registration \n");
    stringBuilder.append("Or Enter \\q to quit \n");
    stringBuilder.append("Enter From above Options: \n");
    printToStream(stringBuilder.toString());
  }


  public void showUserLevelOptions() {
    System.out.println("1. Unread Messages");
    System.out.println("2. Chat user");
    System.out.println("3. Group Options");
    System.out.println("Or Enter \\q to quit");
    System.out.println("Enter From above Options: ");
  }

  public void showGroupLevelOptions() {
    System.out.println("1. Create Group");
    System.out.println("2. Update Group");
    System.out.println("3. Group Chat");
    System.out.println("Or Enter \\q to quit");
    System.out.println("Enter From above Options: ");
  }


  private static void printToStream(String string){
    System.out.println(string);
  }

}
