package com.pay.admin.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static String format(Date date) {
        return dateFormat.format(date);
    }
}
