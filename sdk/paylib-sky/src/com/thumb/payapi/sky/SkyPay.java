package com.thumb.payapi.sky;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.skymobi.pay.app.PayApplication;
import com.skymobi.pay.sdk.SkyPayServer;
import com.skymobi.pay.sdk.SkyPaySignerInfo;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.CommonUtils;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class SkyPay {
    private static boolean useAppUI = true;
    private static Handler handler = null;
    private static SkyPayServer mSkyPayServer = null;
    static String merchantId = null;
    static String merchantSign = null;
    static String systemId = null;
    static String appId = null;
    static String channelName = null;
    static String payMethod = "sms";//
    static String gameType = "0";//0：单机 , 1：联网 ,2：弱联网
    static String payType = "1";//0=注册 1=道具 2=积 分 3=充值 
    private static String payInfoArray[] = null;
    private static boolean inited = false;
    public static void init(Context context) {
        try {
            String payInfoContent = FileUtils.readEncodedAssetFile(context, "pi/" + EncodedSdkConstants.SKY + ".db");
            payInfoArray = payInfoContent.split("\n");
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(context, "pi/" + EncodedSdkConstants.SKY + "2.db"));
            merchantId = obj.optString("merchantId");
            merchantSign = obj.optString("merchantSign");
            systemId = obj.optString("systemId");
            appId = obj.optString("appId");
            channelName = obj.optString("channelName");
            inited = true;
        } catch (Throwable e) {
            MyLogger.error(e);
        }
    }
    public static void pay(Activity activity, int payIndex, PayCallback callback) {
        if (!inited) {
            init(activity);
        }
        final String payInfo = payInfoArray[payIndex];
        String[] fields = payInfo.split(",");
        //String id = fields[0];
        String id = String.valueOf(payIndex+1);
        String orderDesc = fields[3];
        String price = fields[2];
        doPay(activity, payInfo, id, price, orderDesc, callback);
    }
    public static void onApplicationCreate(Application application) {
        new PayApplication().applicationOnCreat(application.getApplicationContext());
    }
    private static void doPay(Activity activity, final String payInfo, String id, String price, String orderDesc, final PayCallback callback) {
        //订单参数
        final String ORDER_INFO_PAY_METHOD = "payMethod";
        final String ORDER_INFO_SYSTEM_ID = "systemId";
        final String ORDER_INFO_CHANNEL_ID = "channelId";
        final String ORDER_INFO_PAY_POINT_NUM = "payPointNum";
        final String ORDER_INFO_ORDER_DESC = "orderDesc";
        final String ORDER_INFO_GAME_TYPE = "gameType";
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == SkyPayServer.MSG_WHAT_TO_APP) {
                    String errmsg = (String) msg.obj;
                    Map<String, String> map = new HashMap<String, String>();
                    String[] keyValues = errmsg.split("&|=");
                    for (int i = 0; i < keyValues.length; i = i + 2) {
                        map.put(keyValues[i], keyValues[i + 1]);
                    }
                    if ("100".equals(map.get("msg_code")) && "102".equals(map.get("pay_status"))) {
                        callback.onResult(Pay.PAY_SUCCESS, errmsg, payInfo);
                        return;
                    }
                    callback.onResult(Pay.PAY_FAILED, errmsg, payInfo);
                }
            }
        };
        mSkyPayServer = SkyPayServer.getInstance();
        int initRet = mSkyPayServer.init(handler);
        if (SkyPayServer.PAY_RETURN_SUCCESS != initRet) {
            callback.onResult(Pay.PAY_FAILED, "服务正处于付费状态或 传入参数为空", null);
            return;
        }
        String orderId = SystemClock.elapsedRealtime() + "";
        SkyPaySignerInfo skyPaySignerInfo = new SkyPaySignerInfo();
        skyPaySignerInfo.setMerchantPasswd(merchantSign);
        skyPaySignerInfo.setMerchantId(merchantId);
        skyPaySignerInfo.setAppId(appId);
        skyPaySignerInfo.setAppName(CommonUtils.getApplicationName(activity));
        skyPaySignerInfo.setAppVersion(CommonUtils.getVersionCode(activity) + "");
        skyPaySignerInfo.setPayType(payType);
        skyPaySignerInfo.setPrice(price);
        skyPaySignerInfo.setOrderId(orderId);
        String signOrderInfo = mSkyPayServer.getSignOrderString(skyPaySignerInfo);
        String mOrderInfo = ""//
                + ORDER_INFO_PAY_METHOD + "=" + payMethod + "&" //
                + ORDER_INFO_SYSTEM_ID + "=" + systemId + "&"// 
                + ORDER_INFO_CHANNEL_ID + "=" + channelName + "&" //
                + ORDER_INFO_PAY_POINT_NUM + "=" + id + "&" //
                + ORDER_INFO_GAME_TYPE + "=" + gameType + "&" //
                + ORDER_INFO_ORDER_DESC + "=" + orderDesc + "&"; //
        if (useAppUI) {
            mOrderInfo += "useAppUI=true&";
        }
        mOrderInfo += signOrderInfo;
        int payRet = mSkyPayServer.startActivityAndPay(activity, mOrderInfo);
        if (SkyPayServer.PAY_RETURN_SUCCESS != payRet) {
            callback.onResult(Pay.PAY_FAILED, "调用接口失败" + payRet, payInfo);
            return;
        }
    }
}