package edu.northeastern.ccs.im.clientmenu.clientutils;

import edu.northeastern.ccs.im.clientmenu.factories.MapMessageTypeFactory;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.model.LoginCredentials;

import java.util.EnumMap;

public class MapMessageTypeToModel {

    private static MapMessageTypeToModel singleInstance = null;

    private MapMessageTypeToModel(){}

    private static EnumMap<MessageType, String> mtMapToModel = new
            EnumMap<>(MessageType.class);

    public static MapMessageTypeToModel getInstance()
    {
        if (singleInstance == null)
            singleInstance = new MapMessageTypeToModel();

        mtMapToModel.put(MessageType.HELLO, LoginCredentials.class.getSimpleName());
        mtMapToModel.put(MessageType.USER_CHAT, ChatModel.class.getSimpleName());
        mtMapToModel.put(MessageType.CREATE_USER, LoginCredentials.class.getSimpleName());

        return singleInstance;
    }

    public String deserialize(MessageType messageType){
        return mtMapToModel.get(messageType);
    }

    public Object serialize(String className){
        MapMessageTypeFactory mapMessageTypeFactory = new MapMessageTypeFactory();
        return mapMessageTypeFactory.getModelFromFactory(className);
    }
}
