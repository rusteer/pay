package com.thumb.payapi.jd;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import cn.cmgame.billing.api.BillingResult;
import cn.cmgame.billing.api.GameInterface;
import cn.cmgame.billing.api.GameInterface.GameExitCallback;
import cn.cmgame.billing.api.GameInterface.IPayCallback;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.CheckExitCallback;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;

/**
 * 基地SDK
 * @author admin
 *
 */
public class JdPay {
    private static String payInfoArray[] = null;
    private static boolean inited = false;
    public static void init(Activity activity) {
        if (!inited) {
            GameInterface.initializeApp(activity);
            String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.JD + ".db");
            payInfoArray = payInfoContent.split("\n");
            inited = true;
        }
    }
    public static void pay(Activity activity, int payIndex, final PayCallback callback) {
        if (inited) {
            final String payInfo = payInfoArray[payIndex];
            String[] fields = payInfo.split(",");
            String payCode = fields[3];
            boolean isRepeat = Boolean.valueOf(fields[4]);
            final IPayCallback jdCallback = new IPayCallback() {
                @Override
                public void onResult(int resultCode, String billingIndex, Object obj) {
                    int result = Pay.PAY_CANCEL;
                    if (resultCode == BillingResult.SUCCESS) result = Pay.PAY_SUCCESS;
                    else if (resultCode == BillingResult.FAILED) result = Pay.PAY_FAILED;
                    callback.onResult(result, null, payInfo);
                }
            };
            GameInterface.doBilling(activity, true, isRepeat, payCode, null, jdCallback);
        }
    }
    public static Boolean isMusicOn(Context context) {
        return GameInterface.isMusicEnabled();
    }
    public static void checkExit(Context context, final CheckExitCallback callback) {
        GameInterface.exit(context, new GameExitCallback() {
            @Override
            public void onConfirmExit() {
                callback.onResult(true);
            }
            @Override
            public void onCancelExit() {
                callback.onResult(false);
            }
        });
    }
    public static void onApplicationCreate(Application application) {}
}
