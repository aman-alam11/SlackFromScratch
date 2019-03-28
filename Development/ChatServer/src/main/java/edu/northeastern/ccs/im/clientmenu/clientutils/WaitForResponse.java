package edu.northeastern.ccs.im.clientmenu.clientutils;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.view.FrontEnd;

public class WaitForResponse {

  private static final int WAIT_TIME_MILLISECONDS = 1000;
  private static final int TIMEOUT_MILLISECONDS = 5000;

  private static final StringBuilder IS_LOADING_MESSAGE = new StringBuilder("LOADING");
  private static final String LOADING_COMPLETE_MESSAGE = "LOADING COMPLETE\n";
  private static final String THREAD_SLEEP_ERROR = "Unable to make the Thread sleep";

  public static String waitForResponseSocket(Connection modelLayer) {
    waitForResponseSync(modelLayer);
    FrontEnd.getView().sendToView("\n"+ LOADING_COMPLETE_MESSAGE);

    if (modelLayer.hasNext()) {
      return modelLayer.next().getMessage();
    }

    return "";
  }


  private static void waitForResponseSync(Connection modelLayer) {
    int count = 0;
    while (!modelLayer.hasNext()) {
      // Wait
      try {
        if (count == 0) {
          FrontEnd.getView().sendToViewSameLine(IS_LOADING_MESSAGE.toString());
        } else {
          FrontEnd.getView().sendToViewSameLine(".");
        }
        Thread.sleep(WAIT_TIME_MILLISECONDS);
      } catch (Exception e) {
        ChatLogger.error(THREAD_SLEEP_ERROR);
      }
      count++;
    }
  }
}
