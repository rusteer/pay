package test;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;
import com.test.R;
import com.thumb.payapi.Pay.PayCallback;
import com.thumb.payapi.baidu.BaiduPayTest;

public class Main extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BaiduPayTest.init(this);
        setContentView(R.layout.login);
        findViewById(R.id.pay_login).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // doTask();
                doTask2();
            }
        });
    }
    private void doTask2() {
        final Context context = this;
        BaiduPayTest.pay(this, 1, new PayCallback() {
            @Override
            public void onResult(boolean success, String errMessage, String payInfo) {
                Toast.makeText(context, success + "\n" + errMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}