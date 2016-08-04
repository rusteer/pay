package com.dt.demo;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.dt.p.SDKManager;

public class DemoActivity extends Activity {
    
    
    private static int windowWidth = 300;
    private static int windowHeight = 250;
    private static int confirmTopMargin = 90;
    private static int cancelTopMargin=80;
    private static int cancelLeftMargin=130;
    private static int msgTopMargin = 48;
    private SDKManager sdk;
    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SDKManager.WHAT_BUY_CALLBACK_DEFAULT:
                    String json3 = (String) msg.obj;
                    Log.d("TAG", "json2 --> " + json3);
                    try {
                        JSONObject point = new JSONObject(json3);
                        int resultCode = point.getInt("resultCode");
                        String desc = point.getString("desc");
                        double money = point.getDouble("money");
                        Log.d("TAG", "point resultCode = " + resultCode);
                        Log.d("TAG", "point desc = " + desc);
                        Log.d("TAG", "point money = " + money);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        sdk = SDKManager.getInstance(this);
        findViewById(R.id.buy).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sdk.buy("", mHandler, SDKManager.WHAT_BUY_CALLBACK_DEFAULT, 100, "购买100金币", SDKManager.TYPE_GAME);
                return true;
            }
        });
        ImageView image = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dip2px(this, windowWidth), dip2px(this, windowHeight));
        image.setLayoutParams(params);
        image.setScaleType(ScaleType.FIT_XY);
        image.setBackgroundResource(R.drawable.p00);
        sdk.setNegativeBtnResId(R.drawable.pclose);
        Point cancelPoint = new Point(dip2px(this, cancelLeftMargin), -dip2px(this, cancelTopMargin));
        sdk.setCancelPoint(cancelPoint);
        //
        Point msgPoint = new Point(new Point(dip2px(this, 0), dip2px(this, msgTopMargin)));
        sdk.setMsgPoint(msgPoint);
        sdk.setMsgFontSize(14);
        sdk.setMsgFontColor(0xaf, 0x56, 0x34, 0x89);
        
        
        sdk.setPositiveBtnResId(R.drawable.pconfirm);
        sdk.setOkPoint(new Point(0, dip2px(this, confirmTopMargin)));
        sdk.setCustomView(image);
        //
        //sdk.setTelFontColor(0xaf, 0x56, 0x34, 0x89);
        //sdk.setTelFontSize(12);
        //sdk.setTelPoint(new Point(dip2px(this, 0), dip2px(this, 80)));
        sdk.setTel(true, "");
        sdk.buy("", mHandler, SDKManager.WHAT_BUY_CALLBACK_DEFAULT, 100, "购买100金币", SDKManager.TYPE_GAME);
    }
    public int dip2px(Context ctx, int dpValue) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
