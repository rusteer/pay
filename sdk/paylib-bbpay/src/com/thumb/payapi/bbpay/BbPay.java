package com.thumb.payapi.bbpay;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import com.thumb.payapi.Pay.InitCallback;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class BbPay {
    private static final String sdkName = EncodedSdkConstants.BBPAY;
    private static String payInfoArray[] = null;
    private static Long appId = null;
    private static int channelId = 0;
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + "2.db"));
            appId = obj.getLong("appId");
            channelId = obj.optInt("channelId");
            com.bbgames.util.Pay.init(activity, appId, channelId, new com.bbgames.util.InitCallback() {
                @Override
                public void onResult(int arg0, String arg1) {}
            });
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        int price = Integer.valueOf(fields[2]);
        com.bbgames.util.Pay.pay(activity, price, new com.bbgames.util.PayCallback() {
            @Override
            public void onResult(int result, String message) {
                callback.onResult(result, message, payInfo);
            }
        });
    }
}
