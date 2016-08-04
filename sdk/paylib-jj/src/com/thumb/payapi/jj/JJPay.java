package com.thumb.payapi.jj;
import android.content.Context;
import com.pjsskj.pay.OnPayListener;
import com.pjsskj.pay.PaySDK;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;

public class JJPay {
    private static String payInfoArray[] = null;
    public static void init(final Context context) {
        PaySDK.getInstance(context).init();
        String payInfoContent = FileUtils.readEncodedAssetFile(context, "pi/" + EncodedSdkConstants.JJ + ".db");
        payInfoArray = payInfoContent.split("\n");
    }
    public static void pay(final Context context, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        String productName = fields[1];
        int price = Integer.valueOf(fields[2]);
        PaySDK sdk = PaySDK.getInstance(context);
        sdk.setPayListener(new OnPayListener() {
            @Override
            public void onPaidResult(int statusCode, String msg) {
                callback.onResult(statusCode == 0 ? Pay.PAY_SUCCESS : Pay.PAY_FAILED, msg, payInfo);
            }
            @Override
            public void onCancelPaying() {
                callback.onResult(Pay.PAY_CANCEL, null, payInfo);
            }
        });
        sdk.pay(productName, price);
    }
}
