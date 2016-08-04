package com.thumb.payapi.yijie;
import android.app.Activity;
import android.content.Context;

import com.snowfish.android.ahelper.APaymentUnity;
import com.snowfish.cn.ganga.offline.helper.SFCommonSDKInterface;
import com.snowfish.cn.ganga.offline.helper.SFGameExitListener;
import com.snowfish.cn.ganga.offline.helper.SFIPayResultListener;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.CheckExitCallback;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;

public class YijiePay {
    private static String payInfoArray[] = null;
    public static void init(final Activity context) {
        SFCommonSDKInterface.onInit(context);
        String payInfoContent = FileUtils.readEncodedAssetFile(context, "pi/" + EncodedSdkConstants.YIJIE + ".db");
        payInfoArray = payInfoContent.split("\n");
    }
    public static String getUserId(Context context) {
        return APaymentUnity.getUserId(context) + "";
    }
    public static void pay(final Activity context, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        String payId = String.valueOf(payIndex);
        SFCommonSDKInterface.pay(context, payId, new SFIPayResultListener() {
            @Override
            public void onCanceled(String msg) {
                callback.onResult(Pay.PAY_CANCEL, msg, payInfo);
            }
            @Override
            public void onFailed(String msg) {
                callback.onResult(Pay.PAY_FAILED, msg, payInfo);
            }
            @Override
            public void onSuccess(String msg) {
                callback.onResult(Pay.PAY_SUCCESS, msg, payInfo);
            }
        });
    }
    public static void checkExit(Activity context, final CheckExitCallback callback) {
    	SFCommonSDKInterface.onExit(context, new SFGameExitListener() {
			@Override
			public void onGameExit(boolean doExit) {
				callback.onResult(doExit);
			}
		});
    }
}
