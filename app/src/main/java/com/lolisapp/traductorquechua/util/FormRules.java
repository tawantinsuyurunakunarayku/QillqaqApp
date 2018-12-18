package com.lolisapp.traductorquechua.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pc1 on 11/07/2016.
 */
public class FormRules {



    public static int getMaxLength(TYPE_DOCUMENT type) {
        int length = 8;
        switch (type) {
            case DNI:
                length = 8;
                break;
            case IDENTITY_CARD:
                length = 12;
                break;
            case ALIEN_CARD:
                length = 12;
                break;
            case PASSPORT:
                length = 12;
                break;
        }
        return length;
    }

    public static boolean validateEmail(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public enum TYPE_DOCUMENT {
        DNI, IDENTITY_CARD, ALIEN_CARD, PASSPORT
    }
}
