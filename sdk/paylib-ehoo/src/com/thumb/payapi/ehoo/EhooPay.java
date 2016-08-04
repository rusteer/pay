package com.thumb.payapi.ehoo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import com.ehoo.app.ImageInfo;
import com.ehoo.app.OnInitListener;
import com.ehoo.app.OnPayListener;
import com.ehoo.app.Pay;
import com.ehoo.app.PayOption;
import com.ehoo.app.PaySDK;
import com.ehoo.app.ResultBean;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class EhooPay {
    public static String merId = null;// "1782";// 商户NCID
    private static String appId = null;// "1122"; // 应用编号
    private static int cancelY = 0;
    private static int cancelX = 0;
    private static int confirmX = 0;
    private static int confirmY = 0;
    private static final String sdkName = EncodedSdkConstants.EHOO;
    private static String payInfoArray[] = null;
    private static boolean initSuccess = false;
    public static void init(final Activity activity) {
        String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + ".db");
        payInfoArray = payInfoContent.split("\n");
        try {
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + sdkName + "2.db"));
            appId = obj.getString("appId");
            merId = obj.optString("merId");
            confirmX = obj.getInt("confirmX");
            confirmY = obj.getInt("confirmY");
            cancelX = obj.getInt("cancelX");
            cancelY = obj.getInt("cancelY");
            initSdk(activity, new InitCallback() {
                @Override
                public void onResult(boolean success) {
                    initSuccess = success;
                }
            });
        } catch (JSONException e) {
            MyLogger.error(e);
        }
    }
    private static interface InitCallback {
        void onResult(boolean success);
    }
    // PaySDK初始化 ：此初始化只需在应用启动时调用一次即可，无需计费时重复调用
    private static void initSdk(final Activity activity, final InitCallback callback) {
        PaySDK.setMerID(merId); // 所属商户NCID
        PaySDK.setOpenAppID(appId); // 应用编号
        PaySDK.init(activity, new OnInitListener() {
            @Override
            public void onInitResult(String result) {
                callback.onResult("0000".equals(result));;
            }
        });
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        if (initSuccess) {
            doPay(activity, payIndex, callback);
        } else {
            initSdk(activity, new InitCallback() {
                @Override
                public void onResult(boolean success) {
                    if (success) {
                        initSuccess = true;
                        doPay(activity, payIndex, callback);
                    } else {
                        callback.onResult(com.thumb.payapi.Pay.PAY_FAILED, "init-error", payInfoArray[payIndex]);
                    }
                }
            });
        }
    }
    private static void doPay(final Activity activity, final int payIndex, final PayCallback callback) {
        final String payInfo = payInfoArray[payIndex];
        final String[] fields = payInfo.split(",");
        String chargePoint = fields[3].trim();
        Pay pay = new Pay(activity);
        PayOption payOption = new PayOption();
        payOption.setOpenChargePoint(chargePoint);// 计费点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        payOption.setOrderDate(format.format(new Date()));
        payOption.setOrderID(generateOrder());
        setVipImage_Basic(payOption, payIndex);
        pay.setPayOptions(payOption);
        pay.setOnPayListener(new OnPayListener() {
            @Override
            public boolean onPostPayResult(ResultBean result) {
                boolean success = result != null && result.isSuccess();
                callback.onResult(success ? com.thumb.payapi.Pay.PAY_SUCCESS : com.thumb.payapi.Pay.PAY_FAILED, result.getMessage(), payInfo);
                return false;
            }
        });
        pay.start();
    }
    private static void setVipImage_Basic(PayOption option, int payIndex) {
        //背景图，图片在内部存储空间或SD卡的某路径下，dialogbg为该图片的绝对路径，如：/sdcard/bird_vip_tip_bg_style2.png"
        //如果图片在assets下 ,dialogbg为assets:/+路径名，如"assets:/image/bird_vip_tip_bg_style2.png"
        ImageInfo[] array = new ImageInfo[2];
        for (int i = 0; i < 2; i++) {
            String prefix = "assets:/ehoo/" + (i+1);
            String dialogbgFilename = prefix + "/p-" + payIndex + ".png";
            String okFilename = prefix + "/confirm.png";
            String cancelFilename = prefix + "/cancel.png";
            array[i] = new ImageInfo(dialogbgFilename, // 主体图片路径
                    okFilename, confirmX/*左*/, confirmY/*上*/, // 确定按钮图片路径以及位置坐标（这个位置坐标是在美工给出的原图上，按钮左上角相对于背景图左上角的偏移量，单位：像素）
                    cancelFilename, cancelX/*左*/, cancelY/*上*/, // 取消按钮图片路径以及位置坐标（这个位置坐标是在美工给出的原图上，按钮左上角相对于背景图左上角的偏移量，单位：像素）
                    0.8); // 整体缩放比（传入0.8表示占满屏幕的80%）              
        }
        option.setVipImageInfos(array[0], array[1]);
    }
    private static final String generateOrder() {
        String from = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        int index = 0;
        String orderId = "";
        for (int i = 0; i < 20; i++) {
            index = random.nextInt(from.length());
            orderId += from.charAt(index);
        }
        return orderId;
    }
}
