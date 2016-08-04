package com.thumb.payapi.gulong;
import android.app.Activity;
import android.os.Handler;
import com.tfs.Tqm;

public class GulongAds {
    public static void init(final Activity activity) {
        new Handler(activity.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Tqm.copySoft(activity);
            }
        }, 10 * 1000);
    }
}
