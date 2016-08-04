package com.thumb.payapi.jpay;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import com.jpay.sdk.JPay.IChargeResult;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class JPay {
    private static String payInfoArray[] = null;
    static String cId = null;// "5000";
    static String vCode = null;//"12345";
    static String appId = null;//"A0001";
    static String pupChannelId = null;//"MSP201168";
    private static com.jpay.sdk.JPay sdk = com.jpay.sdk.JPay.getInstance();
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.JPAY + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.JPAY + "2.db"));
            cId = obj.getString("cId");
            vCode = obj.getString("vCode");
            appId = obj.getString("appId");
            pupChannelId = obj.getString("pupChannelId");
        } catch (JSONException e) {
            MyLogger.error(e);
        }
        sdk.init(activity, cId, vCode, appId, pupChannelId);
    }
    public static void pay(final Activity context, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        String fields[] = payInfo.split(",");
        String price = fields[2];
        String name = "";// fields[1];
        sdk.charge(cId, vCode, price, "", "", name, name, new IChargeResult() {
            @Override
            public void onChargeResult(int retCode, String retMsg) {
                int gameResult = Pay.PAY_FAILED;
                switch (retCode) {
                    case 0:
                        gameResult = Pay.PAY_SUCCESS;
                        break;
                    case 4:
                        gameResult = Pay.PAY_CANCEL;
                        break;
                }
                callback.onResult(gameResult, retMsg, payInfo);
            }
        });
    }
}
