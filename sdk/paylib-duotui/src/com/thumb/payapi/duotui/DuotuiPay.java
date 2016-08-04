package com.thumb.payapi.duotui;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.dt.p.SDKManager;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;
import com.thumb.payapi.utils.ResourceUtils;

public class DuotuiPay {
    private static String payInfoArray[] = null;
    private static SDKManager sdk;
    private static int windowWidth = 289;
    private static int windowHeight = 289;
    private static int confirmTopMargin = 100;
    private static int cancelTopMargin;
    private static int cancelLeftMargin;
    private static int msgTopMargin = 100;
    public static void init(final Activity activity) {
        try {
            String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.DUOTUI + ".db");
            payInfoArray = payInfoContent.split("\n");
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.DUOTUI + "2.db"));
            windowWidth = obj.getInt("windowWidth");
            windowHeight = obj.getInt("windowHeight");
            confirmTopMargin = obj.getInt("confirmTopMargin");
            cancelTopMargin = obj.getInt("cancelTopMargin");
            cancelLeftMargin = obj.getInt("cancelLeftMargin");
            msgTopMargin = obj.getInt("msgTopMargin");
            sdkInit(activity);
        } catch (Throwable e) {
            MyLogger.error(e);
        }
    }
    private static void sdkInit(final Activity activity) {
        sdk = SDKManager.getInstance(activity);
        //关闭按钮
        sdk.setNegativeBtnResId(ResourceUtils.getDrawableIndex(activity, "pclose"));
        Point cancelPoint = new Point(dip2px(activity, cancelLeftMargin), -dip2px(activity, cancelTopMargin));
        sdk.setCancelPoint(cancelPoint);
        //sdk.setMsgFontColor(0x70, 79,171,228);
        sdk.setPositiveBtnResId(ResourceUtils.getDrawableIndex(activity, "pconfirm"));
        sdk.setOkPoint(new Point(0, dip2px(activity, confirmTopMargin)));
        //电话
        // sdk.setTelFontColor(0xaf, 0x56, 0x34, 0x89);
        //sdk.setTelFontColor(0xff, 90,189,247);
        //sdk.setTelFontSize(1);
        //sdk.setTelPoint(new Point(dip2px(this, 0), dip2px(this, 30)));
        sdk.setTel(true, "123456");
        //支付
        Point msgPoint = new Point(new Point(dip2px(activity, 0), dip2px(activity, msgTopMargin)));
        sdk.setMsgPoint(msgPoint);
        sdk.setMsgFontSize(1);
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        setBackground(activity, payIndex);
        final String payInfo = payInfoArray[payIndex].trim();
        String fields[] = payInfo.split(",");
        String name = fields[1].trim();
        int price = Integer.valueOf(fields[2].trim());
        Handler handler = new Handler(activity.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                try {
                    String json2 = (String) msg.obj;
                    JSONObject pay = new JSONObject(json2);
                    int resultCode = pay.getInt("resultCode");
                    String errMessage = pay.getString("desc");
                    int result = Pay.PAY_FAILED;
                    if (resultCode == 0) {
                        result = Pay.PAY_SUCCESS;
                    } else if (resultCode == -2) {
                        result = Pay.PAY_CANCEL;
                    }
                    callback.onResult(result, errMessage, payInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        sdk.buy("", handler, SDKManager.WHAT_BUY_CALLBACK_DEFAULT, price, name, SDKManager.TYPE_GAME);
    }
    private static void setBackground(final Activity activity, final int payIndex) {
        ImageView backGroundImage = new ImageView(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dip2px(activity, windowWidth), dip2px(activity, windowHeight));
        backGroundImage.setLayoutParams(params);
        backGroundImage.setScaleType(ScaleType.FIT_XY);
        backGroundImage.setBackgroundResource(ResourceUtils.getDrawableIndex(activity, "p00") + payIndex);
        sdk.setCustomView(backGroundImage);
    }
    public static int dip2px(Context ctx, int dpValue) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
