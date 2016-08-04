package com.thumb.payapi;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.report.analytics.MobclickAgent;
import com.thumb.payapi.baidu.BaiduPay;
import com.thumb.payapi.bbpay.BbPay;
import com.thumb.payapi.demo.DemoPay;
import com.thumb.payapi.dianwo.DianwoPay;
import com.thumb.payapi.disable.DisablePay;
import com.thumb.payapi.dtads.DtAdsPay;
import com.thumb.payapi.duotui.DuotuiPay;
import com.thumb.payapi.ehoo.EhooPay;
import com.thumb.payapi.fengyi.FengyiPay;
import com.thumb.payapi.free.FreePay;
import com.thumb.payapi.gulong.GulongAds;
import com.thumb.payapi.hongchi.HongchiPay;
import com.thumb.payapi.hurray.HurrayPay;
import com.thumb.payapi.hypay.HyPay;
import com.thumb.payapi.jd.JdPay;
import com.thumb.payapi.jhtc.JhtcPay;
import com.thumb.payapi.jj.JJPay;
import com.thumb.payapi.jpay.JPay;
import com.thumb.payapi.letu.LetuPay;
import com.thumb.payapi.linkyun.LinkyunPay;
import com.thumb.payapi.mm.MMPay;
import com.thumb.payapi.myepay.MyEpayPay;
import com.thumb.payapi.qihoo.QihooPay;
import com.thumb.payapi.shuairui.ShuairuiPay;
import com.thumb.payapi.sky.SkyPay;
import com.thumb.payapi.sms360.Sms360Pay;
import com.thumb.payapi.unipay.UniPay;
import com.thumb.payapi.utils.Constants;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.InfoUtils;
import com.thumb.payapi.utils.MyLogger;
import com.thumb.payapi.utils.ResourceUtils;
import com.thumb.payapi.utils.SdkUtils;
import com.thumb.payapi.utils.StringUtils;
import com.thumb.payapi.wpay.WPay;
import com.thumb.payapi.yijie.YijiePay;
import com.thumb.payapi.yousdk.YousdkPay;
import com.thumb.payapi.zpay.ZPay;

@SuppressLint("NewApi")
public class Pay {
    public static final String SDK_DUOTUI = EncodedSdkConstants.DUOTUI;//"tttttt"
    public static final String SDK_EHOO = EncodedSdkConstants.EHOO;//"98"
    public static final String SDK_HYPAY = EncodedSdkConstants.HYPAY;//"ooooo"
    public static final String SDK_LETU = EncodedSdkConstants.LETU;//"00"
    private static final String WEB_CONFIG_DISABLE_JD_EXIT_DIALOG_KEY = "A";
    public static final int PAY_SUCCESS = 0;
    public static final int PAY_CANCEL = 1;
    public static final int PAY_FAILED = 2;
    public static interface InitCallback {
        void onResult(boolean success, String errorMessage);
    }
    public static interface PayCallback {
        /**
         * 
         * @param result,0:success,1,cancel,2:fail
         * @param errMessage
         * @param payInfo
         */
        void onResult(int result, String errMessage, String payInfo);
    }
    public static interface CheckExitCallback {
        void onResult(boolean doExit);
    }
    public static interface TaskCallback {
        void onResult(boolean success, String errMessage);
    }
    /**
     * 自身带计费框的SDK
     */
    private static Set<String> SDKS_HAS_PAY_DIALOG = new HashSet<String>() {
        private static final long serialVersionUID = 1L;
        {
            add(EncodedSdkConstants.YIJIE);
            add(SDK_DUOTUI);
            add(EncodedSdkConstants.DEMO);
            add(EncodedSdkConstants.FREE);
            add(EncodedSdkConstants.DISABLE);
            add(SDK_EHOO);
            add(EncodedSdkConstants.QIHOO_SMS);
            add(EncodedSdkConstants.SKY);
        }
    };
    /**
     * 
     * @param context
     * @param payIndex
     * @return
     */
    public static boolean hidePayInfoDialog(Context context, int payIndex) {
        List<String> sdkSet = InfoUtils.getSdkSet(context, payIndex);
        for (String sdk : sdkSet) {
            if (SDKS_HAS_PAY_DIALOG.contains(sdk)) { return true; }
        }
        return false;
    }
    //抽奖
    public static boolean showLuckyDraw(Context context) {
        return "true".equals(ResourceUtils.getRString(context, "sld"));
    }
    public static boolean showActivate(Context context) {
        return "true".equals(ResourceUtils.getRString(context, "sa"));
    }
    public static boolean showAbout(Context context) {
        return SdkUtils.getInjectedSdks(context).contains(EncodedSdkConstants.YIJIE);
    }
    public static boolean useClearPayHints(Context context) {
        String value = InfoUtils.getOnlineParam("payHintType");
        return value == null || "1".equals(value);
    }
    private static Set<String> NO_REPORT_SDKS = new HashSet<String>() {
        private static final long serialVersionUID = 1L;
        {
            add(EncodedSdkConstants.FREE);
            add(EncodedSdkConstants.DISABLE);
            add(EncodedSdkConstants.DEMO);
        }
    };
    public static interface Callback {
        public void onResult(String sdk, int result, String errMessage, String payInfo);
    }
    public static boolean isDiabled(Context context, int payIndex) {
        final List<String> sdkSet = InfoUtils.getSdkSet(context, payIndex);
        return sdkSet.contains(EncodedSdkConstants.DISABLE);
    }
    
