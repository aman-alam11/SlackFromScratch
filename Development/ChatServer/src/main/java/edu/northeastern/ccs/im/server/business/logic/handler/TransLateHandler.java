package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.model.TranslateModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.JsonMessageHandlerFactory;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

/**
 * https://cloud.google.com/translate/docs/reference/libraries
 * https://github.com/GoogleCloudPlatform/java-docs-samples/blob/master/translate/cloud-client/src/main/java/com/example/translate/QuickstartSample.java
 */
public class TransLateHandler implements MessageHandler {


  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {
    try {
      Gson mGson = new Gson();
      TranslateModel translateModel = mGson.fromJson(message, TranslateModel.class);


      Translate translate = TranslateOptions.getDefaultInstance().getService();

      String text = translateModel.getMessage();
      String language = translateModel.getToLanguage();

      Translation translation =
              translate.translate(
                      text,
                      TranslateOption.targetLanguage(language));
      ChatModel chatModel = new ChatModel();

      chatModel.setMsg(translation.getTranslatedText());
      chatModel.setToUserName(translateModel.getToUser());
      chatModel.setFromUserName(translateModel.getFromUser());

      new JsonMessageHandlerFactory().getMessageHandler(MessageType.USER_CHAT)
              .handleMessage(user,mGson.toJson(chatModel),clientConnection);
      return true;
    }
    catch (Exception ex ){
      ChatLogger.info("Error in translating the string");
      return false;
    }
  }
}
