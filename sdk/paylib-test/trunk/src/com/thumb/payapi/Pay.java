package com.thumb.payapi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.telephony.TelephonyManager;

@SuppressLint("NewApi")
public class Pay {
    public static final int PAY_SUCCESS = 0;
    public static final int PAY_CANCEL = 1;
    public static final int PAY_FAILED = 2;
    private static String payInfoArray[] = null;
    public static void init(Activity activity) {
        String payContent = getFromAssets(activity, "pi/paylist.txt");
        payInfoArray = payContent.split("\n");
    }
    public static String getUserId(Context context) {
        return "";
    }
    public static interface PayCallback {
        void onResult(int result, String errMessage, String payInfo);
    }
    public static interface CheckExitCallback {
        void onResult(boolean doExit);
    }
    public static String getFromAssets(Context context, String fileName) {
        BufferedReader bufReader = null;
        String result = null;
        try {
            bufReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open(fileName)));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {}
            }
        }
        return result;
    }
    private static String getPayMessage(final Context context, int id) {
        String payContent = getFromAssets(context, "pi/paylist.txt");
        String[] lines = payContent.split("\n");
        if (id >= 0 && id <= lines.length) {
            String payInfo = lines[id].trim();
            String[] fields = payInfo.split(",");
            //0,20钻石,200
            String name = fields[1];
            String price = fields[2];
            return String.format("计费点ID:%d\n计费点名称:%s\n价格:%s\n", id, name, price);
        }
        return null;
    }
    
    private static String getImsi(Context context) {
        String result="";
        try {
            result= ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        } catch (Throwable e) {
           e.printStackTrace();
        }
        return result;
    }
    public static void pay(final Activity activity, int payIndex, final PayCallback callback) {
        final String payMessage = getPayMessage(activity, payIndex);
        if (payMessage != null) {
            final String payInfo = payInfoArray[payIndex];
            new AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_alert).setMessage(payMessage).setTitle("付费演示:"+getImsi(activity)).setPositiveButton("确认支付", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    callback.onResult(PAY_SUCCESS, null, payInfo);
                }
            }).setNegativeButton("取消支付", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    callback.onResult(PAY_CANCEL, "cancel", payInfo);
                }
            }).create().show();
        } else {
            new AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("没有付费信息!").setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
        }
    }
    public static void onPause(Context context) {}
    public static void onResume(Context context) {}
    public static void onApplicationCreate(Application application) {}
    public static void onApplicationTerminate(Application application) {}
    public static void showMoreGames(Context context) {}
    public static void checkExit(final Activity activity, final CheckExitCallback callback) {
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
    /**
     * 
     * @param context
     * @return true: controlled by sdk api
     * @return false: controlled by sdk api
     * @return null: not controlled by sdk api
     * 
     */
    public static Boolean isMusicOn(Context context) {
        return null;
    }
    public static boolean showAbout(Context context) {
        return false;
    }
    public static boolean showActivate(Context context) {
        return true;
    }
    public static boolean hidePayInfoDialog(Activity aCTIVITY, int payIndex) {
        return false;
    }
}
