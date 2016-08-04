package com.thumb.payapi.letu;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.lyhtgh.pay.SdkPayServer;
import com.lyhtgh.pay.application.PayApplication;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;
import com.thumb.payapi.utils.ResourceUtils;

public class LetuPay {
    private static final String sdkName = EncodedSdkConstants.LETU;
    private static String payInfoArray[] = null;
    private static String payAppId = null;
    private static String merchantId = null;
    private static String packageId = null;
    private static String merchantPasswd = null;
    static SdkPayServer mSkyPayServer = null;
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + "2.db"));
            payAppId = obj.getString("appId");
            merchantId = obj.getString("merchantId");
            merchantPasswd = obj.getString("merchantPasswd");
            packageId = obj.getString("packageId");
            sdkInit(activity);
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    private static void sdkInit(final Activity activity) {
        SdkPayServer.setPayStartDataInfo(activity, merchantId, payAppId, packageId);
        mSkyPayServer = SdkPayServer.getInstance();
        mSkyPayServer.initSdkPayServer();
    }
    public static void onApplicationCreate(Application application) {
        new PayApplication().a(application.getApplicationContext());
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        String payPrice = fields[2].trim();
        String name = fields[1].trim();
        String orderId = payAppId + "-" + SystemClock.elapsedRealtime();
        String appVer = "1000"; //应用版本号
        String appName = ResourceUtils.getRString(activity, "app_name");// "测试"; //应用名称
        String pointNum = String.valueOf(payIndex + 1); //计费点编号
        String orderDesc = "购买" + name + ",本次付费需X.XX元";
        String productName = name;
        String channelName = packageId;
        String payType = "1";
        String gameType = "0";
        String sig = mSkyPayServer.getSignature(merchantPasswd, //
                SdkPayServer.ORDER_INFO_ORDER_ID, orderId, //
                SdkPayServer.ORDER_INFO_MERCHANT_ID, merchantId, //
                SdkPayServer.ORDER_INFO_APP_ID, payAppId, //
                SdkPayServer.ORDER_INFO_APP_VER, appVer, //
                SdkPayServer.ORDER_INFO_APP_NAME, appName, //
                SdkPayServer.ORDER_INFO_PAYPOINT, pointNum, //
                SdkPayServer.ORDER_INFO_PAY_PRICE, payPrice, //
                SdkPayServer.ORDER_INFO_PRODUCT_NAME, productName, //
                SdkPayServer.ORDER_INFO_ORDER_DESC, orderDesc, //
                SdkPayServer.ORDER_INFO_CP_CHANNELID, packageId, //
                SdkPayServer.ORDER_INFO_SDK_CHANNELID, channelName, //
                SdkPayServer.ORDER_INFO_PAY_TYPE, payType,// 
                SdkPayServer.ORDER_INFO_GAME_TYPE, gameType//
                );
        String orderInfo = SdkPayServer.ORDER_INFO_ORDER_ID + "=" + orderId + "&" + //
                SdkPayServer.ORDER_INFO_MERCHANT_ID + "=" + merchantId + "&" + //
                SdkPayServer.ORDER_INFO_APP_ID + "=" + payAppId + "&" + //
                SdkPayServer.ORDER_INFO_APP_VER + "=" + appVer + "&" + //
                SdkPayServer.ORDER_INFO_APP_NAME + "=" + appName + "&" + //
                SdkPayServer.ORDER_INFO_PAYPOINT + "=" + pointNum + "&" + //
                SdkPayServer.ORDER_INFO_PAY_PRICE + "=" + payPrice + "&" + //
                SdkPayServer.ORDER_INFO_PRODUCT_NAME + "=" + productName + "&" + //
                SdkPayServer.ORDER_INFO_ORDER_DESC + "=" + orderDesc + "&" + //
                SdkPayServer.ORDER_INFO_CP_CHANNELID + "=" + packageId + "&" + //
                SdkPayServer.ORDER_INFO_SDK_CHANNELID + "=" + channelName + "&" + //
                SdkPayServer.ORDER_INFO_PAY_TYPE + "=" + payType + "&" + //
                SdkPayServer.ORDER_INFO_GAME_TYPE + "=" + gameType + "&" + //
                SdkPayServer.ORDER_INFO_MERCHANT_SIGN + "=" + sig + "&" + //
                SdkPayServer.ORDER_INFO_SHOW_PAYUIKEY + "=" + "false"; //       
        Handler mPayHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == SdkPayServer.MSG_WHAT_TO_APP) {
                    String retInfo = (String) msg.obj;
                    // mHinTextView.setText(mHinTextView.getText() + "\n" + retInfo);
                    String[] keyValues = retInfo.split("&|=");
                    Map<String, String> resultMap = new HashMap<String, String>();
                    for (int i = 0; i < keyValues.length; i = i + 2) {
                        resultMap.put(keyValues[i], keyValues[i + 1]);
                    }
                    String payResult = resultMap.get(SdkPayServer.PAYRET_KEY_RESULT_STATUS);
                    boolean success = false;
                    String errorMessage = null;
                    if (null != payResult && Integer.parseInt(payResult) == SdkPayServer.PAY_RESULT_SUCCESS) {
                        success = true;
                    } else {
                        errorMessage = resultMap.get(SdkPayServer.PAYRET_KEY_FAILED_CODE);
                    }
                    callback.onResult(success ? Pay.PAY_SUCCESS : Pay.PAY_FAILED, errorMessage, payInfo);
                }
            }
        };
        mSkyPayServer.startSdkServerPay(activity, mPayHandler, orderInfo);
    }
}
