package edu.northeastern.ccs.im.server.business.logic.handler;

import java.util.HashSet;
import java.util.Set;

public class ProfanityFilter {
  private static final String SINGLESPACE = " ";

  private ProfanityFilter() {
    // Private Constructor for safety
  }

  private static Set<String> wordSet;

  private static boolean containsWord(String word) {
    if (wordSet == null) {
      initProfaneSet();
    }
    return wordSet.contains(word.toLowerCase());
  }

  public static String filterMessage(String word) {
    String[] wordList = word.split(SINGLESPACE);

    for(int i = 0; i < wordList.length; i++) {
      if (containsWord(wordList[i])) {
        wordList[i] = countAndReplace(wordList[i]);
      }
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (String str: wordList) {
      stringBuilder.append(str);
      stringBuilder.append(SINGLESPACE);
    }
    return stringBuilder.toString();
  }

  private static void initProfaneSet() {
    wordSet = new HashSet<>();
    wordSet.add("fuck");
    wordSet.add("ass");
    wordSet.add("dick");
  }

  private static String countAndReplace(String str)  {
    int length = str.length();
    StringBuilder stringBuilder = new StringBuilder();
    for(int i = 0; i < length; i++) {
      stringBuilder.append("*");
    }
    return stringBuilder.toString();
  }

}
