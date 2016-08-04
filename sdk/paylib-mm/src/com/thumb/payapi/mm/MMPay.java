package com.thumb.payapi.mm;
import java.util.HashMap;
import mm.purchasesdk.OnPurchaseListener;
import mm.purchasesdk.Purchase;
import mm.purchasesdk.core.PurchaseCode;
import org.json.JSONObject;
import android.content.Context;
import com.chinaMobile.MobileAgent;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.Pay.TaskCallback;
import com.thumb.payapi.utils.CommonUtils;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

@SuppressWarnings("rawtypes")
public class MMPay {
    private static String payInfoArray[] = null;
    public static abstract class DefaultIAPListener implements OnPurchaseListener {
        @Override
        public void onAfterApply() {}
        @Override
        public void onAfterDownload() {}
        @Override
        public void onBeforeApply() {}
        @Override
        public void onBeforeDownload() {}
        @Override
        public void onInitFinish(int code) {}
        @Override
        public void onBillingFinish(int code, HashMap arg1) {}
        @Override
        public void onQueryFinish(int code, HashMap arg1) {}
        @Override
        public void onUnsubscribeFinish(int code) {}
    }
    private static Purchase purchase;
    private static boolean initialized = false;
    private static void sdkInit(final Context context, final int tryCount, final long sleepTime, final TaskCallback taskCallback) {
        purchase.init(context, new DefaultIAPListener() {
            @Override
            public void onInitFinish(int code) {
                if (code == PurchaseCode.INIT_OK) {
                    initialized = true;
                    if (taskCallback != null) taskCallback.onResult(true, null);
                } else {
                    String error = Purchase.getReason(code);
                    if ((tryCount - 1) > 0) {
                        CommonUtils.sleep(sleepTime);
                        sdkInit(context, tryCount - 1, sleepTime, taskCallback);
                    } else {
                        if (taskCallback != null) taskCallback.onResult(false, error);
                    }
                }
            }
        });
    }
    public static void init(Context context) {
        purchase = Purchase.getInstance();
        try {
            String payInfoContent = FileUtils.readEncodedAssetFile(context, "pi/" + EncodedSdkConstants.MM + ".db");
            payInfoArray = payInfoContent.split("\n");
            //
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(context, "pi/" + EncodedSdkConstants.MM + "2.db"));
            String mmId = obj.getString("appId");
            String mmKey = obj.getString("appKey");
            purchase.setAppInfo(mmId, mmKey);
            //
            sdkInit(context, 5, 2 * 1000, null);
        } catch (Exception e) {
            MyLogger.error(e);
            return;
        }
    }
    public static void pay(final Context context, final int payIndex, final PayCallback callback) {
        TaskCallback taskCallback = new TaskCallback() {
            @Override
            public void onResult(boolean success, String errMessage) {
                if (success) {
                    final String payInfo = payInfoArray[payIndex];
                    String payCode = payInfo.split(",")[3].trim();
                    purchase.order(context, payCode, new DefaultIAPListener() {
                        @Override
                        public void onBillingFinish(int code, HashMap arg1) {
                            int result = Pay.PAY_FAILED;
                            if (code == PurchaseCode.ORDER_OK || (code == PurchaseCode.AUTH_OK) || (code == PurchaseCode.WEAK_ORDER_OK)) {
                                result = Pay.PAY_SUCCESS;
                            } else if (code == PurchaseCode.BILL_CANCEL_FAIL) {
                                result = Pay.PAY_CANCEL;
                            }
                            callback.onResult(result, result == Pay.PAY_SUCCESS ? null : Purchase.getReason(code), payInfo);
                        }
                    });
                } else {
                    callback.onResult(Pay.PAY_FAILED, errMessage, null);
                }
            }
        };
        if (initialized) {
            taskCallback.onResult(true, null);
        } else {
            sdkInit(context, 1, 0, taskCallback);
        }
    }
    public static void onPause(Context context) {
        MobileAgent.onPause(context);
    }
    public static void onResume(Context context) {
        MobileAgent.onResume(context);
    }
}
