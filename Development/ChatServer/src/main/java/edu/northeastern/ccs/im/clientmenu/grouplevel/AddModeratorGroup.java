package edu.northeastern.ccs.im.clientmenu.grouplevel;

import java.util.Map;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.grouplevel.GetAllGroupsUtil.getAllUserGroup;
import static edu.northeastern.ccs.im.clientmenu.grouplevel.GetAllGroupsUtil.userToToggleModeratorOrDeleteUserGroup;

public class AddModeratorGroup implements CoreOperation {

    private Scanner mScanner;
    private Connection mConnectionLayerModel;

    @Override
    public void passControl(Scanner scanner, Connection connectionLayerModel) {
        mScanner = scanner;
        mConnectionLayerModel = connectionLayerModel;

        Map<String, Boolean> userMap = getAllUserGroup(connectionLayerModel);

        if (userMap == null) {
            FrontEnd.getView().sendToView("ERROR: Operation Failed. Please try again");
        }

        else {
            parseResponse(userMap);
        }
    }

    private void parseResponse(Map<String, Boolean> userModMap) {

        FrontEnd.getView().sendToView("INPUT: Which user from above do you want to upgrade to as moderator?");
        String userToUpgrade = mScanner.nextLine().trim();
        if (!userModMap.containsKey(userToUpgrade)) {
            FrontEnd.getView().sendToView("ERROR: Illegal Name Entered. Sending you back");
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
        } else {
            legalEntryUsername(userModMap, userToUpgrade);
        }
    }

    /**
     * If the user entered a valid username from the list, we proceed with the add/upgrade of user.
     *
     * @param userModMap    The map that contains all the member names of the group.
     * @param userToUpgrade The user we want to change to admin and hence make moderator.
     */
    private void legalEntryUsername(Map<String, Boolean> userModMap, String userToUpgrade) {
        if (userModMap.get(userToUpgrade)) {
            // Means s/he is already a moderator
            FrontEnd.getView().sendToView("ERROR: The selected user is already a moderator. Sending you back");
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
        } else {
            String responseBoolean = userToToggleModeratorOrDeleteUserGroup(userToUpgrade,
                    mConnectionLayerModel, MessageType.TOGGLE_MODERATOR);
            if (responseBoolean.equalsIgnoreCase("true")) {
                FrontEnd.getView().sendToView("SUCCESS: Operation Successful: \t" + userToUpgrade
                        + " is a moderator now.");
                InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
            } else {
                FrontEnd.getView().sendToView("ERROR: Operation Failed. Please try again");
                InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
            }
        }
    }



}
