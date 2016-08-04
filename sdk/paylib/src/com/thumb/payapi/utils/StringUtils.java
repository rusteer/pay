package com.thumb.payapi.utils;
public class StringUtils {
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }
    public static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }
    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }
    
    public static int toInt(String s){
        if(isNotBlank(s)){
            try {
                return Integer.valueOf(s.trim());
            } catch ( Exception e) {
                MyLogger.error(e);
            }
        }
        return -1;
    }
    
}
