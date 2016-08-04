package com.thumb.payapi.free;
import android.app.Activity;
import android.content.Context;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class FreePay {
    private static String payInfoArray[] = null;
    public static void init(final Context context) {
        try {
            String payInfoContent = FileUtils.readEncodedAssetFile(context, "pi/" + EncodedSdkConstants.FREE+ ".db");
            payInfoArray = payInfoContent.split("\n");
        } catch (Throwable e) {
            MyLogger.error(e);
        }
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String payInfo = payInfoArray != null && payIndex < payInfoArray.length ? payInfoArray[payIndex] : "";
                callback.onResult(Pay.PAY_SUCCESS, "free", payInfo);
            }
        });
    }
}