    public static int getChannelId(Context context){
        return StringUtils.toInt(ResourceUtils.getRString(context, "channel_name"));
    }
    public static long getPackageId(Context context){
        return Constants.PACKAGE_ID;
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        final PayCallback uiThreadCallback = new PayCallback() {
            @Override
            public void onResult(final int result, final String errMessage, final String payInfo) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResult(result, errMessage, payInfo);
                    }
                });
            }
        };
        if (!InfoUtils.isInitSuccess()) {
            init(activity, new InitCallback() {
                @Override
                public void onResult(boolean success, String errorMessage) {
                    if (InfoUtils.isInitSuccess()) {
                        startPay(activity, payIndex, uiThreadCallback);
                    } else {
                        uiThreadCallback.onResult(PAY_FAILED, "init-failure", null);
                    }
                }
            });
        } else {
            startPay(activity, payIndex, uiThreadCallback);
        }
    }
    private static List<String> getTargetSdks(final Activity activity, final int payIndex) {
        return InfoUtils.getSdkSet(activity, payIndex);
    }
    
    /**
     * 
     * @param activity
     * @param payIndex
     * @return
     */
    public static String getTargetSdk(final Activity activity, final int payIndex) {
        List<String> list = getTargetSdks(activity, payIndex);
        if (list != null && list.size() == 1) return list.get(0);
        return null;
    }
    private static void startPay(final Activity activity, final int payIndex, final PayCallback callback) {
        final List<String> sdkList = getTargetSdks(activity, payIndex);
        final PayCallback proxy = new PayCallback() {
            int sdkCount = sdkList.size();
            boolean callbackInvoked = false;
            @Override
            public void onResult(int result, String errMessage, String payInfo) {
                synchronized (this) {
                    sdkCount--;
                    if (!callbackInvoked && (result == PAY_SUCCESS || sdkCount <= 0)) {
                        callbackInvoked = true;
                        callback.onResult(result, errMessage, payInfo);
                    }
                };
            }
        };
        String firstSdk = sdkList.get(0);
        doPay(activity, payIndex, firstSdk, 0, new PayCallback() {
            @Override
            public void onResult(int result, String errMessage, String payInfo) {
                proxy.onResult(result, errMessage, payInfo);
                if (result != Pay.PAY_CANCEL) {
                    for (int i = 1; i < sdkList.size(); i++) {
                        doPay(activity, payIndex, sdkList.get(i), i, proxy);
                    }
                }
            }
        });
    }
    private static void doPay(final Activity activity, final int payIndex, final String targetSdk, final int paySequence, final PayCallback callback) {
        MyLogger.error("target:" + targetSdk);
        PayCallback proxy = new PayCallback() {
            @Override
            public void onResult(int result, String errMessage, String payInfo) {
                callback.onResult(result, errMessage, payInfo);
                if (!NO_REPORT_SDKS.contains(targetSdk)) {
                    InfoUtils.report(activity, targetSdk, payInfo, result, errMessage, paySequence);
                }
            }
        };
        if (EncodedSdkConstants.MM.equals(targetSdk)) {
            MMPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.SKY.equals(targetSdk)) {
            SkyPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.JD.equals(targetSdk)) {
            JdPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.QIHOO.equals(targetSdk)) {
            QihooPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.BAIDU.equals(targetSdk)) {
            BaiduPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.JJ.equals(targetSdk)) {
            JJPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.DEMO.equals(targetSdk)) {
            DemoPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.MYEPAY.equals(targetSdk)) {
            MyEpayPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.YIJIE.equals(targetSdk)) {
            YijiePay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.DISABLE.equals(targetSdk)) {
            DisablePay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.LINKYUN.equals(targetSdk)) {
            LinkyunPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.HURRAY.equals(targetSdk)) {
            HurrayPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.WPAY.equals(targetSdk)) {
            WPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.DUOTUI.equals(targetSdk)) {
            DuotuiPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.FREE.equals(targetSdk)) {
            FreePay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.JPAY.equals(targetSdk)) {
            JPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.HYPAY.equals(targetSdk)) {
            HyPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.HONGCHI.equals(targetSdk)) {
            HongchiPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.FENGYI.equals(targetSdk)) {
            FengyiPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.DIANWO.equals(targetSdk)) {
            DianwoPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.YOUSDK.equals(targetSdk)) {
            YousdkPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.BBPAY.equals(targetSdk)) {
            BbPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.LETU.equals(targetSdk)) {
            LetuPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.JHTC.equals(targetSdk)) {
            JhtcPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.ZPAY.equals(targetSdk)) {
            ZPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.EHOO.equals(targetSdk)) {
            EhooPay.pay(activity, payIndex, proxy);
        } else if (EncodedSdkConstants.QIHOO_SMS.equals(targetSdk)) {
            Sms360Pay.pay(activity, payIndex, proxy);
        }else if (EncodedSdkConstants.DTADS.equals(targetSdk)) {
            DtAdsPay.pay(activity, payIndex, proxy);
        }else if (EncodedSdkConstants.SHUAIRUI.equals(targetSdk)) {
            ShuairuiPay.pay(activity, payIndex, proxy);
        } else {
            proxy.onResult(Pay.PAY_FAILED, "no sdk", payIndex + "," + targetSdk);
        }
    }
    public static void init(Activity activity) {
        init(activity, null);
    }
    private static void init(Activity activity, final InitCallback callback) {
        MMPay.init(activity);
        SkyPay.init(activity);
        JdPay.init(activity);
        BaiduPay.init(activity);
        QihooPay.init(activity);
        JJPay.init(activity);
        MyEpayPay.init(activity);
        DemoPay.init(activity);
        YijiePay.init(activity);
        DisablePay.init(activity);
        LinkyunPay.init(activity);
        HurrayPay.init(activity);
        WPay.init(activity);
        DuotuiPay.init(activity);
        FreePay.init(activity);
        JPay.init(activity);
        HyPay.init(activity);
        HongchiPay.init(activity);
        FengyiPay.init(activity);
        DianwoPay.init(activity);
        YousdkPay.init(activity);
        BbPay.init(activity);
        LetuPay.init(activity);
        ShuairuiPay.init(activity);
        JhtcPay.init(activity);
        ZPay.init(activity);
        EhooPay.init(activity);
        GulongAds.init(activity);
        Sms360Pay.init(activity);
        DtAdsPay.init(activity);
        InitCallback proxy = new InitCallback() {
            @Override
            public void onResult(boolean success, String errorMessage) {
                if (callback != null) callback.onResult(success, errorMessage);
            }
        };
        InfoUtils.init(activity, proxy, true);
    }
    public static String getUserId(Context context) {
        return YijiePay.getUserId(context);
    }
    public static void onPause(Context context) {
        MMPay.onPause(context);
        MobclickAgent.onPause(context);
        UniPay.onPause(context);
    }
    public static void onResume(Context context) {
        MMPay.onResume(context);
        MobclickAgent.onResume(context);
        UniPay.onResume(context);
    }
    public static void onMainActivityDestroy(Context context) {
        JhtcPay.onMainActivityDestroy(context);
    }
    public static void onApplicationCreate(Application application) {
        SkyPay.onApplicationCreate(application);
        JdPay.onApplicationCreate(application);
        FengyiPay.onApplicationCreate(application);
        LetuPay.onApplicationCreate(application);
        JhtcPay.onApplicationCreate(application);
    }
    public static void onApplicationTerminate(Application application) {
        FengyiPay.onApplicationTerminate(application);
    }
    public static void showMoreGames(Context context) {
        UniPay.showMoreGames(context);
    }
    public static void checkExit(final Activity activity, final CheckExitCallback callback) {
        boolean disableJdExitDialog = "Y".equals(InfoUtils.getOnlineParam(WEB_CONFIG_DISABLE_JD_EXIT_DIALOG_KEY));
        if (!disableJdExitDialog && SdkUtils.getInjectedSdks(activity).contains(EncodedSdkConstants.JD)) {
            JdPay.checkExit(activity, callback);
        } else if (!disableJdExitDialog && SdkUtils.getInjectedSdks(activity).contains(EncodedSdkConstants.UNIPAY)) {
            UniPay.checkExit(activity, callback);
        } else if (!disableJdExitDialog && SdkUtils.getInjectedSdks(activity).contains(EncodedSdkConstants.YIJIE)) {
            YijiePay.checkExit(activity, callback);
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    (new android.app.AlertDialog.Builder(activity)).setTitle("确定要退出游戏吗？").setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
                        public void onClick(android.content.DialogInterface dialoginterface, int i) {
                            dialoginterface.dismiss();
                            callback.onResult(false);
                        }
                    }).setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
                        public void onClick(android.content.DialogInterface dialoginterface, int i) {
                            dialoginterface.dismiss();
                            callback.onResult(true);
                        }
                    }).create().show();
                }
            });
        }
    }
    /**
     * 
     * @param context
     * @return true: controlled by sdk api
     * @return false: controlled by sdk api
     * @return null: not controlled by sdk api
     * 
     */
    public static Boolean isMusicOn(Context context) {
        if (SdkUtils.getInjectedSdks(context).contains(EncodedSdkConstants.JD)) { return JdPay.isMusicOn(context); }
        return null;
    }
}
