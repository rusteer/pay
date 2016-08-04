package com.example.hurray2pay;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.thumb.payapi.Pay;
import com.thumb.payapi.Pay.PayCallback;

public class InvokeActivity extends Activity {
    public static PayCallback callback;
    public static String payInfo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent recievedIntent = this.getIntent();
        Bundle receivedBundle = recievedIntent.getExtras();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("consumeCode", receivedBundle.getString("consumeCode"));
        intent.putExtras(bundle);
        intent.setClass(InvokeActivity.this, ZhiFuActivity.class);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (100 == resultCode) {
            callback.onResult(Pay.PAY_SUCCESS, null, payInfo);
        }
        if (101 == resultCode) {
            callback.onResult(Pay.PAY_FAILED, null, payInfo);
        }
        finish();
    }
}
