package com.example.testproject;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtils.getLayoutIndex(this, "activity_main"));
        Pay.init(this);
        this.findViewById(ResourceUtils.getIdIndex(this, "button1")).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Pay.pay(MainActivity.this, 0, new PayCallback() {
                    @Override
                    public void onResult(int result, String errMessage, String payInfo) {
                        showResult(result, errMessage, payInfo);
                    }
                });
            }
        });
        TextView textView = (TextView) this.findViewById(ResourceUtils.getIdIndex(this, "text1"));
    }
    private void showResult(final int result, String errMessage, final String payInfo) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, result + "\n" + payInfo, Toast.LENGTH_LONG).show();
            }
        });
    }
  
}
