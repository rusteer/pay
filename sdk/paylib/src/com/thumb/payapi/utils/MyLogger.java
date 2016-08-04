package com.thumb.payapi.utils;
import android.util.Log;

public class MyLogger {
    private static final String API = /*const-replace-start*/"papiiiiii";
    public static void error(Throwable e) {
        Log.e(API, e.getMessage(), e);
    }
    public static void info(Object o) {
        if (o != null) {
            Log.i(API, o.toString());
        }
    }
    public static void error(Object o) {
        if (o != null) {
            Log.e(API, o.toString());
        }
    }
}
