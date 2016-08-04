package com.thumb.payapi.pz;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import com.hzpz.pay.data.CheckOrder;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class PzPay {
    private static String appId = null;// "1122"; // 应用编号
    private static final String sdkName = EncodedSdkConstants.EHOO;
    private static String payInfoArray[] = null;
    private static boolean initSuccess = false;
    private static com.hzpz.pay.PzPay sdk = null;
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + "2.db"));
            appId = obj.getString("appId");
            initSdk(activity, new InitCallback() {
                @Override
                public void onResult(boolean success) {
                    initSuccess = success;
                }
            });
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    private static interface InitCallback {
        void onResult(boolean success);
    }
    // PaySDK初始化 ：此初始化只需在应用启动时调用一次即可，无需计费时重复调用
    private static void initSdk(final Activity activity, final InitCallback callback) {
        sdk = com.hzpz.pay.PzPay.getInstanct(activity, appId, 10001, null, new com.hzpz.pay.PzPay.PzPayListener() {
            @Override
            public void onPayFinished(boolean successed, CheckOrder msg) {
                callback.onResult(successed && msg != null);
            }
        });
    }
    public static void onMainActivityDestroy(Context context) {
        sdk.unregisterPayListener();
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        if (initSuccess) {
            doPay(activity, payIndex, callback);
        } else {
            initSdk(activity, new InitCallback() {
                @Override
                public void onResult(boolean success) {
                    if (success) {
                        initSuccess = true;
                        doPay(activity, payIndex, callback);
                    } else {
                        callback.onResult(com.thumb.payapi.Pay.PAY_FAILED, "init-error", payInfoArray[payIndex]);
                    }
                }
            });
        }
    }
    private static class MyTimer {
        long timeout = 30;
        long start;
        private boolean end;
        boolean isEnd() {
            return end || (System.currentTimeMillis() - start) > timeout * 1000;
        }
        public MyTimer() {
            start = System.currentTimeMillis();
        }
    }
    private static void doPay(final Activity activity, final int payIndex, final PayCallback callback) {
        String orderId = UUID.randomUUID().toString();
        final String payInfo = payInfoArray[payIndex];
        int price = Integer.valueOf(payInfo.split(",")[2].trim());
        sdk.pay(price, orderId, orderId);
        class PayCallbackProxy implements PayCallback {
            private boolean invoked;
            @Override
            public void onResult(int result, String errMessage, String payInfo) {
                synchronized (this) {
                    if (invoked) return;
                    invoked = true;
                }
                callback.onResult(result, errMessage, payInfo);
            }
        }
        final PayCallbackProxy proxy = new PayCallbackProxy();
        final MyTimer timer = new MyTimer();
        while (!timer.isEnd()) {
            sdk.queryOrder(orderId, new com.hzpz.pay.PzPay.OnQueryOrderListListener() {
                @Override
                public void onQueryFinished(CheckOrder data) {
                    if (data != null) {
                        timer.end = true;
                        int result = Pay.PAY_FAILED;
                        if ("0".equals(data.status)) {
                            result = Pay.PAY_SUCCESS;
                        } else if ("34".equals(data.status)) {
                            result = Pay.PAY_CANCEL;
                        }
                        proxy.onResult(result, "", payInfo);
                    }
                }
            });
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        proxy.onResult(Pay.PAY_FAILED, "timeout", payInfo);
    }
}
