package com.thumb.payapi.hurray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.hurray2pay.InvokeActivity;
import com.hurray.hurrayanalysis.HurrayAnalysis;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class HurrayPay {
    private static String payInfoArray[] = null;
    private static String productId = null;
    private static int channelId =0;
    public static void init(final Activity context) {
        String payInfoContent = FileUtils.readEncodedAssetFile(context, "pi/" + EncodedSdkConstants.HURRAY + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(context, "pi/" + EncodedSdkConstants.HURRAY + "2.db"));
            productId = obj.getString("productId");
            channelId=obj.optInt("channelId");
            HurrayAnalysis.HyonCreate(context, channelId+"", productId);
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    public static void pay(final Context context, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        String consumeCode = fields[3].trim();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("consumeCode", consumeCode);
        intent.putExtras(bundle);
        intent.setClass(context, InvokeActivity.class);
        InvokeActivity.callback = callback;
        InvokeActivity.payInfo = payInfo;
        context.startActivity(intent);
    }
}
