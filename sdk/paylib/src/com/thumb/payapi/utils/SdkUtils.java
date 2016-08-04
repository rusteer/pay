package com.thumb.payapi.utils;
import java.util.HashSet;
import java.util.Set;
import android.content.Context;
import android.text.TextUtils;

public class SdkUtils {
    private static Set<String> tempSet = null;
    private static final Object locker=new Object();
    public static Set<String> getInjectedSdks(Context context) {
        if (tempSet == null) {
            synchronized (locker) {
                if (tempSet == null) {
                    tempSet = new HashSet<String>();
                    try {
                        String pcdata = FileUtils.readEncodedAssetFile(context, "pi/pcdata.db");
                        MyLogger.error("pc:" + pcdata);
                        for (String sdk : pcdata.split(",")) {
                            if (!TextUtils.isEmpty(sdk)) tempSet.add(sdk);
                        }
                    } catch (Throwable e) {
                        MyLogger.error(e);
                    }
                }
            }
        }
        return tempSet;
    }
}
