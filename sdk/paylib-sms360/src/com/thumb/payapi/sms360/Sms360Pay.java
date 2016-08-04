package com.thumb.payapi.sms360;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.qihoo.gamecenter.djsdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.djsdk.protocols.pay.ProtocolKeys;
import com.qihoopay.insdk.activity.ContainerActivity;
import com.qihoopay.insdk.matrix.Matrix;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;
import com.thumb.payapi.utils.ResourceUtils;

@SuppressLint("NewApi")
public class Sms360Pay {
    private static boolean isLandScape;
    private static String payInfoArray[] = null;
    private static boolean inited = false;
    private static final String encodedSdkName = EncodedSdkConstants.QIHOO_SMS;
    public static void init(final Activity activity) {
        if (!inited) {
            {
                String content = FileUtils.readEncodedAssetFile(activity, "pi/" + encodedSdkName + ".db");
                payInfoArray = content.split("\n");
                isLandScape = "true".equals(ResourceUtils.getRString(activity, "isLandScape"));
            }
            {
                Matrix.init(activity, isLandScape, new IDispatcherCallback() {
                    @Override
                    public void onFinished(String data) {
                        MyLogger.info("matrix startup callback,result is " + data);
                    }
                });
            }
            inited = true;
        }
    }
    public static void pay(final Activity activity, int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex].trim();
        String[] fields = payInfo.split(",");
        if (fields.length < 4) {
            callback.onResult(Pay.PAY_FAILED, "sdk-info-not-defined", payInfo);
            return;
        }
        String goodInputId = fields[3].trim();
        Bundle bundle = new Bundle();
        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);
        // =========================================================
        // 联通和移动短信支付
        // 使用此功能时，需要先到360平台申请联通短信支付通道，并配合申请下来的 china_unicom.xml 一同使用才能支付成功。
        // 商品ID(在360平台申请计费通道时填写的商品信息)
        bundle.putString(ProtocolKeys.CONCH_PRODUCT_ID, goodInputId);
        //        // 商户自定义订单号，长度必须为16字符，否则影响支付结果
        //        bundle.putString(ProtocolKeys.CONCH_DEFINED_ORDER, String.valueOf(System.currentTimeMillis()) + "000");
        Intent intent = new Intent(activity, ContainerActivity.class);
        intent.putExtras(bundle);
        Matrix.invokeActivity(activity, intent, new IDispatcherCallback() {
            public void onFinished(String data) {
                int result = Pay.PAY_FAILED;
                String errMessage = null;
                try {
                    JSONObject jsonRes = new JSONObject(data);
                    errMessage = jsonRes.optString("error_msg");
                    // error_code 状态码： 0 支付成功， -1 支付取消， 1 支付失败。
                    // error_msg 状态描述
                    switch (jsonRes.getInt("error_code")) {
                        case 0:
                            result = Pay.PAY_SUCCESS;
                            break;
                        case -1:
                            result = Pay.PAY_CANCEL;
                            break;
                    }
                } catch (Throwable e) {
                    MyLogger.error(e);
                    errMessage = e.getMessage();
                }
                callback.onResult(result, errMessage, payInfo);
            }
        });
    }
}
