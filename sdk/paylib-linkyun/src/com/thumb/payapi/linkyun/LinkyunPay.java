package com.thumb.payapi.linkyun;
import java.util.Random;
import org.json.JSONObject;
import android.app.Activity;
import com.ly.pay.LinkYunTools;
import com.ly.pay.PayResult;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.Constants;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class LinkyunPay {
    //sChannel是D9 
    //sGame值看您那边接入的第几个游戏，1,2,3,这种依次类推
    private static String appId;
    private static String channelId;
    private static String payInfoArray[] = null;
    private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456";
    public static String getRandomString(int length) {
        //String content = "abceghjklmnopqrstuvxyzABCEHKMXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    public static void init(final Activity activity) {
        try {
            String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.LINKYUN + ".db");
            payInfoArray = payInfoContent.split("\n");
            //
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + EncodedSdkConstants.LINKYUN + "2.db"));
            appId = obj.getString("appId");
            channelId = obj.getString("channelId");
            LinkYunTools.initLY(activity, appId, channelId, "");
            LinkYunTools.setOpenDialog(true);//开启/关闭loading弹框,true是关闭，false是打开
        } catch (Throwable e) {
            MyLogger.error(e);
        }
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        PayResult sdkCallback = new PayResult() {
            @Override
            public void payResult(int arg0, String arg1) {
                switch (arg0) {
                    case 0:
                        callback.onResult(Pay.PAY_SUCCESS, arg1, payInfo);
                        break;
                    case PAY_FAIL:
                        callback.onResult(Pay.PAY_FAILED, arg1, payInfo);
                        break;
                    case PAY_CANCEL:
                        callback.onResult(Pay.PAY_CANCEL, arg1, payInfo);
                        break;
                }
            }
        };
        String[] fields = payInfo.split(",");
        String name = fields[1].trim();
        int price = Integer.valueOf(fields[2].trim());
        String sMsgAsk = name;//  String  提示语 购买该商品的提示语。如：1金币
        String sMsgSuccess = "领取成功";//String  成功信息    购买该商品的成功提示语。如：购买1金币成功
        String sPrice = String.valueOf(1000 + price / 100).substring(2);//String  金额  计费金额单位为元，格式为两位，例如1元=01，10元=10
        String sGame = appId;//String  游戏ID    游戏ID，在凌云获取
        String sGameName = activity.getApplicationInfo().loadLabel(activity.getPackageManager()).toString();// String  应用名称    应用名称
        String sChannel = channelId;//String  商户编号    应用ID，在凌云获取
        String sExtCmd = Constants.CHANNEL_SHORT_NAME + getRandomString(6);//String  订单号 商户应用生成的订单号(由数字和字母组成，长度为8位，历史记录不能有重复)
        LinkYunTools.pay(activity, sdkCallback, sMsgAsk, sMsgSuccess, sPrice, sGame, sGameName, sChannel, sExtCmd);
    }
}
