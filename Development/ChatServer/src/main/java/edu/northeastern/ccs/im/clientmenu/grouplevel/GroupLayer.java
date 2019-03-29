package edu.northeastern.ccs.im.clientmenu.grouplevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;

public class GroupLayer implements CoreOperation {

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
  }
}
