package com.thumb.payapi.demo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;

public class DemoPay {
    private static String payInfoArray[] = null;
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.DEMO + ".db");
        payInfoArray = payInfoContent.split("\n");
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        new AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_alert).setMessage(payInfo).setTitle("付费演示").setPositiveButton("模拟成功", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                callback.onResult(Pay.PAY_SUCCESS, null, payInfo);
            }
        }).setNegativeButton("模拟失败", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                callback.onResult(Pay.PAY_CANCEL, "cancel", payInfo);
            }
        }).create().show();
    }
}
