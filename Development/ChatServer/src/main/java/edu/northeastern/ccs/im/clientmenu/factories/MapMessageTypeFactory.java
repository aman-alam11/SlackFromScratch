package edu.northeastern.ccs.im.clientmenu.factories;

import edu.northeastern.ccs.im.clientmenu.DefaultOperation;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.model.LoginCredentials;

public class MapMessageTypeFactory {

    public Object getModelFromFactory(String className) {
        switch (className) {

            case "LoginCredentials":
                return new LoginCredentials();

            case "ChatModel":
                return new ChatModel();

            default:
                // This will return first level module when implemented as default
                return new DefaultOperation();
        }
    }

}
