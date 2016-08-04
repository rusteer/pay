package com.dt.p.d;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.dt.p.DtActivity;
import com.dt.p.SDKManager;
import com.dt.p.e.Class1071;
import com.dt.p.e.Class1075;
import com.dt.p.e.Class1079;

public final class Class1048 extends FrameLayout {
    private boolean hideMsg = true;
    private boolean hideTel = true;
    public Class1048(Activity activity) {
        super(activity);
        int maxColorSize = 208;
        h = 14;
        msgColor = 0xff333333;
        j = 14;
        teleColor = 0xff333333;
        sdkManager = SDKManager.getInstance(activity);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) activity.getSystemService("window")).getDefaultDisplay().getMetrics(displaymetrics);
        f = displaymetrics.widthPixels;
        g = displaymetrics.heightPixels;
        android.view.View view = sdkManager.getCustomView();
        a = new FrameLayout(activity);
        a.setFocusable(false);
        a.setClickable(false);
        LayoutParams layoutparams;
        if (view != null) {
            layoutparams = new android.widget.FrameLayout.LayoutParams(-2, -2);
            a.addView(view);
        } else {
            layoutparams = new android.widget.FrameLayout.LayoutParams(Class1075.a(getContext(), 300), Class1075.a(getContext(), 150));
            GradientDrawable gradientdrawable = new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[] { -1, -1, -1 });
            gradientdrawable.setCornerRadius(Class1075.a(activity, 20));
            a.setBackgroundDrawable(gradientdrawable);
        }
        layoutparams.gravity = 17;
        a.setLayoutParams(layoutparams);
        addView(a);
        b = new FrameLayout(activity);
        LayoutParams layoutparams1 = new android.widget.FrameLayout.LayoutParams(-1, -1);
        b.setLayoutParams(layoutparams1);
        b.setFocusable(true);
        b.setClickable(true);
        addView(b);
        ImageView imageview = new ImageView(getContext());
        LayoutParams layoutparams2 = new android.widget.FrameLayout.LayoutParams(-2, -2);
        Point cancelPoint = sdkManager.getCancelPoint();
        if (cancelPoint != null) {
            int cacelPointX = cancelPoint.x;
            int cancelPointY = cancelPoint.y;
            int xAbs = Math.abs(cacelPointX);
            int yAbs = Math.abs(cancelPointY);
            if (xAbs > (f << 1) / 3) cacelPointX = ((cacelPointX / xAbs) * f << 1) / 3;
            if (yAbs > (3 * g) / 4) cancelPointY = (3 * ((cancelPointY / yAbs) * g)) / 4;
            layoutparams2.topMargin = cancelPointY;
            layoutparams2.leftMargin = cacelPointX;
        } else {
            layoutparams2.topMargin = -Class1075.a(getContext(), 50);
            layoutparams2.leftMargin = Class1075.a(getContext(), 125);
        }
        layoutparams2.gravity = 17;
        imageview.setLayoutParams(layoutparams2);
        Drawable drawable = Class1071.a(getContext(), "cmge_pay_res/exit_normal.png");
        int cancelImageId = sdkManager.getNegativeBtnResId();
        if (cancelImageId != 0) {
            if (getResources().getDrawable(cancelImageId) != null) imageview.setImageResource(cancelImageId);
            else imageview.setImageDrawable(drawable);
        } else {
            imageview.setImageDrawable(drawable);
        }
        imageview.setId(1002);
        imageview.setOnClickListener((DtActivity) activity);
        b.addView(imageview);
        c = new ImageView(getContext());
        LayoutParams msgLayout = new android.widget.FrameLayout.LayoutParams(-2, -2);
        msgLayout.setMargins(2, 2, 2, 2);
        if (hideMsg) {
            msgLayout.topMargin = 100000;
            msgLayout.leftMargin = 100000;
        } else {
            Point msgPoint = sdkManager.getMsgPoint();
            if (msgPoint != null) {
                int msgPointX = msgPoint.x;
                int msgPointY = msgPoint.y;
                int j5 = Math.abs(msgPointX);
                int k5 = Math.abs(msgPointY);
                if (j5 > f / 3) msgPointX = ((msgPointX / j5) * f) / 3;
                if (k5 > g / 4) msgPointY = ((msgPointY / k5) * g) / 4;
                msgLayout.topMargin = msgPointY;
                msgLayout.leftMargin = msgPointX;
            }
        }
        msgLayout.gravity = 17;
        c.setLayoutParams(msgLayout);
        int msgAlpha = sdkManager.getMsgAlpha();
        int msgRed = sdkManager.getMsgRed();
        int msgGreen = sdkManager.getMsgGreen();
        int msgBlue = sdkManager.getMsgBlue();
        if (msgAlpha == -1 && msgRed == -1 && msgGreen == -1 && msgBlue == -1) {
            msgColor = 0xff333333;
        } else {
            if (msgAlpha < 112) msgAlpha = 112;
            else if (msgAlpha > 255) msgAlpha = 255;
            if (msgRed < 0) msgRed = 0;
            if (msgRed < 0) msgRed = 0;
            if (msgGreen < 0) msgGreen = 0;
            if (msgRed > maxColorSize && msgGreen > maxColorSize && msgBlue > maxColorSize) {
                msgBlue = maxColorSize;
                msgGreen = maxColorSize;
                msgRed = maxColorSize;
            }
            msgColor = msgBlue + ((msgAlpha << 8 << 8 << 8) + (msgRed << 8 << 8) + (msgGreen << 8));
        }
        h = sdkManager.getMsgFontSize();
        if (h < 12) h = 12;
        b.addView(c);
        if (!hideTel && !sdkManager.isNoTel()) {
            //L1_L1:
            imageViewD = new ImageView(getContext());
            LayoutParams telParams = new android.widget.FrameLayout.LayoutParams(-2, -2);
            telParams.setMargins(2, 2, 2, 2);
            Point telePoint = sdkManager.getTelPoint();
            if (telePoint != null) {
                int x = telePoint.x;
                int y = telePoint.y;
                int absX = Math.abs(x);
                int absY = Math.abs(y);
                if (absX > f / 3) x = ((x / absX) * f) / 3;
                if (absY > g / 4) y = ((y / absY) * g) / 4;
                telParams.topMargin = y;
                telParams.leftMargin = x;
            }
            telParams.gravity = 17;
            imageViewD.setLayoutParams(telParams);
            int telAlpha = sdkManager.getTelAlpha();
            int telRed = sdkManager.getTelRed();
            int telGreen = sdkManager.getTelGreen();
            int telBlue = sdkManager.getTelBlue();
            if (telAlpha == -1 && telRed == -1 && telGreen == -1 && telBlue == -1) {
                teleColor = 0xff333333;
            } else {
                if (telAlpha < 112) {
                    telAlpha = 112;
                } else {
                    if (telAlpha > 255) telAlpha = 255;
                }
                if (telRed < 0) telRed = 0;
                if (telRed < 0) telRed = 0;
                if (telGreen < 0) telGreen = 0;
                int j3;
                int k3;
                if (telRed > maxColorSize && telGreen > maxColorSize && telBlue > maxColorSize) {
                    j3 = maxColorSize;
                    k3 = maxColorSize;
                } else {
                    maxColorSize = telBlue;
                    j3 = telGreen;
                    k3 = telRed;
                }
                teleColor = maxColorSize + ((telAlpha << 8 << 8 << 8) + (k3 << 8 << 8) + (j3 << 8));
            }
            j = sdkManager.getTelFontSize();
            if (j < 12) j = 12;
            b.addView(imageViewD);
        }
    }
    private void a(int l, int i1, Point point) {
        Button button = new Button(getContext());
        button.setTextColor(-1);
        button.setId(1001);
        button.setTag(Integer.valueOf(l));
        button.setTextSize(18F);
        button.setPadding(0, 0, 0, 0);
        button.setOnClickListener((DtActivity) getContext());
        android.widget.FrameLayout.LayoutParams layoutparams1;
        if (i1 != 0) {
            if (getResources().getDrawable(i1) != null) {
                button.setText("");
                android.widget.FrameLayout.LayoutParams layoutparams3 = new android.widget.FrameLayout.LayoutParams(-2, -2);
                button.setBackgroundResource(i1);
                layoutparams1 = layoutparams3;
            } else {
                button.setText("确定");
                android.widget.FrameLayout.LayoutParams layoutparams2 = new android.widget.FrameLayout.LayoutParams(Class1075.a(getContext(), 150), Class1075.a(getContext(), 35));
                button.setBackgroundDrawable(Class1079.a(getContext(), 0xff2acdd9, 0xff008a97));
                layoutparams1 = layoutparams2;
            }
        } else {
            button.setText("确定");
            android.widget.FrameLayout.LayoutParams layoutparams = new android.widget.FrameLayout.LayoutParams(Class1075.a(getContext(), 150), Class1075.a(getContext(), 35));
            button.setBackgroundDrawable(Class1079.a(getContext(), 0xff2acdd9, 0xff008a97));
            layoutparams1 = layoutparams;
        }
        if (point != null) {
            int j1 = point.x;
            int k1 = point.y;
            int l1 = Math.abs(j1);
            int i2 = Math.abs(k1);
            if (l1 > (f << 1) / 3) j1 = ((j1 / l1) * f << 1) / 3;
            if (i2 > (3 * g) / 4) k1 = (3 * ((k1 / i2) * g)) / 4;
            layoutparams1.topMargin = k1;
            layoutparams1.leftMargin = j1;
        } else {
            layoutparams1.topMargin = Class1075.a(getContext(), 45);
        }
        layoutparams1.gravity = 17;
        button.setLayoutParams(layoutparams1);
        b.addView(button, 1);
    }
    private void a(int ai[], Point apoint[]) {
        int l = 0;
        do {
            if (l >= ai.length) return;
            a(l, ai[l], apoint[l]);
            l++;
        } while (true);
    }
    public final void a() {
        android.view.View view = sdkManager.getCustomView();
        if (view != null) a.removeView(view);
    }
    public final void a(String s) {
        int l = Class1075.a(getContext(), 10 + h);
        ArrayList arraylist = new ArrayList();
        arraylist.add(s);
        android.graphics.Bitmap bitmap = Class1075.a(Class1075.a(getContext(), 300), l, arraylist, Class1075.a(getContext(), h), msgColor);
        c.setImageBitmap(bitmap);
        int ai[] = sdkManager.getPositiveBtnResIds();
        Point apoint[] = sdkManager.getOkPoints();
        if (apoint != null && ai != null) a(ai, apoint);
        if (!sdkManager.isNoTel()) {
            int i1 = Class1075.a(getContext(), 10 + j);
            String s1 = sdkManager.getTel();
            if (TextUtils.isEmpty(s1)) s1 = "客服电话:4008760003";
            ArrayList arraylist1 = new ArrayList();
            arraylist1.add(s1);
            android.graphics.Bitmap bitmap1 = Class1075.a(Class1075.a(getContext(), 300), i1, arraylist1, Class1075.a(getContext(), j), teleColor);
            imageViewD.setImageBitmap(bitmap1);
        }
    }
    public final void a(List list) {
        int l;
        android.graphics.Bitmap bitmap;
        if (list.size() == 1) l = Class1075.a(getContext(), 10 + h);
        else l = Class1075.a(getContext(), 10 + h) << 1;
        bitmap = Class1075.a(Class1075.a(getContext(), 300), l, list, Class1075.a(getContext(), h), msgColor);
        c.setImageBitmap(bitmap);
        a(-1, sdkManager.getPositiveBtnResId(), sdkManager.getOkPoint());
        if (!sdkManager.isNoTel()) {
            int i1 = Class1075.a(getContext(), 10 + j);
            String s = sdkManager.getTel();
            if (TextUtils.isEmpty(s)) s = "客服电话:4008760003";
            ArrayList arraylist = new ArrayList();
            arraylist.add(s);
            android.graphics.Bitmap bitmap1 = Class1075.a(Class1075.a(getContext(), 300), i1, arraylist, Class1075.a(getContext(), j), teleColor);
            imageViewD.setImageBitmap(bitmap1);
        }
    }
    private FrameLayout a;
    private FrameLayout b;
    private ImageView c;
    private ImageView imageViewD;
    private SDKManager sdkManager;
    private int f;
    private int g;
    private int h;
    private int msgColor;
    private int j;
    private int teleColor;
}
