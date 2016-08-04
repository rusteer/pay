package com.example.pay;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PayManager.init(this);
        setContentView(ResourceUtils.getLayoutIndex(this, "testmain"));
        this.findViewById(ResourceUtils.getIdIndex(this, "button1")).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(1);
            }
        });
    }
    protected void pay(int payId) {
        PayManager.pay(this, 0);
    }
}
