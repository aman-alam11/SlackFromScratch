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


  public void showFirstLevelOptions() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("1. Unread Messages \n");
    stringBuilder.append("2. Chat user \n");
    stringBuilder.append("Or Enter \\q to quit \n");
    stringBuilder.append("Enter From above Options: \n");
    printToStream(stringBuilder.toString());
  }


  public static void showLoadingView(boolean shouldStopLoading) {
    printToStream(shouldStopLoading ? "Loading complete" : "\nLoading" );
    long getStartTime = shouldStopLoading ?  System.currentTimeMillis() + 10000 : System.currentTimeMillis();

    while (!shouldStopLoading && (System.currentTimeMillis() - getStartTime < 200)) {
      printToStream(".");
    }
  }

  private static void printToStream(String string){
    System.out.println(string);
  }

}
