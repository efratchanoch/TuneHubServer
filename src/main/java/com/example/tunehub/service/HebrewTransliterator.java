package com.example.tunehub.service;

import java.util.HashMap;
import java.util.Map;


public class HebrewTransliterator {

    private static final Map<Character, String> MAP = new HashMap<>();
    static {
        MAP.put('א', "A"); MAP.put('ב', "B"); MAP.put('ג', "G"); MAP.put('ד', "D");
        MAP.put('ה', "H"); MAP.put('ו', "V"); MAP.put('ז', "Z"); MAP.put('ח', "CH");
        MAP.put('ט', "T"); MAP.put('י', "Y"); MAP.put('כ', "K"); MAP.put('ך', "K");
        MAP.put('ל', "L"); MAP.put('מ', "M"); MAP.put('ם', "M"); MAP.put('נ', "N");
        MAP.put('ן', "N"); MAP.put('ס', "S"); MAP.put('ע', "A"); MAP.put('פ', "P");
        MAP.put('ף', "P"); MAP.put('צ', "TZ"); MAP.put('ץ', "TZ"); MAP.put('ק', "K");
        MAP.put('ר', "R"); MAP.put('ש', "SH"); MAP.put('ת', "T");
        // vowels/niqqud mostly ignored; you can expand mapping if needed
    }

    public static boolean containsHebrew(String s) {
        if (s == null) return false;
        for (char c : s.toCharArray()) {
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HEBREW) return true;
        }
        return false;
    }

    public static String transliterateLetterByLetter(String hebrew) {
        if (hebrew == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : hebrew.toCharArray()) {
            if (MAP.containsKey(c)) {
                sb.append(MAP.get(c));
            } else {
                // keep ASCII letters and spaces as is (for safety)
                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ') {
                    sb.append(c);
                } else {
                    // any other char -> try to preserve as is or replace with empty
                    // keep spaces
                    if (Character.isWhitespace(c)) sb.append(' ');
                }
            }
        }
        // normalize whitespace
        return sb.toString().replaceAll("\\s+", " ").trim();
    }
}
