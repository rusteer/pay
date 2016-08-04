package com.thumb.payapi.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import com.thumb.payapi.Pay.InitCallback;
import com.thumb.payapi.bean.Device;
import com.thumb.payapi.bean.Json;
import com.thumb.payapi.bean.PayReport;

public class InfoUtils {
    private static final Object locker=new Object();
    private static final String CLEAR_DATA = "clearData";
    private static final String INSTANT_ID_KEY = "instantId";
    private static final String DEVICE_ID_KEY = "deviceId";
    static final String PACKAGE_ID_KEY = "packageId";
    static final String DEVICE_KEY = "device";
    private static final String ON_LINE_PARAMS_KEY = "params";
    //private static final String URL_PREFIX = "http://192.168.0.100:6080/api";
    //private static final String REPORT_URL = "http://192.168.0.106:6080/api/report";
    //private static final String INFO_URL = "http://192.168.0.106:6080/api/info";
    private static final String REPORT_URL = Constants.URL_PREFIX + "/rrrrr";
    private static final String INFO_URL = Constants.URL_PREFIX + "/iiiii";
    private static final String PHONE = "phone";
    private static final String ANDROID_ID = "android_id";
    private static String WIFI = "wifi";
    private static final String INSTANT_FILE_NAME = "/i.db";
    private static final String ANDROID_DATA = "/Android/data/";
    private static final String TIME = "time";
    private static String deviceIdPath = /*const-replace-start*/"/Android/data/com.thumb.api/d.data";
    public static String deviceStorePassword = /*const-replace-start*/"devicef_9fd&fwfl";
    //
    private static Map<String, String> onLineParams = new HashMap<String, String>();
    private static final String WEB_CONFIG_SDK_KEY = "C";
    private static final String PAY_ITEM_CONFIG_KEY = "pItems";
    private static SparseArray<List<String>> PAY_ITEM_SKDS = new SparseArray<List<String>>();
    public static List<String> getSdkSet(Context context, int payIndex) {
        Set<String> injectedSdks = SdkUtils.getInjectedSdks(context);
        List<String> list = null;
        if (injectedSdks != null && injectedSdks.size() > 0) {
            if (injectedSdks.size() == 1) {
                list = new ArrayList<String>(injectedSdks);
            } else {
                list = PAY_ITEM_SKDS.get(payIndex);
                if (list != null) {
                    for (String sdk : new HashSet<String>(list)) {
                        if (!injectedSdks.contains(sdk)) {
                            list.remove(sdk);
                        }
                    }
                }
            }
        }
        if (list == null || list.size() == 0) {
            list = new ArrayList<String>();
            list.add(getTargetSdk(context));
        }
        return list;
    }
    private static String getTargetSdk(Context context) {
        Set<String> injectedSdks = SdkUtils.getInjectedSdks(context);
        String serverSdk = getOnlineParam(WEB_CONFIG_SDK_KEY);
        if (injectedSdks.contains(serverSdk)) return serverSdk;
        return injectedSdks.iterator().next();
    }
    /**
     * Get online params
     * @param key
     * @return
     */
    public static String getOnlineParam(String key) {
        return onLineParams.get(key);
    }
    public static void init(final Context context, InitCallback callback) {
        init(context, callback, true);
    }
    private static boolean initSuccess = false;
    public static boolean isInitSuccess() {
        synchronized (locker) {
            return initSuccess;
        }
    }
    /**
     * init info
     * @param context
     * @param callback 
     */
    public static void init(final Context context, InitCallback callback, boolean retryOnClear) {
        final JSONObject requestData = composeRequestData(context);
        initRequest(context, requestData, callback, retryOnClear, 3);
    }
    private static JSONObject composeRequestData(final Context context) {
        final JSONObject requestData = new JSONObject();
        long instantId = getInstantId(context);
        if (instantId == 0) {
            Json.put(requestData, PACKAGE_ID_KEY, Constants.PACKAGE_ID);
            long deviceId = getDeviceId(context);
            if (deviceId == 0) {
                Json.put(requestData, DEVICE_KEY, getDevice(context).toJson());
            } else {
                Json.put(requestData, DEVICE_ID_KEY, deviceId);
            }
        } else {
            Json.put(requestData, INSTANT_ID_KEY, instantId);
        }
        {
            Json.put(requestData, ON_LINE_PARAMS_KEY, true);
        }
        return requestData;
    }
    private static void initRequest(final Context context, final JSONObject requestData, final InitCallback callback, final boolean retryOnClear, final int tryCount) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... p) {
                doInitRequest(context, requestData, callback, retryOnClear, tryCount);
                return null;
            }
        };
        task.execute(new Void[0]);
    }
    private static void doInitRequest(final Context context, final JSONObject requestData, final InitCallback callback, final boolean retryOnClear, final int tryCount) {
        String responseText = null;
        String errorMessage = "error";
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(INFO_URL);
            List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
            String current = String.valueOf(System.currentTimeMillis());
            post.addHeader(TIME, current);
            String password = generatePassword(current);
            list.add(new BasicNameValuePair("d", AES.encode(requestData.toString(), password)));
            post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            post.setParams(new BasicHttpParams());
            HttpResponse response = client.execute(post);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                responseText = AES.decode(EntityUtils.toString(response.getEntity()), password);
            }
        } catch (Throwable e) {
            MyLogger.error(e);
            errorMessage = e.getMessage();
        }
        if (responseText != null && responseText.length() > 0) {
            handleResponse(context, responseText, callback, retryOnClear);
        } else if (tryCount > 1) {
            initRequest(context, requestData, callback, retryOnClear, tryCount - 1);
        } else {
            callback.onResult(false, errorMessage);
        }
    }
    private static void handleResponse(final Context context, String responseText, InitCallback callback, boolean retryOnClear) {
        try {
            JSONObject object = new JSONObject(responseText);
            if (object.optBoolean(CLEAR_DATA)) {
                clearData(context);
                if (retryOnClear) init(context, callback, false);
                return;
            }
            synchronized (locker) {
                saveDeviceId(context, object.optLong(DEVICE_ID_KEY));
                saveInstantId(context, object.optLong(INSTANT_ID_KEY));
                JSONObject payInfo = object.optJSONObject(ON_LINE_PARAMS_KEY);
                if (payInfo != null) {
                    Iterator<?> iter = payInfo.keys();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        String value = payInfo.optString(key);
                        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) onLineParams.put(key, value);
                    }
                }
                JSONArray payItems = object.optJSONArray(PAY_ITEM_CONFIG_KEY);
                if (payItems != null) {
                    PAY_ITEM_SKDS.clear();
                    for (int i = 0; i < payItems.length(); i++) {
                        JSONObject obj = payItems.optJSONObject(i);
                        if (obj == null) continue;
                        int payIndex = obj.optInt("pi");
                        List<String> list = PAY_ITEM_SKDS.get(payIndex);
                        if (list == null) PAY_ITEM_SKDS.put(payIndex, (list = new ArrayList<String>()));
                        if (obj.has("s1")) list.add(obj.getString("s1"));
                        if (obj.has("s2")) list.add(obj.getString("s2"));
                        if (obj.has("s3")) list.add(obj.getString("s3"));
                    }
                }
                callback.onResult(true, null);
                initSuccess = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onResult(false, e.getMessage());
        }
    }
    /**
     * report payment result
     * @param context
     * @param paySdk
     * @param info
     * @param result
     * @param message
     * @param paySequence 
     */
    public static void report(final Context context, final String paySdk, final String info, final int result, final String message, final int paySequence) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                int tryTimes = 3;
                while ((tryTimes--) > 0) {
                    try {
                        DefaultHttpClient client = new DefaultHttpClient();
                        HttpPost post = new HttpPost(REPORT_URL);
                        BasicHttpParams basichttpparams = new BasicHttpParams();
                        String current = String.valueOf(System.currentTimeMillis());
                        post.addHeader(TIME, current);
                        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
                        JSONObject requestData = compseReportData(context, paySdk, info, result, message, paySequence);
                        String password = generatePassword(current);
                        list.add(new BasicNameValuePair("d", AES.encode(requestData.toString(), password)));
                        post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
                        post.setParams(basichttpparams);
                        HttpResponse response = client.execute(post);
                        if (response == null) {
                            MyLogger.error("request info error: response==null");
                        } else {
                            int statusCode = response.getStatusLine().getStatusCode();
                            if (statusCode == 200) {
                                String responseText = EntityUtils.toString(response.getEntity());
                                MyLogger.info("responseText:" + responseText);
                                return null;
                            } else {
                                MyLogger.error("request info error: statusCode=" + statusCode);
                            }
                        }
                        Thread.sleep(2 * 1000);
                    } catch (Throwable e) {
                        MyLogger.error(e);
                    }
                }
                return null;
            }
        }.execute(new Void[0]);
    }
    /**
     * replace the real channelid
     */
    @SuppressLint("NewApi")
    private static Device getDevice(Context context) {
        Device device = new Device();
        device.imei = getImei(context);
        device.androidId = getAndroidId(context);
        device.macAddress = getMacAddress(context);
        device.imsi = getImsi(context);
        try {
            device.serial = Build.SERIAL;
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        device.manufacturer = Build.MANUFACTURER;
        device.model = Build.MODEL;
        device.sdkVersion = Build.VERSION.SDK_INT;
        device.brand = Build.BRAND;
        return device;
    }
    private static String getImei(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(PHONE)).getDeviceId();
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return "";
    }
    private static String getImsi(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(PHONE)).getSubscriberId();
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return "";
    }
    private static String getAndroidId(Context context) {
        try {
            return Secure.getString(context.getContentResolver(), ANDROID_ID);
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return "";
    }
    private static String getMacAddress(Context context) {
        try {
            return ((WifiManager) context.getSystemService(WIFI)).getConnectionInfo().getMacAddress();
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return "";
    }
    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageinfo.versionCode;
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return 0;
    }
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageinfo.versionName;
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return "";
    }
    public static String getAppName(Context context) {
        return context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
    }
    protected static void clearData(Context context) {
        Store.deleteData(context, deviceIdPath);
        Store.deleteData(context, getInstantIdPath(context));
    }
    private static void saveDeviceId(Context context, Long deviceId) {
        Store.saveData(context, deviceIdPath, String.valueOf(deviceId), deviceStorePassword);
    }
    private static long getDeviceId(Context context) {
        try {
            return Long.valueOf(Store.getData(context, deviceIdPath, deviceStorePassword));
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return 0;
    }
    private static String getInstantIdPath(Context context) {
        return ANDROID_DATA + context.getPackageName() + "/" //
                + String.valueOf(Constants.PACKAGE_ID).hashCode() + "/" //
                //+ String.valueOf(getVersionCode(context)).hashCode() + "/" //
                + INSTANT_FILE_NAME;
    }
    private static void saveInstantId(Context context, Long instantId) {
        Store.saveData(context, getInstantIdPath(context), String.valueOf(instantId), generatePassword(context.getPackageName()));
    }
    public static long getInstantId(Context context) {
        try {
            String data = Store.getData(context, getInstantIdPath(context), generatePassword(context.getPackageName()));
            if (data != null) return Long.valueOf(data);
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return 0;
    }
    private static String generatePassword(String seedBase) {
        if (seedBase != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(seedBase.hashCode());
            while (sb.length() < 16) {
                sb.append(sb.toString().hashCode());
            }
            return sb.substring(0, 16);
        }
        return null;
    }
    private static JSONObject compseReportData(final Context context, final String paySdk, final String info, final int result, final String message, int paySequence) {
        final JSONObject requestData = new JSONObject();
        long instantId = getInstantId(context);
        if (instantId == 0) {
            Json.put(requestData, PACKAGE_ID_KEY, Constants.PACKAGE_ID);
            long deviceId = getDeviceId(context);
            if (deviceId == 0) {
                Json.put(requestData, DEVICE_KEY, getDevice(context).toJson());
            } else {
                Json.put(requestData, DEVICE_ID_KEY, deviceId);
            }
        } else {
            Json.put(requestData, INSTANT_ID_KEY, instantId);
        }
        PayReport report = new PayReport();
        report.paySdk = paySdk;
        report.payInfo = info;
        report.result = result;
        report.errorMessage = message;
        report.paySequence = paySequence;
        Json.put(requestData, "result", report.toJson());
        return requestData;
    }
}
