package com.example.pay;

import android.app.Application;
import com.thumb.payapi.Pay;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //在此处添加需要预先加载的.so
        //##PRE_LOAD_LIBS_HERE##
        Pay.onApplicationCreate(this);
    }
}
