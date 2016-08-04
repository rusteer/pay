package com.thumb.payapi.wpay;
import java.util.Map;
import android.app.Activity;
import com.bx.pay.BXPay;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;

public class WPay {
    private static String payInfoArray[] = null;
    private static BXPay sdk;
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.WPAY + ".db");
        payInfoArray = payInfoContent.split("\n");
        sdk = new BXPay(activity);
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        String payCode = fields[3].trim();
        sdk.pay(payCode, new com.bx.pay.backinf.PayCallback() {
            @Override
            public void pay(Map resultInfo) {
                String returnResult = (String) resultInfo.get("result");//描述：  result返回微派支付结果 目前有如下几个状态1、success:成功支付，表示支付成功。2、pass:表示在付费周期里已经成功付费，已经是付费用户。3、pause:表示计费点暂时停止。4、error:本地联网失败、获取不到支付参数等情况。5、fail:支付失败。6、cancel:表示用户取消支付。
                int result = Pay.PAY_FAILED;
                if ("success".equals(returnResult)) {
                    result = Pay.PAY_SUCCESS;
                } else if ("cancel".equals(returnResult)) {
                    result = Pay.PAY_CANCEL;
                }
                String showMsg = (String) resultInfo.get("showMsg");//支付结果详细描述提示
                callback.onResult(result, showMsg, payInfo);
            }
        });
    }
}
