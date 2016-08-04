package com.thumb.payapi.myepay;
import android.app.Activity;
import android.content.Context;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.Pay.TaskCallback;
import com.thumb.payapi.utils.CommonUtils;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.InfoUtils;
import com.zywx.myepay.MyEPay;
import com.zywx.myepay.OnDefrayListener;

public class MyEpayPay {
    private static MyEPay def = MyEPay.getInstance();
    private static boolean sdkInitSuccess = false;
    private static String[] payInfoArray = null;
    private static void sdkInit(final Context context, final int tryCount, final long sleepTime, final TaskCallback taskCallback) {
        def.init(context, new OnDefrayListener() {
            @Override
            public void onInitFinished(int code) {
                if (code == 100 || code == 199) {
                    sdkInitSuccess = true;
                    if (taskCallback != null) taskCallback.onResult(true, null);
                } else {
                    String error = String.valueOf(code);
                    if ((tryCount - 1) > 0) {
                        CommonUtils.sleep(sleepTime);
                        sdkInit(context, tryCount - 1, sleepTime, taskCallback);
                    } else {
                        if (taskCallback != null) taskCallback.onResult(false, error);
                    }
                }
            }
            @Override
            public void onDefrayFinished(int arg0) {}
        });
    }
    public static void init(final Activity activity) {
        sdkInit(activity, 5, 2 * 1000, null);
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.MYEPAY + ".db");
        payInfoArray = payInfoContent.split("\n");
    }
    public static void pay(final Activity context, final int payIndex, final PayCallback callback) {
        TaskCallback taskCallback = new TaskCallback() {
            @Override
            public void onResult(boolean success, String errMessage) {
                if (success) {
                    final String payInfo = payInfoArray[payIndex];
                    int price = Integer.valueOf(payInfo.split(",")[2].trim());
                    String extId = String.valueOf(10000000 * (payIndex + 1) + InfoUtils.getInstantId(context));
                    def.startPay(context, price, null, extId, null, new OnDefrayListener() {
                        @Override
                        public void onDefrayFinished(int code) {
                            int result = 0 == code ? Pay.PAY_SUCCESS : Pay.PAY_FAILED;
                            callback.onResult(result, String.valueOf(code), payInfo);
                        }
                        @Override
                        public void onInitFinished(int arg0) {}
                    });
                } else {
                    callback.onResult(Pay.PAY_FAILED, errMessage, null);
                }
            }
        };
        if (sdkInitSuccess) {
            taskCallback.onResult(true, null);
        } else {
            sdkInit(context, 1, 0, taskCallback);
        }
    }
}
