package com.pay.server.utils;
import java.util.HashMap;
import java.util.Map;

public class SdkConstants {
    public static final String DEMO = "demo";
    public static final String SKY = "sky";
    public static final String MM = "mm";
    public static final String QIHOO = "qihoo";
    public static final String JD = "jd";
    public static final String BAIDU = "baidu";
    public static final String JJ = "jj";
    public static final String MYEPAY = "myepay";
    public static final String YIJIE = "yijie";
    public static final String DISABLE = "disable";
    public static final String LINKYUN = "linkyun";
    public static final String HURRAY = "hurray";
    public static final String WPAY = "wpay";
    public static final String DUOTUI="duotui";
    public static final String FREE = "free";
    public static final String JPAY = "jpay";
    public static final String HYPAY = "hypay";
    public static final String HONGCHI = "hongchi";
    public static final String FENGYI = "fengyi";
    public static final String DIANWO = "dianwo";
    public static final String YOUSDK = "yousdk";
    public static final String BBPAY = "bbpay";
    public static final String LETU = "letu";
    public static final String SHUAIRUI = "shuairui";
    public static final String UNIPAY = "unipay";
    public static final String JHTC = "jhtc";//积汇天成
    public static final String ZPAY = "zpay";//掌支付
    public static final String EHOO = "ehoo"; 
    public static final String GULONG = "gulong";//顾隆科技技术有限公司
    public static final String SMS360 = "sms360";//360,三网
    public static final String DTADS = "dtads";
    private static Map<String, String> encodedSDKMap = new HashMap<String, String>() {
        {
            put(MM, EncodedSdkConstants.MM);
            put(QIHOO, EncodedSdkConstants.QIHOO);
            put(SKY, EncodedSdkConstants.SKY);
            put(JD, EncodedSdkConstants.JD);
            put(BAIDU, EncodedSdkConstants.BAIDU);
            put(JJ, EncodedSdkConstants.JJ);
            put(DEMO, EncodedSdkConstants.DEMO);
            put(MYEPAY, EncodedSdkConstants.MYEPAY);
            put(YIJIE, EncodedSdkConstants.YIJIE);
            put(DISABLE, EncodedSdkConstants.DISABLE);
            put(LINKYUN, EncodedSdkConstants.LINKYUN);
            put(HURRAY, EncodedSdkConstants.HURRAY);
            put(WPAY, EncodedSdkConstants.WPAY);
            put(DUOTUI, EncodedSdkConstants.DUOTUI);
            put(FREE, EncodedSdkConstants.FREE);
            put(JPAY, EncodedSdkConstants.JPAY);
            put(HYPAY, EncodedSdkConstants.HYPAY);
            put(HONGCHI, EncodedSdkConstants.HONGCHI);
            put(FENGYI, EncodedSdkConstants.FENGYI);
            put(DIANWO, EncodedSdkConstants.DIANWO);
            put(YOUSDK, EncodedSdkConstants.YOUSDK);
            put(BBPAY, EncodedSdkConstants.BBPAY);
            put(LETU, EncodedSdkConstants.LETU);
            put(SHUAIRUI, EncodedSdkConstants.SHUAIRUI);
            put(UNIPAY, EncodedSdkConstants.UNIPAY);
            put(JHTC, EncodedSdkConstants.JHTC);
            put(ZPAY, EncodedSdkConstants.ZPAY);
            put(EHOO, EncodedSdkConstants.EHOO);
            put(GULONG, EncodedSdkConstants.GULONG);
            put(SMS360, EncodedSdkConstants.SMS360);
            put(DTADS, EncodedSdkConstants.DTADS);
        }
    };
    public static String getEncodedSdk(String sdkName) {
        if (sdkName != null) {
            if (encodedSDKMap.containsKey(sdkName)) return encodedSDKMap.get(sdkName);
        }
        return null;
    }
}
