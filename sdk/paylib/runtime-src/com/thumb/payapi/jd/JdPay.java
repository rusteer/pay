package com.thumb.payapi.jd;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.thumb.payapi.Pay.CheckExitCallback;
import com.thumb.payapi.Pay.PayCallback;

/**
 * 基地SDK
 * @author admin
 *
 */
public class JdPay {
    public static void init(Activity activity) {}
    public static void pay(Activity activity, int payIndex, final PayCallback callback) {}
    public static Boolean isMusicOn(Context context) {
        return null;
    }
    public static void checkExit(Context context, final CheckExitCallback callback) {}
    public static void onApplicationCreate(Application application) {}
}
