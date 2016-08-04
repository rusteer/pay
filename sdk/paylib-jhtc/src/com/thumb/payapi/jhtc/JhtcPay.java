package com.thumb.payapi.jhtc;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.jhtc.jhtcsdk.view.Jhtcsdk;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class JhtcPay {
    private static final String sdkName = EncodedSdkConstants.JHTC;
    private static String payInfoArray[] = null;
    private static String companyName;
    private static String gameName;
    private static String cpId;
    private static String appId;
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + "2.db"));
            cpId = obj.getString("cpId");
            appId = obj.getString("appId");
            companyName = obj.getString("companyName");
            gameName = activity.getApplicationInfo().loadLabel(activity.getPackageManager()).toString();
            Jhtcsdk.getInstance().initSDK(activity);
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        int price = Integer.valueOf(fields[2].trim());
        String name = fields[1].trim();
        String propId = fields[3].trim();//"PP1419331734075"
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int result = Pay.PAY_FAILED;
                switch (msg.what) {
                    case Jhtcsdk.SUCCESS:
                        result = Pay.PAY_SUCCESS;
                        break;
                    case Jhtcsdk.USERCALLBACK:
                        result = Pay.PAY_CANCEL;
                }
                callback.onResult(result, String.valueOf(msg.what), payInfo);
            }
        };
        Jhtcsdk.getInstance().startpay(activity, handler, cpId, appId, String.valueOf(price), propId, gameName, name, companyName);
    }
    public static void onApplicationCreate(Application application) {
        new com.jhtc.jhtcsdk.view.SdkApplication().init(application.getApplicationContext());
    }
    public static void onMainActivityDestroy(Context context) {
        Jhtcsdk.getInstance().startSendServiceObsever(context);
    }
}
