package edu.northeastern.ccs.im.clientmenu.userlevel;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;

import java.util.Scanner;

public class UserFollowLayer implements CoreOperation {
    @Override
    public void passControl(Scanner scanner, Connection connectionLayerModel) {
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.FOLLOW_USER_LEVEL);
    }
}
