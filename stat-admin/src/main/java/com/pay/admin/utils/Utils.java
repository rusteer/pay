package com.pay.admin.utils;
public class Utils {
    public static String generatePassword(String seedBase) {
        if (seedBase != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(seedBase.hashCode());
            while (sb.length() < 16) {
                sb.append(sb.toString().hashCode());
            }
            return sb.substring(0, 16);
        }
        return null;
    }
    
   
}
