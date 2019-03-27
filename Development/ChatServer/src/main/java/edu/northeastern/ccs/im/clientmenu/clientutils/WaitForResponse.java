package edu.northeastern.ccs.im.clientmenu.clientutils;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.view.FrontEnd;

public class WaitForResponse {

  public static String waitForResponseSocket(Connection modelLayer) {

    waitForResponseSync(modelLayer);
    FrontEnd.getView().sendToView("LOADING COMPLETE\n");

    if (modelLayer.hasNext()) {
      return modelLayer.next().getMessage();
    }

    return "";
  }



  private static void waitForResponseSync(Connection modelLayer) {
    while (!modelLayer.hasNext()) {
      // Wait
      try {
        FrontEnd.getView().sendToView("LOADING");
        Thread.sleep(1000);
      } catch (Exception e) {
        ChatLogger.error("Unable to make the Thread sleep");
      }
    }
  }
}
