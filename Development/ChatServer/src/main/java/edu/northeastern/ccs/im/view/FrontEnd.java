package edu.northeastern.ccs.im.view;

import java.util.Timer;
import java.util.TimerTask;

import edu.northeastern.ccs.im.client.communication.AsyncListener;

public class FrontEnd implements AsyncListener {

  private static FrontEnd mFrontEnd;
  private boolean shouldStopLoading = false;
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
    System.out.println("3. Chat user");
    // TODO
//    System.out.println("Or type logout to logout user");
    System.out.println("Enter From above Options: ");
  }


  public void showLoadingView() {
    System.out.println("\nLoading");
    long getStartTime = System.currentTimeMillis();
    Timer timer = new Timer();
    isWaitingForResponse = true;
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        System.out.print(".");
      }
    }, 1, 1000);

    while (!shouldStopLoading && (System.currentTimeMillis() - getStartTime < 5000)) {
      timer.cancel();
      isWaitingForResponse = false;
    }
    System.out.println("\n");
  }

  @Override
  public void listen(String message) {
    // Stop if we get a response or if we have a timeout of 5 seconds (ANR)
    isWaitingForResponse = false;
    shouldStopLoading = true;
  }
}
