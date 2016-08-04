package com.thumb.payapi.shuairui;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import com.mj.jar.pay.BillingListener;
import com.mj.jar.pay.MjPaySDK;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class ShuairuiPay {
    private static final String sdkName = EncodedSdkConstants.SHUAIRUI;
    private static MjPaySDK sdk;
    private static String payInfoArray[] = null;
    private static PayCallback CALLBACK = null;
    private static String PAY_INFO = null;
    private static Object locker = new Object();
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + "2.db"));
            String appId = obj.getString("appId");
            String distro = "";
            String fm = obj.getInt("packageId")+"";
            sdk = new MjPaySDK(activity, new BillingListener() {
                @Override
                public void onBillingResult(int statusCode, Bundle bundle) {
                    synchronized (locker) {
                        if (CALLBACK != null) {
                            int result = Pay.PAY_FAILED;
                            if (statusCode == 2000) result = Pay.PAY_SUCCESS;
                            else if (statusCode == 2001) result = Pay.PAY_CANCEL;
                            CALLBACK.onResult(result, String.valueOf(statusCode), PAY_INFO);
                            CALLBACK = null;
                        }
                    }
                }
                @Override
                public void onInitResult(int statusCode) {}
            }, appId, distro, fm);
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    public static void pay(Activity activity, int payIndex, PayCallback callback) {
        synchronized (locker) {
            final String payInfo = payInfoArray[payIndex];
            final String[] fields = payInfo.split(",");
            String payCode = fields[3].trim();
            String price = fields[2].trim();
            CALLBACK = callback;
            PAY_INFO = payInfo;
            sdk.pay(String.valueOf(System.currentTimeMillis()), payCode, price);
        }
    }
}
