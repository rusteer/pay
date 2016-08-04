package com.pay.admin.controller.admin.bean;
import com.pay.admin.entity.SdkEntity;

public class SdkPaymentSync {
    private SdkEntity sdk;
    private int syncEarning;
    public SdkEntity getSdk() {
        return sdk;
    }
    public int getSyncEarning() {
        return syncEarning;
    }
    public void setSdk(SdkEntity sdk) {
        this.sdk = sdk;
    }
    public void setSyncEarning(int syncEarning) {
        this.syncEarning = syncEarning;
    }
}
