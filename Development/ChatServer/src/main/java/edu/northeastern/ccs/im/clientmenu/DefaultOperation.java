package edu.northeastern.ccs.im.clientmenu;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.view.FrontEnd;

import java.util.Scanner;

public class DefaultOperation implements CoreOperation {



    @Override
    public void passControl(Scanner scanner, Connection model) {
        FrontEnd.getView().sendToView("Invalid Input, please try again");
    }
}
