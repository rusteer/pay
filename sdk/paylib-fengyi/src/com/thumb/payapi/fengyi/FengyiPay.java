package com.thumb.payapi.fengyi;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import com.fengyi.gamesdk.service.MyPay;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class FengyiPay {
    private static final String TIMEOUT = "timeout";
    private static final String sdk = EncodedSdkConstants.FENGYI;
    private static String payInfoArray[] = null;
    public static void onApplicationCreate(Application application) {}
    public static void onApplicationTerminate(Application application) {}
    public static void init(final Activity activity) {
        try {
            String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdk + ".db");
            payInfoArray = payInfoContent.split("\n");
            MyPay.init(activity.getApplication());
        } catch (Throwable e) {
            MyLogger.error(e);
        }
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final int price = Integer.valueOf(payInfo.split(",")[2]);
        final PayCallback proxy = new PayCallback() {
            boolean invoked = false;
            @Override
            public void onResult(int result, String errMessage, String payInfo) {
                if (!invoked) {
                    callback.onResult(result, errMessage, payInfo);
                    invoked = true;
                }
            }
        };
        class MyReceiver extends BroadcastReceiver {
            private boolean unregisterInvoked = false;
            @Override
            public void onReceive(Context context, Intent intent) {
                unregister();
                Bundle bundle = intent.getExtras();
                boolean success = bundle.getInt("orderResult") == 0;
                proxy.onResult(success ? Pay.PAY_SUCCESS : Pay.PAY_FAILED, "", payInfo);
            }
            private void unregister() {
                if (!unregisterInvoked) {
                    activity.unregisterReceiver(this);
                    unregisterInvoked = true;
                }
            }
        }
        //注册receiver
        final MyReceiver receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.fengyi.gamesdk.service.feenotify");
        activity.registerReceiver(receiver, filter);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyPay.pay(activity, price / 100);
            }
        });
        new Handler(activity.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                receiver.unregister();
                proxy.onResult(Pay.PAY_FAILED, TIMEOUT, payInfo);
            }
        }, 20L * 1000);
    }
}
