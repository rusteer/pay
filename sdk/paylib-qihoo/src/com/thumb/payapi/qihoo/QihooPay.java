package com.thumb.payapi.qihoo;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.protocols.pay.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.pay.ProtocolKeys;
import com.qihoopay.insdk.activity.ContainerActivity;
import com.qihoopay.insdk.matrix.Matrix;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;
import com.thumb.payapi.utils.ResourceUtils;

@SuppressLint("NewApi")
public class QihooPay {
    private static boolean isLandScape;
    private static String payInfoArray[] = null;
    private static boolean inited = false;
    private static String productName = null;
    public static void init(final Activity activity) {
        if (!inited) {
            {
                String content = FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.QIHOO + ".db");
                payInfoArray = content.split("\n");
                isLandScape = "true".equals(ResourceUtils.getRString(activity, "isLandScape"));
            }
            {
                Matrix.init(activity, isLandScape, new IDispatcherCallback() {
                    @Override
                    public void onFinished(String data) {
                        MyLogger.error("matrix startup callback,result is " + data);
                    }
                });
            }
            {
                final StringBuilder sb = new StringBuilder().append(activity.getApplicationInfo().loadLabel(activity.getPackageManager()));
                productName = sb.length() > 10 ? sb.substring(0, 10) : sb.toString();
            }
            inited = true;
        }
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final int price = Integer.valueOf(payInfo.split(",")[2]);
        Intent intent = QihooPay.getPayIntent(activity, productName, isLandScape, price);
        // 必需参数，使用360SDK的支付模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_OFFLINE_PAY);
        // 界面相关参数，设置360SDK登录界面背景不透明。
        // 此参数需要配合assets下的360sdk_bg.jpg一起使用，使用时，请替换360sdk_bg.jpg为游戏效果图
        intent.putExtra(ProtocolKeys.IS_LOGIN_BG_TRANSPARENT, false);
        Matrix.invokeActivity(activity, intent, new IDispatcherCallback() {
            @Override
            public void onFinished(String data) {
                MyLogger.info("mPayCallback, data is " + data);
                int result = Pay.PAY_FAILED;
                String errorMsg = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    int errorCode = obj.optInt("error_code");//error_code 状态码： 0 支付成功， -1 支付取消， 1 支付失败， -2 支付进行中。
                    errorMsg = obj.optString("error_msg");// error_msg 状态描述
                    if (errorCode == 0) result = Pay.PAY_SUCCESS;
                    else if (errorCode == 1) result = Pay.PAY_CANCEL;
                } catch (JSONException e) {
                    errorMsg = e.toString();
                }
                callback.onResult(result, errorMsg, payInfo);
            }
        });
    }
    /***
     * 生成调用360SDK支付接口基础参数的Intent
     *
     * @param isLandScape
     * @param pay
     * @return Intent
     */
    public static Intent getPayIntent(Activity activity, String productName, boolean isLandScape, int price) {
        Bundle bundle = new Bundle();
        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);
        // *** 以下非界面相关参数 ***
        // 设置QihooPay中的参数。
        // 必需参数，所购买商品金额, 以分为单位。金额大于等于100分，360SDK运行定额支付流程； 金额数为0，360SDK运行不定额支付流程。
        bundle.putString(ProtocolKeys.AMOUNT, String.valueOf(price));
        // 必需参数，人民币与游戏充值币的默认比例，例如2，代表1元人民币可以兑换2个游戏币，整数。
        bundle.putString(ProtocolKeys.RATE, "1");
        // 必需参数，所购买商品名称，应用指定，建议中文，最大10个中文字。
        bundle.putString(ProtocolKeys.PRODUCT_NAME, productName);
        // 必需参数，购买商品的商品id，应用指定，最大16字符。
        bundle.putString(ProtocolKeys.PRODUCT_ID, "123456");
        // 必需参数，游戏或应用名称，最大16中文字。
        String appName = new StringBuilder().append(activity.getApplicationInfo().loadLabel(activity.getPackageManager())).toString();
        if (appName.length() > 16) appName = appName.substring(0, 16);
        bundle.putString(ProtocolKeys.APP_NAME, productName);
        // 可选参数，应用扩展信息1，原样返回，最大255字符。
        bundle.putString(ProtocolKeys.APP_EXT_1, "ext1");
        // 可选参数，应用扩展信息2，原样返回，最大255字符。
        bundle.putString(ProtocolKeys.APP_EXT_2, "ext2");
        // 必需参数，应用内的用户名，如游戏角色名。 若应用内绑定360账号和应用账号，则可用360用户名，最大16中文字。（充值不分区服，
        // 充到统一的用户账户，各区服角色均可使用）。
        String appUserName = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase().substring(0, 10);
        bundle.putString(ProtocolKeys.APP_USER_NAME, appUserName);
        // 必需参数，应用内的用户id。
        // 若应用内绑定360账号和应用账号，充值不分区服，充到统一的用户账户，各区服角色均可使用，则可用360用户ID最大32字符。
        bundle.putString(ProtocolKeys.APP_USER_ID, appUserName);
        // 可选参数，应用方提供的支付结果通知uri，最大255字符。360服务器将把支付接口回调给该uri，具体协议请查看文档中，支付结果通知接口–应用服务器提供接口。
        //bundle.putString(ProtocolKeys.NOTIFY_URI, Constants.DEMO_APP_SERVER_NOTIFY_URI);
        /*
        // =========================================================
        // 联通短信支付
        // 使用此功能时，需要先到360平台申请联通短信支付通道，并配合申请下来的 china_unicom.xml 一同使用才能支付成功。
        // 商品ID(在360平台申请计费通道时填写的商品信息)
        String goodInputId = etUnicomPayGoodInputId.getText().toString();
        if (TextUtils.isEmpty(goodInputId)) {
            goodInputId = Constants.DEMO_UNICOM_PAY_GOOD_INPUT_ID;
        }
        bundle.putString(ProtocolKeys.UNICOM_GOOD_INPUTID, goodInputId);
        // 商户自定义订单号，最大32字节
        bundle.putString(ProtocolKeys.UNICOM_ORDER_NO, Constants.DEMO_UNICOM_PAY_ORDER_NO);
        // 商户自定义字段，最大16个字节
        bundle.putString(ProtocolKeys.UNICOM_USER_DEFINED_ID, Constants.DEMO_UNICOM_PAY_USER_DEFINED_ID);
        //若开发者没有接入移动的SDK, 此值为0; 若接入了移动商城, 此值为1; 若接入了移动基地,此值为2
        bundle.putInt(ProtocolKeys.CHINA_MOBILE_MODE,0);
        //若开发者没有接入电信的SDK, 此值为0; 若接入了电信天翼空间, 此值为16;若接入了电信爱游戏,此值为32
        bundle.putInt(ProtocolKeys.CHINA_TELECOM_MODE,0);
        
        */
        Intent intent = new Intent(activity, ContainerActivity.class);
        intent.putExtras(bundle);
        return intent;
    }
}
