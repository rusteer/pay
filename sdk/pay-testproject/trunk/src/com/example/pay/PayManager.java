package com.example.pay;
import android.app.Activity;
import android.widget.Toast;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;

public class PayManager {
    public static void init(Activity activity) {
        Pay.init(activity);
    }
    public static void pay(final Activity activity, int id) {
       
        Pay.pay(activity, id, new PayCallback() {
            @Override
            public void onResult(final int result, String message,String payInfo) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "PayResult:" + result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
