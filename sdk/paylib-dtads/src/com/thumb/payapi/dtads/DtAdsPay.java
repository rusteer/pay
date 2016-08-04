package com.thumb.payapi.dtads;
import org.json.JSONObject;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import com.android.it.CustomCallBack;
import com.dot.a.SDK;
import com.dot.a.view.DtView;
import com.dot.a.view.LoadType;
import com.dot.a.view.ViewType;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.utils.EncodedSdkConstants;
import com.thumb.payapi.utils.FileUtils;
import com.thumb.payapi.utils.MyLogger;

public class DtAdsPay {
    private static String payInfoArray[] = null;
    private static String adsId=null;
    private static String notiId=null;
    private static String bannerId=null;
    private static String screenId=null;
   
    private static final String SDK_NAME=EncodedSdkConstants.DUOTUI;
    public static void init(final Activity activity) {
        try {
            String payInfoContent = FileUtils.readEncodedAssetFile(activity, "pi/" +SDK_NAME + ".db");
            payInfoArray = payInfoContent.split("\n");
            JSONObject obj = new JSONObject(FileUtils.readEncodedAssetFile(activity, "pi/" + SDK_NAME + "2.db"));
            adsId = obj.getString("adsId");
            notiId = obj.getString("notiId");
            bannerId = obj.getString("bannerId");
            screenId = obj.getString("screenId");
            sdkInit(activity);
        } catch (Throwable e) {
            MyLogger.error(e);
        }
    }
    private static void sdkInit(final Activity activity) {
        SDK sdk = SDK.getInstance();
        // 设置潜伏期，参数为具体的某一天的时间点的毫秒值，建议为安装时间+上几个小时或者几天
        sdk.setLatentTime(getFirstInstallTime(activity) + 0);
        // 设置通知栏收起后的图标，如果不设置，则用一个系统图标
        //sdk.setNotifyStatusIcon(R.drawable.reward_small_coin);
        // 初始化要在 设置测试模式，设置渠道号，设置时间后
        sdk.init(activity);
    }
    public static long getFirstInstallTime(Activity activity) {
        long firstInstallTime = -1;
        String pkName = activity.getPackageName();
        try {
            PackageManager pm = activity.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(pkName, 0);
            firstInstallTime = pInfo.firstInstallTime;
        } catch (NameNotFoundException e) {}
        return firstInstallTime;
    }
    public static void pay(final Activity activity, final int payIndex, final PayCallback callback) {
        DtView dtView = new DtView(activity, screenId,ViewType.INSERTSCREEN,LoadType.SYNCHRONOUS,new CustomCallBack(){
            @Override
            public void onCall(String arg0) {
                // TODO Auto-generated method stub
                
            }});
        dtView.show();
    }
    
}
