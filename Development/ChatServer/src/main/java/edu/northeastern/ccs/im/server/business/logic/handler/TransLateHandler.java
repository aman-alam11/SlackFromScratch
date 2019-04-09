package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;

import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.model.TranslateModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

public class TransLateHandler implements MessageHandler {


  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {
    Gson mGson = new Gson();
    TranslateModel translateModel = mGson.fromJson(message, TranslateModel.class);


    Translate translate = TranslateOptions.getDefaultInstance().getService();

    // The text to translate
    String text = translateModel.getMessage();
    String language = translateModel.getToLanguage();

    // Translates some text into Russian
    Translation translation =
            translate.translate(
                    text,
                    TranslateOption.targetLanguage(language));
    ChatModel chatModel = new ChatModel();

    chatModel.setMsg(translation.getTranslatedText());
    chatModel.setToUserName(translateModel.getToUser());
    chatModel.setFromUserName(translateModel.getFromUser());

    ChatHandler chatHandler = new ChatHandler();
    chatHandler.handleMessage(user,mGson.toJson(chatModel),clientConnection);
    return false;
  }
}
