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

    System.out.println(stringToDisplay);
  }


  public void showMainMenu() {
    System.out.println("Welcome to Chatter Application");
    System.out.println("1. Login");
    System.out.println("2. Registration");
    System.out.println("Or Enter \\q to quit");
    System.out.println("Enter From above Options: ");
  }


  public void showUserLevelOptions() {
    System.out.println("1. Unread Messages");
    System.out.println("2. Chat user");
    System.out.println("Or type logout to logout user");
    System.out.println("Enter From above Options: ");
  }

}
