package com.thumb.payapi.zpay;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;
import com.zhangzhifu.sdk.ZhangPayCallback;
import com.zhangzhifu.sdk.ZhangPaySdk;

public class ZPay {
    private static final boolean showProcess = true;
    private static final String sdkName = EncodedSdkConstants.ZPAY;
    private static String payInfoArray[] = null;
    private static String appId = null;
    private static String channelId = null;
    private static String qd = null;
    private static String key = null;
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + "2.db"));
            appId = obj.getString("appId");
            channelId = obj.getString("channelId");
            qd = obj.getString("qd");
            key = obj.getString("key");
            ZhangPaySdk.getInstance().init(activity, channelId, appId, qd);
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        int price = Integer.valueOf(fields[2].trim());
        String name = fields[1].trim();
        String pointId = fields[3].trim();
        Map<String, String> map = new HashMap<String, String>();
        map.put("channelId", channelId);
        map.put("key", key);
        map.put("priciePointId", pointId);
        map.put("money", String.valueOf(price));
        map.put("cpparam", String.valueOf(System.currentTimeMillis()));
        map.put("appId", appId);
        map.put("qd", qd);
        map.put("tradeName", name);
        ZhangPaySdk.getInstance().pay(activity, map, new ZhangPayCallback() {
            private boolean invoked;
            @Override
            public void onZhangPayBuyProductOK(String priciePointId, String okCode) {
                if (!invoked) {
                    invoked = true;
                    callback.onResult(Pay.PAY_SUCCESS, null, payInfo);
                }
            }
            @Override
            public void onZhangPayBuyProductFaild(String priciePointId, String errorCode) {
                if (!invoked) {
                    invoked = true;
                    callback.onResult(Pay.PAY_FAILED, errorCode, payInfo);
                }
            }
        }, showProcess);// pay接口为耗时操作请新开线程，此处false为不调用sdk提供的loading。如果为true就调用
    }
}
