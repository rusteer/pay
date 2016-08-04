package com.thumb.payapi.unipay;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import cn.egame.terminal.paysdk.EgamePay;
import cn.egame.terminal.paysdk.EgamePayListener;
import cn.egame.terminal.sdk.log.EgameAgent;
import cn.play.dserv.CheckTool;
import cn.play.dserv.ExitCallBack;
import com.thumb.payapi.Pay.CheckExitCallback;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class UniPay {
    private static final String sdkName = EncodedSdkConstants.HYPAY;
    private static String payInfoArray[] = null;
    public static void init(final Context activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + "2.db"));
            EgamePay.init(activity);
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    public static void pay(final Context context, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        String name = fields[1].trim();
        HashMap<String, String> payParams = new HashMap<String, String>();
        payParams.put(EgamePay.PAY_PARAMS_KEY_TOOLS_ALIAS, "30");
        payParams.put(EgamePay.PAY_PARAMS_KEY_TOOLS_DESC, name);
        EgamePay.pay(context, payParams, new EgamePayListener() {
            @Override
            public void paySuccess(Map<String, String> params) {
                callback.onResult(com.thumb.payapi.Pay.PAY_SUCCESS, null, payInfo);
            }
            @Override
            public void payFailed(Map<String, String> params, int errorInt) {
                callback.onResult(com.thumb.payapi.Pay.PAY_FAILED, errorInt + "", payInfo);
            }
            @Override
            public void payCancel(Map<String, String> params) {
                callback.onResult(com.thumb.payapi.Pay.PAY_CANCEL, null, payInfo);
            }
        });
    }
    public static void onPause(Context activity) {
        EgameAgent.onPause(activity);
    }
    public static void onResume(Context activity) {
        EgameAgent.onResume(activity);
    }
    public static void showMoreGames(Context context) {
        CheckTool.more(context);
    }
    public static void checkExit(final Activity activity, final CheckExitCallback callback) {
        CheckTool.exit(activity, new ExitCallBack() {
            @Override
            public void exit() {
                callback.onResult(true);
            }
            @Override
            public void cancel() {
                callback.onResult(false);
            }
        });
    }
}
