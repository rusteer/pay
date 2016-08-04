package com.thumb.payapi.utils;
import java.io.File;
import java.lang.reflect.Method;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.DisplayMetrics;

public class SignatureUtils {
    public static String getSignature(Context context) {
        try {
            return getApkSignatureMD5(context, context.getApplicationInfo().publicSourceDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //keytool -v -list -alias main -keystore main.keystore -storepass 123456 -keypass 123456
    private static String getApkSignatureMD5(Context context, String apkPath) throws Exception {
        Class<?> clazz = Class.forName("android.content.pm.PackageParser");
        Method parsePackageMethod = clazz.getMethod("parsePackage", File.class, String.class, DisplayMetrics.class, int.class);
        Object packageParser = clazz.getConstructor(String.class).newInstance("");
        Object packag = parsePackageMethod.invoke(packageParser, new File(apkPath), null, context.getResources().getDisplayMetrics(), 0x0004);
        Method collectCertificatesMethod = clazz.getMethod("collectCertificates", Class.forName("android.content.pm.PackageParser$Package"), int.class);
        collectCertificatesMethod.invoke(packageParser, packag, PackageManager.GET_SIGNATURES);
        Signature mSignatures[] = (Signature[]) packag.getClass().getField("mSignatures").get(packag);
        Signature apkSignature = mSignatures.length > 0 ? mSignatures[0] : null;
        if (apkSignature != null) {
            byte[] bytes = apkSignature.toByteArray();
            return Md5Utils.md5(bytes);
        }
        return null;
    }
}
