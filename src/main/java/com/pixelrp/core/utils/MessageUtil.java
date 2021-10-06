package com.pixelrp.core.utils;

public class MessageUtil {

    public static String replaceIfOdd(String stringToChange, String searchingWord, String replacingWord)
    {
        String separator = "#######";
        String splittingString = stringToChange.replaceAll(searchingWord, "#######" + searchingWord);

        String[] splitArray = splittingString.split("#######");
        String result = "";
        for (int i = 0; i < splitArray.length; i++) {
            if (i % 2 == 1) {
                splitArray[i] = splitArray[i].replace(searchingWord, replacingWord);
            }
            result = result + splitArray[i];
        }
        return result;
    }

    public static String replaceIfEven(String stringToChange, String searchingWord, String replacingWord) {
        String separator = "#######";
        String splittingString = stringToChange.replaceAll(searchingWord, "#######" + searchingWord);

        String[] splitArray = splittingString.split("#######");
        String result = "";
        for (int i = 0; i < splitArray.length; i++) {
            if (i % 2 == 0) {
                splitArray[i] = splitArray[i].replace(searchingWord, replacingWord);
            }
            result = result + splitArray[i];
        }
        return result;
    }

}
