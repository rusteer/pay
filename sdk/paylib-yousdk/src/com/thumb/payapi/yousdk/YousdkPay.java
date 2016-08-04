package com.thumb.payapi.yousdk;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import com.flyplay.pp.InitCallback;
import com.flyplay.pp.Pay;
import com.flyplay.pp.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class YousdkPay {
    private static final String sdkName = EncodedSdkConstants.HYPAY;
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
            Pay.init(activity, appId, channelId, new InitCallback() {
                @Override
                public void onResult(int arg0, String arg1) {}
            });
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    public static void pay(final Activity activity, final int payIndex, final com.thumb.payapi.Pay.PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        int price = Integer.valueOf(fields[2]);
        Pay.pay(activity, price, new PayCallback() {
            @Override
            public void onResult(int result, String message) {
                callback.onResult(result, message, payInfo);
            }
        });
    }
}
