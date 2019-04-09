package edu.northeastern.ccs.im;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.junit.Test;

import java.io.Console;



public class TranslateTest {

  @Test
  public void test() {
    // Instantiates a client
    Translate translate = TranslateOptions.getDefaultInstance().getService();

    // The text to translate
    String text = "Hello, world!";

    // Translates some text into Russian
    Translation translation =
            translate.translate(
                    text,
                    TranslateOption.targetLanguage("ru"));


    System.out.printf("Text: %s%n", text);
    System.out.printf("Translation: %s%n", translation.getTranslatedText());


  }
}
