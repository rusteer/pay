package com.thumb.payapi.utils;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class CommonUtils {
    private static String getPasswordByPackageName(String packageName) {
        if (packageName.length() >= 16) {
            return packageName.substring(0, 16);
        } else {
            while (packageName.length() < 16) {
                packageName += "w";
            }
            return packageName;
        }
    }
    public static String getPassword(Context context) {
        return getPasswordByPackageName(context.getPackageName());
    }
    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageinfo.versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception) {
            return -1;
        }
    }
    public static String getApplicationName(Activity activity) {
        try {
            PackageManager packageManager = activity.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(activity.getPackageName(), 0);
            return (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return null;
    }
    public static String getDecodedRString(Context context, String key) {
        String password = getPassword(context);
        String encodedValue = ResourceUtils.getRString(context, key);
        return AES.decode(encodedValue, password);
    }
    public static void sleep(long time) {
        if (time > 0) try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            MyLogger.error(e);
        }
    }
}
