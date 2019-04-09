package edu.northeastern.ccs.im.server.business.logic;

import java.util.HashSet;
import java.util.Set;

public class ProfanityFilter {
  private final static String SINGLESPACE = " ";

  private static ProfanityFilter profanityFilter;
  private static Set<String> wordSet;

  private ProfanityFilter() {
    addWords();
  }

  public static ProfanityFilter getInstace() {
    if (profanityFilter == null) {
      wordSet = new HashSet<>();
      profanityFilter = new ProfanityFilter();
    }
    return profanityFilter;
  }

  private static boolean containsWord(String word) {
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

  private void addWords() {
    wordSet.add("fuck");
    wordSet.add("ass");
    wordSet.add("bitch");
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
