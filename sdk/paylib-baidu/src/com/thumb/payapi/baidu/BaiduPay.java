package com.thumb.payapi.baidu;
import java.security.MessageDigest;
import java.util.Random;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import com.baidu.opengame.sdk.BDGameSDK;
import com.baifubao.mpay.ifmgr.ILoginCallback;
import com.baifubao.mpay.ifmgr.ILoginCallbackEx;
import com.baifubao.mpay.ifmgr.IPayResultCallback;
import com.baifubao.mpay.tools.PayRequest;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class BaiduPay {
    private static boolean payAsync = false;
    private static boolean allowThirdPartyLogin = true;
    private static String payInfoArray[] = null;
    private static boolean initialized = false;
    private static String appId = null;
    private static String appKey = null;
    private static boolean isLandScape = false;
    //private static String notifiyUrl = null;
    private static long lastLoginTime = 0;
    private static final long loginActivateSeconds = 60 * 9;
    public static void pay(final Activity activity, int payIndex, final PayCallback callback) {
        if (!initialized) init(activity);
        final String payInfo = payInfoArray[payIndex];
        if ((System.currentTimeMillis() - lastLoginTime) / 1000 < loginActivateSeconds) {
            startPay(activity, payInfo, callback);
        } else {
            login(activity, new LoginCallback() {
                @Override
                public void onResult(boolean success, String errorMessage) {
                    if (success) {
                        startPay(activity, payInfo, callback);
                    } else {
                        callback.onResult(2, errorMessage, payInfo);
                    }
                }
            });
        }
    }
    //public static final String appid = "1194026";
    //public static final String appkey = "fzXxMdPFceFLrufFyB03f9pe";
    //public static final String notifyurl = "http://baidupay.iapppay.com:5081/";
    public static void init(Activity activity) {
        try {
            String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.BAIDU + ".db");
            payInfoArray = payInfoContent.split("\n");
            //
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.BAIDU + "2.db"));
            appId = obj.getString("appId");
            appKey = obj.getString("appKey");
            isLandScape = obj.getBoolean("isLandScape");
            payAsync = obj.optBoolean("payAsync");
            //notifiyUrl = obj.optString("notifiyUrl");
            /*
            payInfoArray = new String[] { "1,20钻石,200", "2,70钻石,600", "3,140钻石,1000", "4,330钻石,1500", "5,500钻石,3000", "6,首充礼包,200", "7,豪华礼包,1500", "8,过关礼包,200", };
            //测试ID
            //appId = "1194026";
            //appKey = "fzXxMdPFceFLrufFyB03f9pe";
            //粉碎传奇账号
            appId = "4654382";
            appKey = "QZV1X6afIM7zGtSfdcUcgG66";
            //
            //notifiyUrl = "http://baidupay.iapppay.com:5081/";
            isLandScape = false;
            payAsync = false;
            */
            BDGameSDK.getInstance().init(activity, isLandScape ? 0 : 1, appId, appKey);
            initialized = true;
        } catch (Exception e) {
            MyLogger.error(e);
            return;
        }
    }
    private interface LoginCallback {
        void onResult(boolean success, String error);
    }
    private static void startPay(final Activity activity, final String payInfo, final PayCallback callback) {
        new Handler(activity.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                PayRequest payRequest = new PayRequest();
                final int price = Integer.valueOf(payInfo.split(",")[2]);
                //if (!TextUtils.isEmpty(notifiyUrl)) {
                //    payRequest.addParam("notifyurl", notifiyUrl);
                //}
                payRequest.addParam("appid", appId);
                payRequest.addParam("waresid", 1);
                payRequest.addParam("exorderno", genExorderno());
                payRequest.addParam("price", price);
                payRequest.addParam("cpprivateinfo", "id:" + new Random().nextInt(Integer.MAX_VALUE) + "");
                if (payAsync) payRequest.addParam("asyncflag", 1);
                String paramUrl = payRequest.genSignedUrlParamString(appKey);
                BDGameSDK.getInstance().startPay(activity, paramUrl, new IPayResultCallback() {
                    @Override
                    public void onPayResult(int resultCode, String signValue, String resultInfo) {// resultInfo = 应用编号&商品编号&外部订单号
                        if (BDGameSDK.PAY_SUCCESS == resultCode) {
                            if (null == signValue) {
                                MyLogger.error("signValue is null ");
                                callback.onResult(Pay.PAY_FAILED, "没有签名值", payInfo);
                                return;
                            } else {
                                if (PayRequest.isLegalSign(signValue, resultInfo, appKey)) {
                                    callback.onResult(Pay.PAY_SUCCESS, "支付成功", payInfo);
                                } else {
                                    callback.onResult(Pay.PAY_FAILED, "支付成功，但是验证签名失败", payInfo);// 非法签名值，默认采用finish()，请根据需要修改
                                }
                            }
                            return;
                        } else if (BDGameSDK.PAY_CANCEL == resultCode) {
                            callback.onResult(Pay.PAY_CANCEL, "取消支付", payInfo);
                            return;
                        } else if (BDGameSDK.PAY_HANDLING == resultCode) {
                            callback.onResult(Pay.PAY_FAILED, "支付处理中:" + resultInfo, payInfo); // 如果返回支付正在处理，请通过您的服务器查询
                        } else {
                            callback.onResult(Pay.PAY_FAILED, "支付失败:" + resultCode + "," + resultInfo, payInfo); // 计费失败处理，默认采用finish()，请根据需要修改
                        }
                    }
                });
            }
        });
    }
    private static void login(final Activity activity, final LoginCallback callback) {
        new Handler(activity.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final String time = System.currentTimeMillis() + "";
                final String sign = MD5(Sign(appId, appKey, time));
                BDGameSDK.getInstance().loginUIEx(activity, appId, time, sign, new ILoginCallbackEx() {
                    @Override
                    public void onCallBack(int retcode, String token, String uid, String uname, String sign) {
                        Log.d("zhy", "retcode:" + retcode + "token:" + token + "uid:" + uid + "uname:" + uname + "sign:" + sign);
                        if (retcode == ILoginCallback.RETCODE_SUCCESS) {
                            String orderEnc = MD5(Sign_Login_Callback_Ex(appId, appKey, time, token));
                            if (sign.equalsIgnoreCase(orderEnc)) {
                                //Toast.makeText(activity, "APP获取Token结果:" + token + "成功,验证签名成功！", Toast.LENGTH_LONG).show();
                                lastLoginTime = System.currentTimeMillis();
                                callback.onResult(true, null);
                            } else {
                                //Toast.makeText(activity, "APP获取Token结果:" + token + "成功,验证签名失败！", Toast.LENGTH_LONG).show();
                                callback.onResult(false, "验证签名失败！");
                            }
                        } else if (retcode == ILoginCallback.RETCODE_FAIL) {
                            //Toast.makeText(activity, "APP获取token失败", Toast.LENGTH_LONG).show();
                            callback.onResult(false, "APP获取token失败");
                        }
                    }
                }, allowThirdPartyLogin, false);
            }
        });
    }
    /**
     * 签名拼写
     * 
     * @param appid
     * @param appKey
     * @param CurrentTime
     * @return
     */
    private static String Sign(String appid, String appKey, String CurrentTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(appid).append(appKey).append(CurrentTime);
        String unSignValue = sb.toString();
        return unSignValue;
    }
    /**
     * 签名拼写
     * 
     * @param appid
     * @param appKey
     * @param CurrentTime
     * @return
     */
    private static String Sign_Login_Callback_Ex(String appid, String appKey, String CurrentTime, String token) {
        StringBuilder sb = new StringBuilder();
        // 按照升序排列
        sb.append(appid).append(appKey).append(CurrentTime).append(token);
        String unSignValue = sb.toString();
        Log.e("tag", "unSignValue:" + unSignValue);
        return unSignValue;
    }
    // MD5加密，32位
    private static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    private static String genExorderno() {
        return String.valueOf(System.currentTimeMillis());
    }
}
