package edu.northeastern.ccs.im.clientmenu;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.view.FrontEnd;

public class DefaultOperation implements CoreOperation {


    private Scanner mScanner;

    @Override
    public void passControl(Scanner scanner, Connection model) {
        this.mScanner = scanner;
        FrontEnd.getView().sendToView("Invalid Response, please try again");
    }
}
