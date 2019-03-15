package edu.northeastern.ccs.im.view;

public class FrontEnd {

  private static FrontEnd mFrontEnd;
  public static boolean isWaitingForResponse = false;


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


  public void showFirstLevelOptions() {
    System.out.println("1. Unread Messages");
    System.out.println("2. Chat user");
    System.out.println("Or type logout to logout user");
    System.out.println("Enter From above Options: ");
  }


  public void showLoadingView(boolean shouldStopLoading) {
    System.out.println(shouldStopLoading ? "Loading complete" : "\nLoading" );
    long getStartTime = shouldStopLoading ?  System.currentTimeMillis() + 10000 : System.currentTimeMillis();
    isWaitingForResponse = !shouldStopLoading;

    while (!shouldStopLoading && (System.currentTimeMillis() - getStartTime < 200)) {
      System.out.print(".");
    }
    System.out.println("\n");
  }

}
