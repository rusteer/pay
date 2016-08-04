package com.thumb.payapi.dianwo;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.qy.pay.listener.PayAgent;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;

public class DianwoPay {
    private static final String sdkName = EncodedSdkConstants.DIANWO;
    private static String payInfoArray[] = null;
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + ".db");
        payInfoArray = payInfoContent.split("\n");
        PayAgent.init(activity);
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        int money = Integer.valueOf(fields[2]);
        String pointNum = fields[3];
        PayAgent.pay(activity, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String errMessage = null;
                boolean success = false;
                Bundle bundle = msg.getData();
                if (bundle != null) {
                    success = bundle.getInt("code", -1) == 0;
                    errMessage = bundle.getString("msg");
                }
                callback.onResult(success ? Pay.PAY_SUCCESS : Pay.PAY_FAILED, errMessage, payInfo);
            }
        }, pointNum, money);
    }
}
